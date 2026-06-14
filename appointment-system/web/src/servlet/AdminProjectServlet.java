package servlet;

import com.google.gson.Gson;
import dao.ReservationDao;
import dao.ProjectDao;
import entity.Project;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/projects")
public class AdminProjectServlet
        extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        ProjectDao dao = new ProjectDao();
        List<Project> list = dao.getAllProjects();
        Gson gson = new Gson();
        String json = gson.toJson(list);

        response.getWriter()
                .print(json);
    }

    @Override
    protected void doDelete(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain;charset=UTF-8");

        String projectIdText = request.getParameter("projectId");

        try {
            Integer projectId = Integer.parseInt(projectIdText);
            ReservationDao reservationDao = new ReservationDao();

            boolean hasReservation =
                    reservationDao
                            .existsActiveReservation(
                                    projectId
                            );

            if (hasReservation) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("当前项目存在有效预约，无法删除");

                return;
            }

            ProjectDao projectDao = new ProjectDao();

            boolean success =
                    projectDao.deleteProject(
                            projectId
                    );

            if (!success) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("项目不存在");

                return;
            }

            response.getWriter().write("删除成功");

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("项目ID格式错误");
        }
    }
}