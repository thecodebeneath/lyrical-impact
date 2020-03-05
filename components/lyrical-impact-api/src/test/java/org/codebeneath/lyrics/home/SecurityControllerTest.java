package org.codebeneath.lyrics.home;

import org.codebeneath.lyrics.StandaloneMvcTestViewResolver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Controller test (no security + no view rendering)
 */
@RunWith(SpringRunner.class)
@WebMvcTest(SecurityController.class)
public class SecurityControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Before
    public void setUp() {
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
