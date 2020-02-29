package org.codebeneath.lyrics.verses;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 *
 * @author black
 */
@EnableDiscoveryClient
@SpringBootApplication
public class VersesServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(VersesServiceApplication.class, args);
    }
}