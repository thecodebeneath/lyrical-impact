package org.codebeneath.lyrics.authn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
//@EnableWebSecurity(debug=true)
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
 
    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_USER = "USER";
    
    @Autowired
    private AuthenticationSuccessHandler successHandler;

    @Autowired
    private AuthenticationFailureHandler failureHandler;
    
    @Autowired
    private LoggingAccessDeniedHandler accessDeniedHandler;
    
    @Autowired
    private OAuth2UserService<OidcUserRequest,OidcUser> customOidcUserService;
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
        
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers(
                            "/",
                            "/about",
                            "/login",
                            "/global",
                            "/verses/metrics/**",
                            "/robot.txt",
                            "/robots.txt",
                            "/js/**",
                            "/css/**",
                            "/img/**",
                            "/webjars/**").permitAll()
                    .antMatchers("/user/**").hasRole(ROLE_USER)
                    .antMatchers("/my/peek/**", "/admin/**", "/seed/**", "/h2-console/**", "/actuator/**").hasRole(ROLE_ADMIN)
                    .anyRequest().authenticated()
                .and()
                .logout()
                    .logoutSuccessUrl("/login?logout")
                    .permitAll()
                .and()
                .exceptionHandling()
                    .accessDeniedHandler(accessDeniedHandler)
                .and()
                .oauth2Login()
                    .loginPage("/login") // my view, not the built-in security filter page "/login"
                    .successHandler(successHandler)
                    .failureHandler(failureHandler)
                    .userInfoEndpoint()
                        .oidcUserService(customOidcUserService); // okta, google
        
        // dev only, h2-console access...
        http.csrf().disable();
        http.headers().frameOptions().disable();
    }
}