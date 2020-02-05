package org.codebeneath.lyrics.authn;

import java.util.Optional;
import org.codebeneath.lyrics.impacted.Impacted;
import org.codebeneath.lyrics.impacted.ImpactedRepository;
import static org.hamcrest.Matchers.containsString;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 *
 * @author black
 */
@RunWith(SpringRunner.class)
@WebMvcTest(SecurityController.class)
public class SecurityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImpactedRepository irepo;

    @MockBean
    private AuthenticationSuccessHandler successHandler;

    @MockBean
    private LoggingAccessDeniedHandler deniedHandler;
    
    @Test
    public void aboutPageRendersForPublicView() throws Exception {
        this.mockMvc.perform(get("/about"))
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("about"))
                .andExpect(content().string(containsString("About")));
    }

    @WithMockUser(username = "test", roles = {"USER"})
    @Test
    public void aboutPageDisplaysUserNameForAuthnView() throws Exception {
        Impacted testUser = new Impacted("", "", "firstName", "");
        when(irepo.findByUserName(anyString())).thenReturn(Optional.of(testUser));

        this.mockMvc.perform(get("/about"))
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("about"))
                .andExpect(content().string(containsString("About")))
                .andExpect(content().string(containsString(testUser.getFirstName())));
    }

}
