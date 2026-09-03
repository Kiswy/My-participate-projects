package servlet;

import dao.CopywritingRecordDao;
import util.JsonUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/api/copywriting/delete")
public class DeleteHistoryServlet extends BaseApiServlet {
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
        Map<String, Object> body = readBody(request);
        int id = JsonUtil.getInt(request, body, "id", 0);
        int userId = currentUserId(request, body);

        if (id <= 0 || userId <= 0) {
            writeFail(response, HttpServletResponse.SC_BAD_REQUEST,
                    "id and userId are required");
            return;
        }

        boolean deleted = new CopywritingRecordDao().deleteById(id, userId);
        if (!deleted) {
            writeFail(response, HttpServletResponse.SC_NOT_FOUND,
                    "Record not found");
            return;
        }

        writeSuccess(response, null);
    }
}
