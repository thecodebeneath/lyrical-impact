package org.codebeneath.lyrics.tags;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

/**
 *  JPA repository for Tags that an impacted user has defined.
 */
public interface TagsRepository extends CrudRepository<Tag, String> {
    
    @Override
    List<Tag> findAll();
    
    Optional<Tag> findByLabel(String label);
    
}
