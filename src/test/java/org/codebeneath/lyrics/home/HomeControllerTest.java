package org.codebeneath.lyrics.home;

import io.github.benas.randombeans.api.EnhancedRandom;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.codebeneath.lyrics.authn.LoggingAccessDeniedHandler;
import org.codebeneath.lyrics.impacted.Impacted;
import org.codebeneath.lyrics.impacted.ImpactedRepository;
import org.codebeneath.lyrics.tag.Tag;
import org.codebeneath.lyrics.tag.TagRepository;
import org.codebeneath.lyrics.verse.Verse;
import org.codebeneath.lyrics.verse.VerseRepository;
import static org.hamcrest.Matchers.containsString;
import org.junit.Before;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@Import(org.codebeneath.lyrics.authn.LoggingAccessDeniedHandler.class)
@WebMvcTest(HomeController.class)
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImpactedRepository irepo;

    @MockBean
    private VerseRepository vrepo;

    @MockBean
    private TagRepository trepo;

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

    @WithMockUser(username = "test", roles = {"USER"})
    @Test
    public void userCanSeeOwnVerses() throws Exception {
        when(irepo.findByUserName(anyString())).thenReturn(Optional.of(testUser));
        when(vrepo.findByImpactedId(testUser.getId())).thenReturn(newUserVerses);
        when(vrepo.getRandomVerse()).thenReturn(randomVerse);
        
        this.mockMvc.perform(get("/my"))
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("impacted/my"))
                .andExpect(content().string(containsString(testUser.getFirstName())));
    }

    @WithMockUser(username = "test", roles = {"USER"})
    @Test
    public void userCannotSeeOtherUserVerses() throws Exception {
        this.mockMvc.perform(get("/my/999"))
                // .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/access-denied"));
    }

    @WithMockUser(username = "testAdmin", roles = {"ADMIN"})
    @Test
    public void adminCanSeeOtherUserVerses() throws Exception {
        when(irepo.findById(999L)).thenReturn(Optional.of(testUser));
        when(vrepo.findByImpactedId(999L)).thenReturn(newUserVerses);
        when(vrepo.getRandomVerse()).thenReturn(randomVerse);

        this.mockMvc.perform(get("/my/999"))
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("impacted/my"))
                .andExpect(content().string(containsString("How were")));
    }

    @WithMockUser(username = "test", roles = {"USER"})
    @Test
    public void userCanSeeGlobalVerses() throws Exception {
        when(irepo.findByUserName(anyString())).thenReturn(Optional.of(testUser));
        when(vrepo.findByImpactedId(testUser.getId())).thenReturn(newUserVerses);

        this.mockMvc.perform(get("/global"))
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("impacted/globalTag"))
                .andExpect(content().string(containsString(testUser.getFirstName())));
    }

}
