package org.codebeneath.lyrics.impactedapi;

import java.util.List;
import java.util.Optional;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author black
 */
@FeignClient(name = "lyrical-impact-impacted")
public interface ImpactedClient {

    @GetMapping(path = "/impacted/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    Optional<ImpactedUser> findById(@PathVariable Long id);

    @GetMapping(path = "/impacted/byRole/{role}", produces = MediaType.APPLICATION_JSON_VALUE)    
    List<ImpactedUser> findByRolesContains(@PathVariable String role);
    
    @PostMapping(path = "/impacted", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ImpactedUser save(ImpactedUserDto impactedUser);

    @GetMapping(path = "/impacted/byUniqueId/{uid}", produces = MediaType.APPLICATION_JSON_VALUE)
    Optional<ImpactedUser> findByUniqueId(@PathVariable String uid);

}
