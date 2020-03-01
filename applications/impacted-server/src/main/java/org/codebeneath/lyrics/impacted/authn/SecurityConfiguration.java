package org.codebeneath.lyrics.impacted.authn;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {         
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers(
                        "/**").permitAll();
        
        // dev only, h2-console access...
        http.csrf().disable();
        http.headers().frameOptions().disable();
    }
}