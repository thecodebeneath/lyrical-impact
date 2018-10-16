package org.codebeneath.lyrics.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Health check of remote service providing verse lookup
 */
@Component
public class PoetrydbHealthIndicator implements HealthIndicator {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String poetryDbUrl = "http://poetrydb.org/author";
        
    @Override
    public Health health() {
        if (isRemoteServiceUp()) {
            return Health.up()
                    .withDetail("remote service", "online")
                    .build();
        } else {
            return Health.down()
                    .withDetail("remote service", "offline")
                    .build();
        }
    }

    private boolean isRemoteServiceUp() {
        ResponseEntity<String> response = restTemplate.getForEntity(poetryDbUrl, String.class);
        return (response.getStatusCode() == HttpStatus.OK);
    }
}
