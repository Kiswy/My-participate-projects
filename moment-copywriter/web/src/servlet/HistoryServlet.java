package servlet;

import dao.CopywritingRecordDao;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/copywriting/history")
public class HistoryServlet extends BaseApiServlet {
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        handle(request, response, new HashMap<>());
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        handle(request, response, readBody(request));
    }

    private void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            Map<String, Object> body
    ) throws IOException {
        int userId = currentUserId(request, body);
        if (userId <= 0) {
            writeFail(response, HttpServletResponse.SC_BAD_REQUEST,
                    "userId is required");
            return;
        }

        writeSuccess(response, new CopywritingRecordDao().listByUserId(userId));
    }
}
