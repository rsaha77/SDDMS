package project.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LogoutController {
	
	@RequestMapping(value = "/logout") 
	public void logout(HttpServletRequest request,  HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(false);
		if(session != null) {
			session.invalidate();
		}
		response.sendRedirect("");
	}
	
}
