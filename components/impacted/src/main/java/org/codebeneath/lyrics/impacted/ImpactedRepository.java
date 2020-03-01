package org.codebeneath.lyrics.impacted;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *  JPA repository for Impacted users.
 */
@Repository
public interface ImpactedRepository extends CrudRepository<Impacted, Long> {
    
    Optional<Impacted> findByUniqueId(String uniqueId);

    List<Impacted> findByRolesContains(String role);

    Optional<Impacted> findByEmail(String email);
    
}
