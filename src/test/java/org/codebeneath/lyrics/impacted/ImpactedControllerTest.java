package org.codebeneath.lyrics.impacted;

import io.github.benas.randombeans.api.EnhancedRandom;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.codebeneath.lyrics.authn.LoggingAccessDeniedHandler;
import org.codebeneath.lyrics.tag.Tag;
import org.codebeneath.lyrics.tag.TagRepository;
import org.codebeneath.lyrics.verse.Verse;
import org.codebeneath.lyrics.verse.VerseRepository;
import static org.hamcrest.CoreMatchers.any;
//import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.containsString;
import org.junit.Before;
import org.junit.Ignore;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(ImpactedController.class)
public class ImpactedControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImpactedRepository irepo;

    @MockBean
    private VerseRepository vrepo;

    @MockBean
    private TagRepository trepo;

    @MockBean
    private LoggingAccessDeniedHandler deniedHandler;
            
    Impacted newUser;
    Verse randomVerse;
    List<Verse> newUserVerses = Collections.emptyList();
    List<Tag> newUserTags = Collections.emptyList();
    
    @Before
    public void setUp() {
        newUser = EnhancedRandom.random(Impacted.class);
        newUser.setId(2L);
        randomVerse = EnhancedRandom.random(Verse.class);
    }
    
    @Test
    public void aboutPageMatched() throws Exception {
        this.mockMvc.perform(get("/about"))
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("About")));
    }

    @WithMockUser(value = "alan")
    @Test
    public void newUserReturnsNoVersesOrTags() throws Exception {
        when(irepo.findById(newUser.getId())).thenReturn(Optional.of(newUser));
        when(vrepo.findByImpactedId(newUser.getId())).thenReturn(newUserVerses);
        when(vrepo.getRandomVerse()).thenReturn(randomVerse);
        when(trepo.findByImpacted(newUser)).thenReturn(newUserTags);
                
        this.mockMvc.perform(get("/impacted", newUser.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString(newUser.getFirstName())));
    }

}
