package project.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Controller;

@Controller
public class AuthSuccessHandlerController extends SavedRequestAwareAuthenticationSuccessHandler {
	
	@Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
        // Get the role of logged in user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().toString();

        String targetUrl = "";
        if(role.contains("MANAGER")) {
            targetUrl = "/indexM";
        } else if(role.contains("SALESMAN")) {
            targetUrl = "/indexS";
        }
        
        return targetUrl;
    }
}