package org.codebeneath.lyrics.home;

import io.github.benas.randombeans.api.EnhancedRandom;
import java.util.Collections;
import java.util.List;
import org.codebeneath.lyrics.authn.LoggingAccessDeniedHandler;
import org.codebeneath.lyrics.impacted.Impacted;
import org.codebeneath.lyrics.tag.Tag;
import org.codebeneath.lyrics.tag.TagsClient;
import org.codebeneath.lyrics.verse.Verse;
import org.codebeneath.lyrics.verse.VerseRepository;
import static org.hamcrest.Matchers.containsString;
import org.junit.Before;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@Import(org.codebeneath.lyrics.authn.LoggingAccessDeniedHandler.class)
@WebMvcTest(GlobalController.class)
public class GlobalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VerseRepository vrepo;

    @MockBean
    private TagsClient tagsClient;

    @MockBean
    private AuthenticationSuccessHandler successHandler;
    
    @Autowired
    private LoggingAccessDeniedHandler deniedHandler;

    Impacted testUser;
    Verse randomVerse;
    List<Verse> newUserVerses = Collections.emptyList();
    List<Tag> tags = Collections.emptyList();
    
    @Before
    public void setUp() {
        testUser = EnhancedRandom.random(Impacted.class);
        randomVerse = EnhancedRandom.random(Verse.class);
    }

    @Test
    public void globalPageRendersForPublicView() throws Exception {
        this.mockMvc.perform(get("/global"))
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("impacted/global"));        
    }
    
    @WithMockUser(username = "test", roles = {"USER"})
    @Test
    public void globalPageDisplaysUserNameForAuthnView() throws Exception {
        when(vrepo.findByImpactedId(testUser.getId())).thenReturn(newUserVerses);

        this.mockMvc.perform(get("/global"))
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("impacted/global"))
                .andExpect(content().string(containsString(testUser.getDisplayName())));
    }

    @Test
    public void globalPageDisplaysAdditionalVersesOnScroll() throws Exception {
        this.mockMvc.perform(get("/global")
                .param("p", "2")
        )
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("impacted/globalVersesScroll"));
    }

}
