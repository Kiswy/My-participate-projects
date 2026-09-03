package servlet;

import dao.FavoriteDao;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/copywriting/favorites")
public class FavoritesServlet extends BaseApiServlet {
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
        int userId = requireUserId(request, response);
        if (userId <= 0) {
            return;
        }

        writeSuccess(response, new FavoriteDao().listByUserId(userId));
    }
}
