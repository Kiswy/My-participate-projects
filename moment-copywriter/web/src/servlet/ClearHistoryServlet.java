package servlet;

import dao.CopywritingRecordDao;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/copywriting/clear-history")
public class ClearHistoryServlet extends BaseApiServlet {
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        handle(request, response);
    }

    @Override
    protected void doDelete(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        handle(request, response);
    }

    private void handle(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        int userId = requireUserId(request, response);
        if (userId <= 0) {
            return;
        }

        int deletedCount = new CopywritingRecordDao().deleteAllByUserId(userId);

        Map<String, Object> data = new HashMap<>();
        data.put("deletedCount", deletedCount);
        writeSuccess(response, data);
    }
}
