package org.codebeneath.lyrics.impacted;

import java.util.List;
import java.util.Optional;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
@RequestMapping("/impacted")
public class ImpactedRestController {

    private final ImpactedRepository iRepo;

    public ImpactedRestController(ImpactedRepository iRepo) {
        this.iRepo = iRepo;
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Impacted findById(@PathVariable Long id) {
        return iRepo.findById(id).get();
    }

    @GetMapping(path = "/byRole/{role}", produces = MediaType.APPLICATION_JSON_VALUE)    
    List<Impacted> findByRolesContains(@PathVariable String role) {
        return iRepo.findByRolesContains(role);
    }
    
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Impacted save(@RequestBody Impacted impactedUser) {
        return iRepo.save(impactedUser);
    }

    @GetMapping(path = "/byUniqueId/{uid}", produces = MediaType.APPLICATION_JSON_VALUE)
    Optional<Impacted> findByUniqueId(@PathVariable String uid) {
        return iRepo.findByUniqueId(uid);
    }
}
