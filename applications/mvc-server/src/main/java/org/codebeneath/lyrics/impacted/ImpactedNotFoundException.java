package org.codebeneath.lyrics.impacted;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ImpactedNotFoundException extends RuntimeException {

    public ImpactedNotFoundException(String userName) {
        super("Could not find a impacted user name '" + userName + "'.");
    }

    public ImpactedNotFoundException(Long id) {
        super("Could not find a impacted user id '" + id + "'.");
    }

}
