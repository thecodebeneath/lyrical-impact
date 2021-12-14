package org.codebeneath.lyrics.home;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.codebeneath.lyrics.authn.LoggingAccessDeniedHandler;
import org.codebeneath.lyrics.impactedapi.ImpactedUser;
import org.codebeneath.lyrics.tagsapi.VerseTagsService;
import org.codebeneath.lyrics.versesapi.ImpactedVerse;
import org.codebeneath.lyrics.versesapi.VersesService;
import static org.hamcrest.Matchers.*;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Controller test (full context w/ security + view rendering)
 */
@WebMvcTest(GlobalController.class)
public class GlobalControllerTest {

    List<ImpactedVerse> emptyImpactedVerses = Collections.emptyList();
    List<ImpactedVerse> globalVerses = new ArrayList<>(1);
    ImpactedUser testUser;
    ImpactedVerse randomVerse;
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VersesService versesService;
    
    @MockBean
    private VerseTagsService tagsService;

    @MockBean
    private AuthenticationSuccessHandler successHandler;

    @MockBean
    private AuthenticationFailureHandler failureHandler;
    
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
    
    @BeforeEach
    public void setUp() {
        testUser = new ImpactedUser("testname", "okta", "testdisplayname");
        testUser.setId(-1L);
        EasyRandom randomGen = new EasyRandom();
        randomVerse = randomGen.nextObject(ImpactedVerse.class);
        globalVerses.add(randomVerse);
    }

    @Test
    public void globalPageRendersForGuest() throws Exception {
        when(versesService.global(eq(0), isNull(), isNull())).thenReturn(globalVerses);
        this.mockMvc.perform(get("/global"))
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("verses"))
                .andExpect(model().attribute("verses", hasSize(1)))
                .andExpect(view().name("impacted/global"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString(randomVerse.getTitle())));
    }

    @Test
    public void globalPageScrollableForGuest() throws Exception {
        when(versesService.global(eq(2), isNull(), isNull())).thenReturn(globalVerses);
        this.mockMvc.perform(get("/global")
                .param("p", "2")
        )
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("verses"))
                .andExpect(model().attribute("verses", hasSize(1)))
                .andExpect(view().name("impacted/globalVersesScroll"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString(randomVerse.getTitle())));
    }
    
    @Test
    public void globalPageRendersDisplayNameForAuthn() throws Exception {
        when(versesService.global(eq(0), isNull(), isNull())).thenReturn(globalVerses);
        this.mockMvc.perform(get("/global")
                .with(oidcLogin().oidcUser(testUser))
        )
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("impacted/global"))
                .andExpect(content().string(containsString(testUser.getDisplayName())));
    }

    @Test
    public void globalPageRendersForAuthn() throws Exception {
        when(versesService.global(eq(0), isNull(), isNull())).thenReturn(globalVerses);
        this.mockMvc.perform(get("/global")
                .with(oidcLogin().oidcUser(testUser))
        )
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("verses"))
                .andExpect(model().attribute("verses", hasSize(1)))
                .andExpect(view().name("impacted/global"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString(randomVerse.getTitle())));
    }

    @Test
    public void globalPageFilterableForAuthn() throws Exception {
        when(versesService.global(eq(0), eq("happytag"), isNull())).thenReturn(globalVerses);
        this.mockMvc.perform(get("/global")
                .with(oidcLogin().oidcUser(testUser))
                .param("tag", "happytag")
        )
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("verses"))
                .andExpect(model().attribute("verses", hasSize(1)))
                .andExpect(view().name("impacted/global"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString(randomVerse.getTitle())));
    }

    @Test
    public void globalPageSearchableForAuthn() throws Exception {
        when(versesService.global(eq(0), isNull(), eq("search words"))).thenReturn(globalVerses);
        this.mockMvc.perform(get("/global")
                .with(oidcLogin().oidcUser(testUser))
                .param("q", "search words")
        )
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("verses"))
                .andExpect(model().attribute("verses", hasSize(1)))
                .andExpect(view().name("impacted/global"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString(randomVerse.getTitle())));
    }

    @Test
    public void globalPageFilterableAndSearchableForAuthn() throws Exception {
        when(versesService.global(eq(0), eq("happytag"), eq("search words"))).thenReturn(globalVerses);
        this.mockMvc.perform(get("/global")
                .with(oidcLogin().oidcUser(testUser))
                .param("tag", "happytag")
                .param("q", "search words")
        )
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("verses"))
                .andExpect(model().attribute("verses", hasSize(1)))
                .andExpect(view().name("impacted/global"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString(randomVerse.getTitle())));
    }

    @Test
    public void globalPageScrollableForAuthn() throws Exception {
        when(versesService.global(eq(2), isNull(), isNull())).thenReturn(globalVerses);
        this.mockMvc.perform(get("/global")
                .with(oidcLogin().oidcUser(testUser))
                .param("p", "2")
        )
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("verses"))
                .andExpect(model().attribute("verses", hasSize(1)))
                .andExpect(view().name("impacted/globalVersesScroll"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString(randomVerse.getTitle())));
    }

    @Test
    public void globalPageFilterableAndScrollableForAuthn() throws Exception {
        when(versesService.global(eq(2), eq("happytag"), isNull())).thenReturn(globalVerses);
        this.mockMvc.perform(get("/global")
                .with(oidcLogin().oidcUser(testUser))
                .param("p", "2")
                .param("tag", "happytag")
        )
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("verses"))
                .andExpect(model().attribute("verses", hasSize(1)))
                .andExpect(view().name("impacted/globalVersesScroll"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString(randomVerse.getTitle())));
    }

    @Test
    public void globalPageSearchableAndScrollableForAuthn() throws Exception {
        when(versesService.global(eq(2), isNull(), eq("search words"))).thenReturn(globalVerses);
        this.mockMvc.perform(get("/global")
                .with(oidcLogin().oidcUser(testUser))
                .param("p", "2")
                .param("q", "search words")
        )
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("verses"))
                .andExpect(model().attribute("verses", hasSize(1)))
                .andExpect(view().name("impacted/globalVersesScroll"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString(randomVerse.getTitle())));
    }

}
