package org.codebeneath.lyrics.lookup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Ideas for external API calls to help users lookup title/author given the verse text.
 */  
@EnableDiscoveryClient
@SpringBootApplication
public class LookupServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LookupServiceApplication.class, args);
    }

}