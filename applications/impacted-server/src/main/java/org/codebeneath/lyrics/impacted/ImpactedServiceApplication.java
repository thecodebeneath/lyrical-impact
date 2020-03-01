package org.codebeneath.lyrics.impacted;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 *
 * @author black
 */
@EnableDiscoveryClient
@SpringBootApplication
public class ImpactedServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImpactedServiceApplication.class, args);
    }
}