package servlet;

import dao.ProjectDao;
import dao.ReservationDao;
import entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/reservation")
public class ReservationServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        response.setContentType("text/plain;charset=UTF-8");

        HttpSession session = request.getSession();
        User loginUser =
                (User) session.getAttribute(
                        "loginUser"
                );

        if (loginUser == null) {
            response.getWriter().write(
                    "请先登录"
            );

            return;
        }

        Integer userId = loginUser.getId();
        Integer projectId =
                Integer.parseInt(
                        request.getParameter(
                                "projectId"
                        )
                );

        ReservationDao reservationDao = new ReservationDao();
        ProjectDao projectDao = new ProjectDao();

        boolean exists =
                reservationDao.existsReservation(
                        userId,
                        projectId
                );

        if (exists) {
            response.getWriter().write(
                    "您已经预约过该项目"
            );

            return;
        }

        String reservationCode =
                "R" +
                System.currentTimeMillis();

        boolean success =
                reservationDao.createReservation(
                        userId,
                        projectId,
                        reservationCode
                );

        if (!success) {
            response.getWriter().write(
                    "预约失败"
            );

            return;
        }

        boolean decreaseSuccess =
                projectDao.decreaseRemainingCount(
                        projectId
                );

        if (!decreaseSuccess) {
            response.getWriter().write(
                    "扣减名额失败"
            );

            return;
        }

        response.getWriter().write(
                "预约成功"
        );
    }
}