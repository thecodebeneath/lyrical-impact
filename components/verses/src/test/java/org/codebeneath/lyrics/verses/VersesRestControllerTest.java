package org.codebeneath.lyrics.verses;

import io.github.benas.randombeans.api.EnhancedRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import org.springframework.data.domain.Pageable;

public class VersesRestControllerTest {

    private VersesRepository vrepo = mock(VersesRepository.class);
    private VersesRestController controller = new VersesRestController(vrepo);

    List<Verse> noVerses = Collections.emptyList();
    List<Verse> matchedVerses = new ArrayList<>(1);
    List<Verse> randomVerses = new ArrayList<>();
    Verse randomVerse;

    @Before
    public void setUp() {
        randomVerse = EnhancedRandom.random(Verse.class);
        matchedVerses.add(randomVerse);
        
        randomVerses.addAll(matchedVerses);
        for (int i = 1; i <= 10; i++) {
            randomVerses.add(EnhancedRandom.random(Verse.class));
        }
    }

    @Test
    public void testGlobalNoFiltersReturnsAllVerses() {
        when(vrepo.findAll(any(Pageable.class))).thenReturn(randomVerses);

        List<Verse> result = controller.findGlobal(0, null, null);

        assertThat(result).hasSize(11)
                .contains(randomVerse);
    }

    @Test
    public void testGlobalTagReturnsMatchingVerses() {
        when(vrepo.findByTags(eq("tag"), any(Pageable.class))).thenReturn(matchedVerses);

        List<Verse> result = controller.findGlobal(0, "tag", null);

        assertThat(result).hasSize(1)
                .contains(randomVerse);
    }
    
    @Test
    public void testGlobalSearchReturnsMatchingVerses() {
        when(vrepo.findByTextContainsIgnoreCase(eq("search"), any(Pageable.class))).thenReturn(matchedVerses);

        List<Verse> result = controller.findGlobal(0, null, "search");

        assertThat(result).hasSize(1)
                .contains(randomVerse);
    }

}
