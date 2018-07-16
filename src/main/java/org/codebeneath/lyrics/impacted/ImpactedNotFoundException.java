package org.codebeneath.lyrics.impacted;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
class ImpactedNotFoundException extends RuntimeException {

    public ImpactedNotFoundException(String firstName) {
        super("Could not find a impacted user named '" + firstName + "'.");
    }

}
