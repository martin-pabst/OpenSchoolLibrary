package de.sp.main.server;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.sp.database.model.User;

public class AuthenticationFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {

		long time = System.currentTimeMillis();

		// Before servlet
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		boolean allowedRequest = false;

		String pathInfo = request.getPathInfo();
		String servletPath = request.getServletPath();
		// System.out.println("pathInfo:" + pathInfo);
		if (pathInfo != null && pathInfo.startsWith("/public/")
				|| //
				servletPath != null
				&& (servletPath.equals("/login") || servletPath
						.equals("/install") || servletPath.equals("/progress"))) {
			allowedRequest = true;
		} else {

			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("user");

			if (user != null) {
				allowedRequest = true;
			}

		}

		if (allowedRequest) {
			// Invoke servlet
			chain.doFilter(request, response);

			time = System.currentTimeMillis() - time;
			Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
			logger.info("Request " + request.getRequestURI() + ": " + time
					+ " ms");

			// After servlet
		} else {
			// response.sendRedirect("/static/public/login.html");
			response.sendRedirect("/login");
		}

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
