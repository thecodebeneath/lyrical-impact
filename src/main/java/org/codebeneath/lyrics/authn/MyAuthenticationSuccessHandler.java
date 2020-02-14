package org.codebeneath.lyrics.authn;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.codebeneath.lyrics.impacted.Impacted;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component("successHandler")
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Impacted impactedUser = (Impacted)authentication.getPrincipal();
        // TODO: impactedUser.updateLastLogin(new Date());
        
        // what else needs to be implemented for OAuth contract? redirect to view...?
    }
}
