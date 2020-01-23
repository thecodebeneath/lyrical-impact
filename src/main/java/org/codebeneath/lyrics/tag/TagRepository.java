package org.codebeneath.lyrics.tag;

import org.springframework.data.repository.CrudRepository;

/**
 *  JPA repository for Tags that an impacted user has defined.
 */
public interface TagRepository extends CrudRepository<Tag, String> {

}
