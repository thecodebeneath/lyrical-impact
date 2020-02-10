package org.codebeneath.lyrics.verse;

import java.util.List;
import java.util.Optional;
import org.codebeneath.lyrics.impacted.Impacted;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * JPA repository for Verses.
 */
public interface VerseRepository extends CrudRepository<Verse, Long> {

    // ====== my =======
    
    Optional<Verse> findByIdAndImpactedId(Long vid, Long id);

    List<Verse> findByImpacted(Impacted impacted);

    List<Verse> findByImpactedId(Long id);

    List<Verse> findByImpactedId(Long id, Pageable pageable);

    List<Verse> findByImpactedIdAndTags(Long id, String label);

    List<Verse> findByImpactedIdAndTags(Long id, String label, Pageable pageable);

    List<Verse> findByImpactedIdAndTextContainsIgnoreCase(Long id, String query);

    List<Verse> findByImpactedIdAndTextContainsIgnoreCase(Long id, String query, Pageable pageable);

    // ====== global =======

    List<Verse> findAll(Pageable pageable);

    List<Verse> findByTags(String label);

    List<Verse> findByTags(String label, Pageable pageable);

    List<Verse> findByTextContainsIgnoreCase(String query);

    List<Verse> findByTextContainsIgnoreCase(String query, Pageable pageable);

    // ====== utility =======
    
    @Query(value = "SELECT * FROM verse order by rand() limit 1", nativeQuery = true)
    Verse getRandomVerse();

    // same tag used by multiple users
    long countByTags(String label);
}
