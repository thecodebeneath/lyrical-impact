package org.codebeneath.lyrics.verses;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA repository for Verses.
 */
@Repository
public interface VersesRepository extends CrudRepository<Verse, Long> {

    // ====== my =======
    
    Optional<Verse> findByIdAndImpactedId(Long vid, Long id);

    List<Verse> findByImpactedId(Long id);

    List<Verse> findByImpactedId(Long id, Pageable pageable);

    List<Verse> findByImpactedIdAndTags(Long id, String label);

    List<Verse> findByImpactedIdAndTags(Long id, String label, Pageable pageable);

    List<Verse> findByImpactedIdAndTextContainsIgnoreCase(Long id, String query);

    List<Verse> findByImpactedIdAndTextContainsIgnoreCase(Long id, String query, Pageable pageable);

    // ====== global =======

    @Override
    List<Verse> findAll();

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
