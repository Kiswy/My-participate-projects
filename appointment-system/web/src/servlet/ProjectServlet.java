package servlet;

import com.google.gson.Gson;
import dao.ProjectDao;
import entity.Project;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/projects")
public class ProjectServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType(
                "application/json;charset=UTF-8"
        );

        String categoryIdStr =
                request.getParameter("categoryId");

        Integer categoryId = Integer.parseInt(categoryIdStr);
        ProjectDao projectDao = new ProjectDao();
        List<Project> list = projectDao.getProjectsByCategory(categoryId);
        Gson gson = new Gson();
        String json = gson.toJson(list);

        response.getWriter().print(
                json
        );
    }
}