package org.codebeneath.lyrics.home;

import io.github.benas.randombeans.api.EnhancedRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.codebeneath.lyrics.StandaloneMvcTestViewResolver;
import org.codebeneath.lyrics.tagsapi.VerseTag;
import org.codebeneath.lyrics.tagsapi.VerseTagsService;
import org.codebeneath.lyrics.versesapi.ImpactedVerse;
import org.codebeneath.lyrics.versesapi.VersesService;
import static org.hamcrest.Matchers.hasSize;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Controller test (no security + no view rendering)
 */
@RunWith(SpringRunner.class)
@WebMvcTest(GlobalController.class)
public class NoAuthGlobalControllerTest {

    List<ImpactedVerse> globalVerses = new ArrayList<>(1);
    ImpactedVerse randomVerse;
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private VersesService versesService;
    
    @MockBean
    private VerseTagsService tagsService;
    
    @Before
    public void setUp() {
        final GlobalController controller = new GlobalController(versesService, tagsService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                    .setViewResolvers(new StandaloneMvcTestViewResolver())
                    .build();
        
        randomVerse = EnhancedRandom.random(ImpactedVerse.class);
        globalVerses.add(randomVerse);
    }

    @Test
    public void globalPageRendersForGuest() throws Exception {
        when(versesService.global(eq(0), isNull(), isNull())).thenReturn(globalVerses);
        this.mockMvc.perform(get("/global"))
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("verses", hasSize(1)))
                .andExpect(view().name("impacted/global"));        
    }

    @Test
    public void globalPageDisplaysAdditionalVersesOnScrollForGuest() throws Exception {
        when(versesService.global(eq(2), isNull(), isNull())).thenReturn(globalVerses);
        this.mockMvc.perform(get("/global")
                .param("p", "2"))
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("verses", hasSize(1)))
                .andExpect(view().name("impacted/globalVersesScroll"));
    }

}
