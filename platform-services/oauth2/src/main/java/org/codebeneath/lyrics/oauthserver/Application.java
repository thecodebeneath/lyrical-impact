package org.codebeneath.lyrics.oauthserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;

@SpringBootApplication
@ComponentScan("org.codebeneath.lyrics.oauthserver")
public class Application extends AuthorizationServerConfigurerAdapter {

    // ref: https://github.com/spring-projects/spring-security/wiki/OAuth-2.0-Migration-Guide
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}