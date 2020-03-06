package org.codebeneath.lyrics.home;

import io.github.benas.randombeans.api.EnhancedRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.codebeneath.lyrics.authn.LoggingAccessDeniedHandler;
import org.codebeneath.lyrics.impactedapi.ImpactedClient;
import org.codebeneath.lyrics.impactedapi.ImpactedUser;
import org.codebeneath.lyrics.tagsapi.VerseTag;
import org.codebeneath.lyrics.tagsapi.VerseTagsService;
import org.codebeneath.lyrics.versesapi.ImpactedVerse;
import org.codebeneath.lyrics.versesapi.VersesService;
import static org.hamcrest.Matchers.*;
import org.junit.Before;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
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
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@WebMvcTest(HomeController.class)
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImpactedClient iClient;

    @MockBean
    private VersesService vService;

    @MockBean
    private VerseTagsService tagsService;

    @MockBean
    private AuthenticationSuccessHandler successHandler;
    
    @MockBean
    private LoggingAccessDeniedHandler deniedHandler;

    @MockBean
    private OAuth2UserService<OidcUserRequest,OidcUser> customOidcUserService;
    
    ImpactedUser testUser;
    ImpactedVerse randomVerse;
    List<ImpactedVerse> emptyImpactedVerses = Collections.emptyList();
    List<ImpactedVerse> testUserImpactedVerses = new ArrayList<>(1);
    List<VerseTag> tags = Collections.emptyList();
    
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
        testUser = new ImpactedUser("testname", "okta", "testdisplayname");
        testUser.setId(-1L);
        randomVerse = EnhancedRandom.random(ImpactedVerse.class);
        testUserImpactedVerses.add(randomVerse);
    }

    @Test
    public void guestIsRedirectedToLogin() throws Exception {
        this.mockMvc.perform(get("/my"))
                // .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    public void userCanSeeOwnVerses() throws Exception {
        when(vService.findByImpactedId(eq(testUser.getId()), anyInt(), isNull(), isNull())).thenReturn(testUserImpactedVerses);
        when(vService.getRandomVerse()).thenReturn(randomVerse);
        
        this.mockMvc.perform(get("/my")
                .with(oidcLogin().oidcUser(testUser)))
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("verses"))
                .andExpect(model().attribute("verses", hasSize(1)))
                .andExpect(view().name("impacted/my"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString(testUser.getDisplayName())));
    }

    @Test
    public void userPageDisplaysAdditionalVersesOnScroll() throws Exception {
        when(vService.findByImpactedId(eq(testUser.getId()), eq(2), isNull(), isNull())).thenReturn(testUserImpactedVerses);
        when(vService.getRandomVerse()).thenReturn(randomVerse);

        this.mockMvc.perform(get("/my")
                .param("p", "2")
                .with(oidcLogin().oidcUser(testUser)))
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("verses"))
                .andExpect(model().attribute("verses", hasSize(1)))
                .andExpect(view().name("impacted/myVersesScroll"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }

//    @WithMockUser(username = "testAdmin", roles = {"USER"})
//    @Test
//    public void userCannotSeeOtherUserVerses() throws Exception {
//        this.mockMvc.perform(get("/my/999")
//                .with(oidcLogin().oidcUser(testUser)))
//                .andDo(print())
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/access-denied"));
//    }

//    @WithMockUser(username = "testAdmin", roles = {"ADMIN"})
//    @Test
//    public void adminCanSeeOtherUserVerses() throws Exception {
//        when(irepo.findById(999L)).thenReturn(Optional.of(testUser));
//        when(vrepo.findByImpactedId(999L)).thenReturn(newUserVerses);
//        when(vrepo.getRandomVerse()).thenReturn(randomVerse);
//
//        this.mockMvc.perform(get("/my/999")
//                .with(oidcLogin().oidcUser(testUser)))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(view().name("impacted/my"))
//                .andExpect(content().string(containsString("How were")));
//    }
//    

}
