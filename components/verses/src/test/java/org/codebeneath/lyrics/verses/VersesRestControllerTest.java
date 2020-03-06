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
    private static final Long MY_IMPACTED_ID = -1L;
    private static final String TAG_TEXT = "tag";
    private static final String QUERY_TEXT = "search";

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
        when(vrepo.findByTags(eq(TAG_TEXT), any(Pageable.class))).thenReturn(matchedVerses);
        List<Verse> result = controller.findGlobal(0, TAG_TEXT, null);
        assertThat(result).hasSize(1)
                .contains(randomVerse);
    }
    
    @Test
    public void testGlobalSearchReturnsMatchingVerses() {
        when(vrepo.findByTextContainsIgnoreCase(eq(QUERY_TEXT), any(Pageable.class))).thenReturn(matchedVerses);
        List<Verse> result = controller.findGlobal(0, null, QUERY_TEXT);
        assertThat(result).hasSize(1)
                .contains(randomVerse);
    }

    @Test
    public void testGlobalTagSearchReturnsMatchingVerses() {
        when(vrepo.findByTagsAndTextContainsIgnoreCase(eq(TAG_TEXT), eq(QUERY_TEXT), any(Pageable.class))).thenReturn(matchedVerses);
        List<Verse> result = controller.findGlobal(0, TAG_TEXT, QUERY_TEXT);
        assertThat(result).hasSize(1)
                .contains(randomVerse);
    }

    @Test
    public void testMyNoFiltersReturnsAllVerses() {
        when(vrepo.findByImpactedId(eq(MY_IMPACTED_ID), any(Pageable.class))).thenReturn(randomVerses);
        List<Verse> result = controller.findByImpactedId(MY_IMPACTED_ID, 0, null, null);
        assertThat(result).hasSize(11)
                .contains(randomVerse);
    }

    @Test
    public void testMyTagReturnsMatchingVerses() {
        when(vrepo.findByImpactedIdAndTags(eq(MY_IMPACTED_ID), eq(TAG_TEXT), any(Pageable.class))).thenReturn(matchedVerses);
        List<Verse> result = controller.findByImpactedId(MY_IMPACTED_ID, 0, TAG_TEXT, null);
        assertThat(result).hasSize(1)
                .contains(randomVerse);
    }
    
    @Test
    public void testMySearchReturnsMatchingVerses() {
        when(vrepo.findByImpactedIdAndTextContainsIgnoreCase(eq(MY_IMPACTED_ID), eq(QUERY_TEXT), any(Pageable.class))).thenReturn(matchedVerses);
        List<Verse> result = controller.findByImpactedId(MY_IMPACTED_ID, 0, null, QUERY_TEXT);
        assertThat(result).hasSize(1)
                .contains(randomVerse);
    }

    @Test
    public void testMyTagSearchReturnsMatchingVerses() {
        when(vrepo.findByImpactedIdAndTagsAndTextContainsIgnoreCase(eq(MY_IMPACTED_ID), eq(TAG_TEXT), eq(QUERY_TEXT), any(Pageable.class))).thenReturn(matchedVerses);
        List<Verse> result = controller.findByImpactedId(MY_IMPACTED_ID, 0, TAG_TEXT, QUERY_TEXT);
        assertThat(result).hasSize(1)
                .contains(randomVerse);
    }

}
