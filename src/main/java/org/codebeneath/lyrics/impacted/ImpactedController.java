package org.codebeneath.lyrics.impacted;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 */
@Controller
public class ImpactedController {

    private ImpactedRepository repo;

    public ImpactedController(ImpactedRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/impacted/{firstName}")
    public String impactedVerses(@PathVariable(required = true) String firstName, Model model) {
        validateImpacted(firstName);

        Impacted jeff = repo.findByFirstName(firstName).get(0);
        model.addAttribute("impacted", jeff);
        return "impacted";
    }

    private void validateImpacted(String firstName) {
        if (repo.findByFirstName(firstName).isEmpty()) {
            throw new ImpactedNotFoundException(firstName);
        }
    }
}
