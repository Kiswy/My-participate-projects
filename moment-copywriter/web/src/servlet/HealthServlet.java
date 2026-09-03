package servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/health")
public class HealthServlet extends BaseApiServlet {
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "running");
        data.put("service", "moment-copywriter-backend");
        writeSuccess(response, data);
    }
}
