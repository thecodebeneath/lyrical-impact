package org.codebeneath.lyrics.home;

import org.codebeneath.lyrics.StandaloneMvcTestViewResolver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Controller test (no security + no view rendering)
 */
@WebMvcTest(SecurityController.class)
public class NoAuthSecurityControllerTest {

    @Autowired
    private static MockMvc mockMvc;
    
    @BeforeAll
    public static void setUpBeforeAll() {
        final SecurityController controller = new SecurityController();
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                    .setViewResolvers(new StandaloneMvcTestViewResolver())
                    .build();
    }

    @Test
    public void aboutPageResolvesForPublicView() throws Exception {
        this.mockMvc.perform(get("/about"))
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("about"));
    }

    @Test
    public void privacyPageResolvesForPublicView() throws Exception {
        this.mockMvc.perform(get("/privacy"))
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("about"));
    }

}
