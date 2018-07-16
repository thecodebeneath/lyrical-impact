package org.codebeneath.lyrics.verse;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 * JPA repository for Verses.
 */
public interface VerseRepository extends CrudRepository<Verse, Long> {

    List<Verse> findAllByTagsLabel(String label);

}
