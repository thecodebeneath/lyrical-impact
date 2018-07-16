package org.codebeneath.lyrics.impacted;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
public class ImpactedRestController {

    private ImpactedRepository repo;

    public ImpactedRestController(ImpactedRepository repo) {
        this.repo = repo;
    }

    @RequestMapping("/impacted/json")
    public Impacted impactedVerses(@RequestParam(value = "firstName", defaultValue = "Jeff") String firstName) {
        return repo.findByFirstName(firstName).get(0);
    }
}
