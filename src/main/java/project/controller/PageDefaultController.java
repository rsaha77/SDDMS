package project.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageDefaultController {

	// for 403 access denied page
	@RequestMapping(value = "/403")
	public String accesssDenied(Principal user) {
		return "error/unauthorized";
	}
}
