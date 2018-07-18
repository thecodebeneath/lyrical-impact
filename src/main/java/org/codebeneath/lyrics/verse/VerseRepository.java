package org.codebeneath.lyrics.verse;

import java.util.List;
import org.codebeneath.lyrics.impacted.Impacted;
import org.springframework.data.repository.CrudRepository;

/**
 * JPA repository for Verses.
 */
public interface VerseRepository extends CrudRepository<Verse, Long> {

    List<Verse> findByImpacted(Impacted impacted);

    List<Verse> findByImpactedId(Long id);
    
    // all by tag for one user
    List<Verse> findByImpactedIdAndTagsLabel(Long id, String label);

    // same tag used by multiple users
    long countByTagsLabel(String label);

}
