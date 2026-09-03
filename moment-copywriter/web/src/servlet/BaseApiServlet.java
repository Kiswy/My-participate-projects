package servlet;

import entity.User;
import util.CorsUtil;
import util.JsonUtil;

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
        CorsUtil.allowCors(response);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    protected Map<String, Object> readBody(HttpServletRequest request)
            throws IOException {
        return JsonUtil.readJsonObject(request);
    }

    protected void writeSuccess(HttpServletResponse response, Object data)
            throws IOException {
        CorsUtil.allowCors(response);
        JsonUtil.writeJson(response, JsonUtil.success(data));
    }

    protected void writeFail(HttpServletResponse response, int status, String message)
            throws IOException {
        CorsUtil.allowCors(response);
        response.setStatus(status);
        JsonUtil.writeJson(response, JsonUtil.fail(message));
    }

    protected int currentUserId(HttpServletRequest request, Map<String, Object> body) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object loginUser = session.getAttribute("loginUser");
            if (loginUser instanceof User) {
                return ((User) loginUser).getId();
            }
        }

        return JsonUtil.getInt(request, body, "userId", 0);
    }
}
