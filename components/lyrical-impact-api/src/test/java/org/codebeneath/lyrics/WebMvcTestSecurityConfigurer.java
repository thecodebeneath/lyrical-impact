package org.codebeneath.lyrics;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Supporting @WebMvcTest controller tests to "disabled" spring security 
 */
@Configuration
public class WebMvcTestSecurityConfigurer extends WebSecurityConfigurerAdapter{

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/**").permitAll();
    }
}
