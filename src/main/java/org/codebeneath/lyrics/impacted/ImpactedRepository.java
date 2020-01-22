package org.codebeneath.lyrics.impacted;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

/**
 *  JPA repository for Impacted users.
 */
public interface ImpactedRepository extends CrudRepository<Impacted, Long> {
    
    Optional<Impacted> findByUserName(String userName);

    Optional<Impacted> findByEmail(String email);
    
    List<Impacted> findByFirstNameAndLastName(String firstName, String lastName);

}
