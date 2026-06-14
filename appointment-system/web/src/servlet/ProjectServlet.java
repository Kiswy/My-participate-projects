package servlet;

import com.google.gson.Gson;
import dao.ProjectDao;
import entity.Project;
import entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;
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

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain;charset=UTF-8");

        HttpSession session = request.getSession(false);
        User loginUser = session == null
                ? null
                : (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("请先登录");
            return;
        }

        if (!"管理员".equals(loginUser.getRole())) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("没有新增项目的权限");
            return;
        }

        String categoryIdText = request.getParameter("categoryId");
        String projectName = request.getParameter("projectName");
        String description = request.getParameter("description");
        String location = request.getParameter("location");
        String appointmentTime = request.getParameter("appointmentTime");
        String capacityText = request.getParameter("capacity");

        if (projectName == null || projectName.isBlank()
                || description == null || description.isBlank()
                || location == null || location.isBlank()
                || appointmentTime == null || appointmentTime.isBlank()) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("请完整填写项目信息");
            return;
        }

        try {
            int categoryId = Integer.parseInt(categoryIdText);
            int capacity = Integer.parseInt(capacityText);

            if (categoryId <= 0 || capacity <= 0) {
                throw new NumberFormatException();
            }

            Project project = new Project();
            project.setCategoryId(categoryId);
            project.setProjectName(projectName.trim());
            project.setDescription(description.trim());
            project.setLocation(location.trim());
            project.setAppointmentTime(appointmentTime);
            project.setCapacity(capacity);

            ProjectDao projectDao = new ProjectDao();
            boolean success = projectDao.addProject(project);

            if (!success) {
                response.setStatus(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR
                );
                response.getWriter().write("新增项目失败");
                return;
            }

            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write("新增项目成功");

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("分类或容量格式不正确");
        }
    }
}