package org.codebeneath.lyrics.tags;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *  JPA repository for Tags that an impacted user has defined.
 */
@Repository
public interface TagsRepository extends CrudRepository<Tag, String> {
    
    @Override
    List<Tag> findAll();
    
    Optional<Tag> findByLabel(String label);
    
}
