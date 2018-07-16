package org.codebeneath.lyrics.tag;

import java.util.List;
import org.codebeneath.lyrics.impacted.Impacted;
import org.springframework.data.repository.CrudRepository;

/**
 *  JPA repository for Tags that an impacted user has defined.
 */
public interface TagRepository extends CrudRepository<Tag, String> {

    List<Tag> findByImpacted(Impacted impacted);

    List<Tag> findByImpactedId(Long id);
}
