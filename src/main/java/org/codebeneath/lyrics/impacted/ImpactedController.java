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

    private final ImpactedRepository repo;

    public ImpactedController(ImpactedRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/impacted/{id}")
    public String impactedVerses(@PathVariable(required = true) Long id, Model model) {
        validateImpacted(id);

        Impacted jeff = repo.findById(id).get();
        model.addAttribute("impacted", jeff);
        return "impacted";
    }

    private void validateImpacted(Long id) {
        if (!repo.findById(id).isPresent()) {
            throw new ImpactedNotFoundException(id);
        }
    }
}
