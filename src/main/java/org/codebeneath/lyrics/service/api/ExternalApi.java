package org.codebeneath.lyrics.service.api;

import org.springframework.web.client.RestClientException;

/**
 *
 * @author black
 */
public interface ExternalApi {

    public void call() throws RestClientException;
}
