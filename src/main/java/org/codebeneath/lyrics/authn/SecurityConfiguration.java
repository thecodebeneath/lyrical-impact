package org.codebeneath.lyrics.authn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
 
    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_USER = "USER";
    
    @Autowired
    private AuthenticationSuccessHandler successHandler;

    @Autowired
    private LoggingAccessDeniedHandler accessDeniedHandler;
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("alan").password(passwordEncoder().encode("alan")).roles(ROLE_USER)
                .and()
                .withUser("jeff").password(passwordEncoder().encode("jeff")).roles(ROLE_ADMIN, ROLE_USER);
    }
    
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers(
                            "/",
                            "/about",
                            "/js/**",
                            "/css/**",
                            "/img/**",
                            "/webjars/**").permitAll()
                    .antMatchers("/user/**").hasRole(ROLE_USER)
                    .antMatchers("/admin/**", "/h2-console/**").hasRole(ROLE_ADMIN)
                    .anyRequest().authenticated()
                .and()
                .formLogin()
                    .loginPage("/login")
                    // .successHandler(successHandler)
                    .permitAll()
                .and()
                .logout()
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login?logout")
                    .deleteCookies("JSESSIONID")
                    .permitAll()
                .and()
                .exceptionHandling()
                    .accessDeniedHandler(accessDeniedHandler);
        
        // dev only, h2-console access...
        http.csrf().disable();
        http.headers().frameOptions().disable();
    }
}