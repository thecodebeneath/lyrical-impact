package org.codebeneath.lyrics.verse;

import java.util.List;
import java.util.Optional;
import org.codebeneath.lyrics.impacted.Impacted;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * JPA repository for Verses.
 */
public interface VerseRepository extends CrudRepository<Verse, Long> {

    Optional<Verse> findByIdAndImpactedId(Long vid, Long id);

    List<Verse> findByImpacted(Impacted impacted);

    List<Verse> findByImpactedId(Long id);

    // all by tag for one user
    List<Verse> findByImpactedIdAndTags(Long id, String label);

    List<Verse> findByImpactedIdAndTextContainsIgnoreCase(Long id, String query);

    List<Verse> findByTextContainsIgnoreCase(String query);
    
    // all by tag
    List<Verse> findByTags(String label);

    @Query(value = "SELECT * FROM verse order by random() limit 1", nativeQuery = true)
    Verse getRandomVerse();

    // same tag used by multiple users
    long countByTags(String label);
}
