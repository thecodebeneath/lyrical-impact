package org.codebeneath.lyrics.versesapi;

import io.github.benas.randombeans.api.EnhancedRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.codebeneath.lyrics.authn.LoggingAccessDeniedHandler;
import org.codebeneath.lyrics.impactedapi.ImpactedClient;
import org.codebeneath.lyrics.impactedapi.ImpactedUser;
import org.codebeneath.lyrics.tagsapi.VerseTagsService;
import org.codebeneath.lyrics.versesapi.ImpactedVerse;
import org.codebeneath.lyrics.versesapi.VersesService;
import static org.hamcrest.Matchers.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Controller test (full context w/ security + view rendering)
 */
@RunWith(SpringRunner.class)
@WebMvcTest(VersesController.class)
public class VerseControllerTest {

    ImpactedUser testUser;
    ImpactedVerse randomVerse;
    ImpactedVerse myImpactedVerse;
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VersesService versesService;

    @MockBean
    private VerseTagsService tagsService;

    @MockBean
    private AuthenticationSuccessHandler successHandler;
    
    @MockBean
    private LoggingAccessDeniedHandler deniedHandler;

    @MockBean
    private OAuth2UserService<OidcUserRequest,OidcUser> customOidcUserService;
        
    @MockBean
    ClientRegistrationRepository clientRegistrationRepository;

    @TestConfiguration
    static class AuthorizedClient {
        @Bean
        public OAuth2AuthorizedClientRepository authorizedClientRepository() {
            return new HttpSessionOAuth2AuthorizedClientRepository();
        }
    }
    
    @Before
    public void setUp() {
        testUser = EnhancedRandom.random(ImpactedUser.class);
        randomVerse = EnhancedRandom.random(ImpactedVerse.class);
        myImpactedVerse = EnhancedRandom.random(ImpactedVerse.class);
        myImpactedVerse.setImpactedId(testUser.getId());
    }

    @Test
    public void guestIsRedirectedToLogin() throws Exception {
        this.mockMvc.perform(get("/verse"))
                // .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }
    
    @Test
    public void userSeesEmptyNewVerseForm() throws Exception {
        this.mockMvc.perform(get("/verse")
                .with(oidcLogin().oidcUser(testUser))
        )
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("verse", hasProperty("id", nullValue())))
                .andExpect(model().attribute("verse", hasProperty("impactedId", nullValue())))
                .andExpect(model().attribute("verse", hasProperty("text", is(emptyOrNullString()))))
                .andExpect(view().name("impacted/verseForm"));

    }

    @Test
    public void userSeesFormFilledWithAnonymizedRandomVerse() throws Exception {
        when(versesService.findById(eq(5L))).thenReturn(Optional.of(randomVerse));

        this.mockMvc.perform(get("/verse")
                .with(oidcLogin().oidcUser(testUser))
                .param("randomVerseId", "5")
        )
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("verse", hasProperty("id", nullValue())))
                .andExpect(model().attribute("verse", hasProperty("impactedId", nullValue())))
                .andExpect(model().attribute("verse", hasProperty("text", is(randomVerse.getText()))))
                .andExpect(view().name("impacted/verseForm"))
                .andExpect(content().string(containsString(randomVerse.getText())));
    }

    @Test
    public void userSeesFormFilledWithAnonymizedGlobalVerse() throws Exception {
        when(versesService.findById(eq(5L))).thenReturn(Optional.of(randomVerse));

        this.mockMvc.perform(get("/verse/global/5")
                .with(oidcLogin().oidcUser(testUser))
        )
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("verse", hasProperty("id", nullValue())))
                .andExpect(model().attribute("verse", hasProperty("impactedId", nullValue())))
                .andExpect(model().attribute("verse", hasProperty("text", is(randomVerse.getText()))))
                .andExpect(view().name("impacted/verseForm"))
                .andExpect(content().string(containsString(randomVerse.getText())));
    }

    @Test
    public void userSeesFormFilledWithExistingVerse() throws Exception {
        when(versesService.findByIdAndImpactedId(4L, testUser.getId())).thenReturn(Optional.of(myImpactedVerse));

        this.mockMvc.perform(get("/verse/4")
                .with(oidcLogin().oidcUser(testUser))
        )
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("verse", hasProperty("id", is(myImpactedVerse.getId()))))
                .andExpect(model().attribute("verse", hasProperty("impactedId", is(testUser.getId()))))
                .andExpect(model().attribute("verse", hasProperty("text", is(myImpactedVerse.getText()))))
                .andExpect(model().attribute("verse", hasProperty("title", is(myImpactedVerse.getTitle()))))
                .andExpect(model().attribute("verse", hasProperty("author", is(myImpactedVerse.getAuthor()))))
                .andExpect(view().name("impacted/verseForm"))
                .andExpect(content().string(containsString(myImpactedVerse.getText())))
                .andExpect(content().string(containsString(myImpactedVerse.getTitle())))
                .andExpect(content().string(containsString(myImpactedVerse.getAuthor())));
    }

    @Test
    public void userRedirectedForVerseNotOwnedOrFound() throws Exception {
        when(versesService.findByIdAndImpactedId(999L, testUser.getId())).thenReturn(Optional.empty());

        this.mockMvc.perform(get("/verse/999")
                .with(oidcLogin().oidcUser(testUser))
        )
                // .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/my"));
    }

    // POST

    @Test
    public void userSeesErrorOnSubmitWhenTextEmpty() throws Exception {
        this.mockMvc.perform(post("/verse")
                .with(oidcLogin().oidcUser(testUser))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        )
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrors("verse", "text"))
                .andExpect(model().attributeHasFieldErrorCode("verse", "text", "NotNull"))
                .andExpect(view().name("impacted/verseForm"));
    }
    
    @Test
    public void userSeesErrorOnSubmitWhenTextTooShort() throws Exception {
        this.mockMvc.perform(post("/verse")
                .with(oidcLogin().oidcUser(testUser))
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

    @Test
    public void userSeesMyPageWhenSaveIsSuccessful() throws Exception {
        this.mockMvc.perform(post("/verse")
                .with(oidcLogin().oidcUser(testUser))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("text", myImpactedVerse.getText())
                .param("title", myImpactedVerse.getTitle())
                .param("author", myImpactedVerse.getAuthor())
        )
                // .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/my"))
                .andExpect(model().errorCount(0));
    }
    
}
