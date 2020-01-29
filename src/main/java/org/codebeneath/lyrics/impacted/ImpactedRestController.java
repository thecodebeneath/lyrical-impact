package org.codebeneath.lyrics.impacted;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
public class ImpactedRestController {

    private final ImpactedRepository repo;

    public ImpactedRestController(ImpactedRepository repo) {
        this.repo = repo;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/impacted/{id}/json")
    public Impacted impactedVerses(@PathVariable(required = true) Long id) {
        return repo.findById(id).get();
    }

}
