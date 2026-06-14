package filter;

import entity.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(
        {"/homepage_Max.jsp",
        "/admin/*"})
public class AdminFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        User loginUser = null;

        if (session != null) {
            loginUser = (User) session.getAttribute(
                    "loginUser"
            );
        }

        if (loginUser == null) {
            resp.sendRedirect(
                    req.getContextPath() + "/index.jsp"
            );
            return;
        }

        if (!"管理员".equals(loginUser.getRole())) {
            session.invalidate();

            resp.sendRedirect(
                    req.getContextPath()
                            + "/index.jsp?accessDenied=true"
            );
            return;
        }

        chain.doFilter(request, response);
    }
}