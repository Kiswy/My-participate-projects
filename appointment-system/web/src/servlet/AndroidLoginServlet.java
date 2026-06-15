package servlet;

import com.google.gson.Gson;
import dao.UserDao;
import entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/login")
public class AndroidLoginServlet
        extends HttpServlet {
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UserDao userDao = new UserDao();
        User user =
                userDao.login(
                        username,
                        password
                );

        Map<String, Object> result = new HashMap<>();

        if (user != null) {
            HttpSession session = request.getSession();

            session.setAttribute("loginUser", user);

            result.put("success", true);
            result.put("message", "登录成功");

        } else {
            result.put("success", false);
            result.put("message", "用户名或密码错误");
        }

        Gson gson = new Gson();

        response.getWriter().write(
                gson.toJson(result)
        );
    }
}