package servlet;

import entity.User;
import util.CorsUtil;
import util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

public abstract class BaseApiServlet extends HttpServlet {
    @Override
    protected void doOptions(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        CorsUtil.allowCors(request, response);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    protected Map<String, Object> readBody(HttpServletRequest request)
            throws IOException {
        return JsonUtil.readJsonObject(request);
    }

    protected void writeSuccess(HttpServletResponse response, Object data)
            throws IOException {
        JsonUtil.writeJson(response, JsonUtil.success(data));
    }

    protected void writeFail(HttpServletResponse response, int status, String message)
            throws IOException {
        response.setStatus(status);
        JsonUtil.writeJson(response, JsonUtil.fail(message));
    }

    @Override
    protected void service(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        CorsUtil.allowCors(request, response);
        super.service(request, response);
    }

    protected User currentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object loginUser = session.getAttribute("loginUser");
            if (loginUser instanceof User) {
                return (User) loginUser;
            }
        }

        return null;
    }

    protected int currentUserId(HttpServletRequest request) {
        User user = currentUser(request);
        return user == null ? 0 : user.getId();
    }

    protected int requireUserId(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        int userId = currentUserId(request);
        if (userId <= 0) {
            writeFail(response, HttpServletResponse.SC_UNAUTHORIZED, "请先登录");
        }

        return userId;
    }
}
