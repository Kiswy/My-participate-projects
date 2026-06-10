package filter;

import entity.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String uri = req.getRequestURI();

        if (uri.contains("index.jsp")
                || uri.contains("register.jsp")
                || uri.contains("login")
                || uri.contains("register")
                || uri.contains("css")
                || uri.contains("images")) {

            chain.doFilter(request, response);

            return;
        }

        HttpSession session = req.getSession(false);
        User loginUser = null;

        if (session != null) {
            loginUser =
                    (User) session.getAttribute(
                            "loginUser"
                    );
        }

        if (loginUser == null) {
            resp.sendRedirect("index.jsp");

            return;
        }

        chain.doFilter(request, response);
    }
}