package project.interceptor;

import java.util.Locale;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import project.service.spec.UserService;

@Component
public class AccessControlInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private LocaleResolver localeResolver;

	@Autowired
	private UserService userService;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		Locale locale = Locale.ENGLISH;
		if (request.getUserPrincipal() != null) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (request.getSession().getAttribute("roles") == null) {
				request.getSession().setAttribute("roles", auth.getAuthorities().stream()
						.map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
			}
			if (request.getRequestURI().toString().endsWith("login")) {
				response.sendRedirect(request.getContextPath() + "/");
				return false;
			}
			String language = userService.getLanguage(request
					.getUserPrincipal().getName());
			if (language != null) {
				locale = new Locale(language);
			}
		}

		localeResolver.setLocale(request, response, locale);
		return true;
	}
}