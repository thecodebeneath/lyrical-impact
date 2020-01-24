package org.codebeneath.lyrics.tag;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

/**
 *  JPA repository for Tags that an impacted user has defined.
 */
public interface TagRepository extends CrudRepository<Tag, String> {
    
    Optional<Tag> findByLabel(String label);
    
}
