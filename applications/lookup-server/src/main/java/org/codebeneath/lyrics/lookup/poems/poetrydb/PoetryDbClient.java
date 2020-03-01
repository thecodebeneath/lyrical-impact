package org.codebeneath.lyrics.lookup.poems.poetrydb;

import lombok.extern.slf4j.Slf4j;
import org.codebeneath.lyrics.lookup.api.ExternalApi;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * PoetryDB API
 * Ref: https://github.com/thundercomb/poetrydb/blob/master/README.md
 */
@Slf4j
public class PoetryDbClient implements ExternalApi {

    public static final String URL = "http://poetrydb.org/lines/{lines}";
    private final RestTemplate restTemplate = new RestTemplate();
    
    @Override
    public void call() throws RestClientException {
        PoetryDbEntry[] p = restTemplate.getForObject(URL, PoetryDbEntry[].class, "foe to my own friends");

        log.info("poetrydb: {}", p[0].getLines().get(0));
//        for (int x = 0; x < p.length; x++) {
//            log.info("poetrydb: {}", p[x].toString());
//        }

    }
}
