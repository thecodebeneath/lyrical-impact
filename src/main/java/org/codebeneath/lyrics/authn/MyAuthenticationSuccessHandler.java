package org.codebeneath.lyrics.authn;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.codebeneath.lyrics.impacted.Impacted;
import org.codebeneath.lyrics.impacted.ImpactedNotFoundException;
import org.codebeneath.lyrics.impacted.ImpactedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component("successHandler")
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

     @Autowired
     ImpactedRepository impactedRepo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Impacted impactedUser = getImpactedUser(authentication);
//            impactedUser.updateLastLogin(new Date());
        }
    }

    private Impacted getImpactedUser(Authentication authentication) {
        Optional<Impacted> impacted = impactedRepo.findByUserName(authentication.getName());
        if (!impacted.isPresent()) {
            throw new ImpactedNotFoundException(authentication.getName());
        }
        return impacted.get();
    }
}
