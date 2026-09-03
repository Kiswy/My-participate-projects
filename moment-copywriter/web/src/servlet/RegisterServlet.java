package servlet;

import dao.UserDao;
import util.JsonUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/register")
public class RegisterServlet extends BaseApiServlet {
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        Map<String, Object> body = readBody(request);
        String username = JsonUtil.getString(request, body, "username");
        String password = JsonUtil.getString(request, body, "password");
        String phone = JsonUtil.getString(request, body, "phone");

        if (username == null || password == null) {
            writeFail(response, HttpServletResponse.SC_BAD_REQUEST,
                    "username and password are required");
            return;
        }

        UserDao userDao = new UserDao();
        boolean success = userDao.register(username, password, phone);

        if (!success) {
            writeFail(response, HttpServletResponse.SC_BAD_REQUEST,
                    "Register failed. The username may already exist.");
            return;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        writeSuccess(response, data);
    }
}
