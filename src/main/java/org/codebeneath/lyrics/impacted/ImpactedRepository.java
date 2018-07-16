package org.codebeneath.lyrics.impacted;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 *  JPA repository for Impacted users.
 */
public interface ImpactedRepository extends CrudRepository<Impacted, Long> {
    
    List<Impacted> findByLastName(String lastName);

    List<Impacted> findByFirstName(String firstName);

}
