package servlet;

import dao.CopywritingRecordDao;
import entity.CopywritingRecord;
import util.AiClient;
import util.JsonUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/copywriting/generate")
public class GenerateCopywritingServlet extends BaseApiServlet {
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        Map<String, Object> body = readBody(request);
        String scene = JsonUtil.getString(request, body, "scene");
        String mood = JsonUtil.getString(request, body, "mood");
        String style = JsonUtil.getString(request, body, "style");
        String keywords = JsonUtil.getString(request, body, "keywords");

        if (scene == null) {
            writeFail(response, HttpServletResponse.SC_BAD_REQUEST,
                    "scene is required");
            return;
        }

        AiClient aiClient = new AiClient();
        String content;

        try {
            content = aiClient.generateMomentCopywriting(scene, mood, style, keywords);
        } catch (IllegalStateException e) {
            writeFail(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    e.getMessage());
            return;
        } catch (Exception e) {
            e.printStackTrace();
            writeFail(response, HttpServletResponse.SC_BAD_GATEWAY,
                    "AI service request failed");
            return;
        }

        CopywritingRecord record = new CopywritingRecord();
        record.setUserId(currentUserId(request, body));
        record.setScene(scene);
        record.setMood(mood);
        record.setStyle(style);
        record.setKeywords(keywords);
        record.setGeneratedContent(content);
        record.setAiModel(aiClient.getModel());

        int recordId = new CopywritingRecordDao().add(record);

        Map<String, Object> data = new HashMap<>();
        data.put("content", content);
        data.put("recordId", recordId);
        data.put("saved", recordId > 0);
        data.put("model", aiClient.getModel());

        writeSuccess(response, data);
    }
}
