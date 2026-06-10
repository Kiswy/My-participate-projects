package servlet;

import dao.UserDao;
import entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UserDao userDao = new UserDao();
        User user = userDao.login(
                username,
                password
        );

        if (user != null) {
            HttpSession session =
                    request.getSession();

            session.setAttribute(
                    "loginUser",
                    user
            );

            if ("管理员".equals(user.getRole())) {
                response.sendRedirect(
                        "homepage_Max.jsp"
                );
            } else {
                response.sendRedirect(
                        "homepage.jsp"
                );
            }
        } else {
            response.getWriter().println(
                    "用户名或密码错误"
            );
        }
    }
}