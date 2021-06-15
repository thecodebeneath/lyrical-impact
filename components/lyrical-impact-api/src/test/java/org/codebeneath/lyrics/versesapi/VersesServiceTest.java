package org.codebeneath.lyrics.versesapi;

import static java.util.Arrays.asList;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import org.codebeneath.lyrics.tagsapi.VerseTag;
import org.codebeneath.lyrics.tagsapi.VerseTagsService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VersesServiceTest {
    
    private VersesClient vClient = mock(VersesClient.class);
    private VerseTagsService tService = mock(VerseTagsService.class);
    static List<VerseTag> allTags;
    static VerseTag happyTag = new VerseTag("happy");
    static VerseTag sadTag = new VerseTag("sad");
    static VerseTag noneTag = new VerseTag("none");

    @BeforeAll
    public static void setUpBeforeAll() {
        allTags = asList(happyTag, sadTag, noneTag);
    }
    
    @Test
    public void allTagsReturnVerseCountInOrder() {
        when(vClient.countByTags(eq("happy"))).thenReturn("60");
        when(vClient.countByTags(eq("sad"))).thenReturn("23");
        when(vClient.countByTags(eq("none"))).thenReturn("0");
        when(tService.getVerseTags()).thenReturn(allTags);
        
        VersesService service = new VersesService(vClient, tService);
        ChartVerseTagCount result = service.allTagCounts();
        
        assertThat(result.getLabels()).hasSize(3)
                .contains(happyTag.getLabel())
                .contains(sadTag.getLabel())
                .contains(noneTag.getLabel());
        assertThat(result.getSeries()).hasSize(3)
                .contains("60")
                .contains("23")
                .contains("0");
    }
}
