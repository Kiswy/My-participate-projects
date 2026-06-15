package servlet;

import com.google.gson.Gson;
import dao.UserDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/register")
public class AndroidRegisterServlet
        extends HttpServlet {
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");
        UserDao userDao = new UserDao();

        boolean success =
                userDao.register(
                        username,
                        password,
                        phone
                );

        Map<String,Object> result = new HashMap<>();

        if(success){
            result.put("success", true);
            result.put("message", "注册成功");
        }else{
            result.put("success", false);
            result.put("message", "注册失败");
        }

        Gson gson = new Gson();

        response.getWriter().write(
                gson.toJson(result)
        );
    }
}