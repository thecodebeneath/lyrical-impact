package org.codebeneath.lyrics.tags;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class TagsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TagsServiceApplication.class, args);
    }

}
