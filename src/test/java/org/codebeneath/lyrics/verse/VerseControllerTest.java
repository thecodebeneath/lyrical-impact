package org.codebeneath.lyrics.verse;

import io.github.benas.randombeans.api.EnhancedRandom;
import java.util.Optional;
import org.codebeneath.lyrics.authn.LoggingAccessDeniedHandler;
import org.codebeneath.lyrics.impacted.Impacted;
import org.codebeneath.lyrics.impacted.ImpactedRepository;
import org.codebeneath.lyrics.tag.TagRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@RunWith(SpringRunner.class)
@Import(org.codebeneath.lyrics.authn.LoggingAccessDeniedHandler.class)
@WebMvcTest(VerseController.class)
public class VerseControllerTest {

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

    @Before
    public void setUp() {
        testUser = EnhancedRandom.random(Impacted.class);
        randomVerse = EnhancedRandom.random(Verse.class);
    }

    @WithMockUser(username = "test", roles = {"USER"})
    @Test
    public void userSeesEmptyNewVerseForm() throws Exception {
        when(irepo.findByUserName(anyString())).thenReturn(Optional.of(testUser));

        this.mockMvc.perform(get("/verse"))
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("impacted/verseForm"))
                .andExpect(model().attribute("verse", hasProperty("id", nullValue())))
                .andExpect(model().attribute("verse", hasProperty("impacted", nullValue())))
                .andExpect(model().attribute("verse", hasProperty("text", is(emptyOrNullString()))));
    }

    @WithMockUser(username = "test", roles = {"USER"})
    @Test
    public void userSeesFormFilledWithAnonymizedRandomVerse() throws Exception {
        when(irepo.findByUserName(anyString())).thenReturn(Optional.of(testUser));
        when(vrepo.findById(5L)).thenReturn(Optional.of(randomVerse));

        this.mockMvc.perform(get("/verse").param("randomVerseId", "5"))
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("impacted/verseForm"))
                .andExpect(model().attribute("verse", hasProperty("id", nullValue())))
                .andExpect(model().attribute("verse", hasProperty("impacted", nullValue())))
                .andExpect(model().attribute("verse", hasProperty("text", is(randomVerse.getText()))));
    }

    @WithMockUser(username = "test", roles = {"USER"})
    @Test
    public void userSeesFormFilledWithAnonymizedGlobalVerse() throws Exception {
        when(irepo.findByUserName(anyString())).thenReturn(Optional.of(testUser));
        when(vrepo.findById(5L)).thenReturn(Optional.of(randomVerse));

        this.mockMvc.perform(get("/verse/global/5"))
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("impacted/verseForm"))
                .andExpect(model().attribute("verse", hasProperty("id", nullValue())))
                .andExpect(model().attribute("verse", hasProperty("impacted", nullValue())))
                .andExpect(model().attribute("verse", hasProperty("text", is(randomVerse.getText()))));
    }

    @WithMockUser(username = "test", roles = {"USER"})
    @Test
    public void userSeesFormFilledWithExistingVerse() throws Exception {
        when(irepo.findByUserName(anyString())).thenReturn(Optional.of(testUser));
        when(vrepo.findByIdAndImpactedId(4L, testUser.getId())).thenReturn(Optional.of(randomVerse));

        this.mockMvc.perform(get("/verse/4"))
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("impacted/verseForm"))
                .andExpect(model().attribute("verse", hasProperty("id", is(randomVerse.getId()))))
                .andExpect(model().attribute("verse", hasProperty("impacted", is(randomVerse.getImpacted()))))
                .andExpect(model().attribute("verse", hasProperty("text", is(randomVerse.getText()))));
    }

    @WithMockUser(username = "test", roles = {"USER"})
    @Test
    public void userRedirectedForVerseNotOwnedOrFound() throws Exception {
        when(irepo.findByUserName(anyString())).thenReturn(Optional.of(testUser));
        when(vrepo.findByIdAndImpactedId(999L, testUser.getId())).thenReturn(Optional.empty());

        this.mockMvc.perform(get("/verse/999"))
                // .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/my"));
    }

    // POST

    @WithMockUser(username = "test", roles = {"USER"})
    @Test
    public void userSeesErrorOnSubmitWhenTextEmpty() throws Exception {
        when(irepo.findByUserName(anyString())).thenReturn(Optional.of(testUser));

        this.mockMvc.perform(post("/verse")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        )
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrors("verse", "text"))
                .andExpect(model().attributeHasFieldErrorCode("verse", "text", "NotNull"))
                .andExpect(view().name("impacted/verseForm"));
    }
    
    @WithMockUser(username = "test", roles = {"USER"})
    @Test
    public void userSeesErrorOnSubmitWhenTextTooLong() throws Exception {
        when(irepo.findByUserName(anyString())).thenReturn(Optional.of(testUser));

        this.mockMvc.perform(post("/verse")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("text", "")
        )
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrors("verse", "text"))
                .andExpect(model().attributeHasFieldErrorCode("verse", "text", "Size"))
                .andExpect(view().name("impacted/verseForm"));        
    }
    
}
