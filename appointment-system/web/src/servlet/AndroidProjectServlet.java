package servlet;

import com.google.gson.Gson;
import dao.ProjectDao;
import entity.Project;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/projects")
public class AndroidProjectServlet
        extends HttpServlet {
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");

        ProjectDao projectDao = new ProjectDao();
        List<Project> list = projectDao.getAllProjects();
        Gson gson = new Gson();

        response.getWriter().write(
                gson.toJson(list)
        );
    }
}