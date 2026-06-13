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

import com.google.gson.Gson;
import entity.Reservation;

import java.util.List;

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

        ReservationDao reservationDao = new ReservationDao();
        ProjectDao projectDao = new ProjectDao();

        String action =
                request.getParameter(
                        "action"
                );

        // 取消预约
        if ("cancel".equals(action)) {
            Integer reservationId =
                    Integer.parseInt(
                            request.getParameter(
                                    "reservationId"
                            )
                    );

            Reservation reservation = reservationDao.getReservationById( reservationId );

            if (reservation == null) {
                response.getWriter().write(
                        "预约不存在"
                );

                return;
            }

            if ("已取消".equals(
                    reservation.getStatus()
            )) {
                response.getWriter().write(
                        "该预约已经取消"
                );

                return;
            }

            boolean cancelSuccess =
                    reservationDao.cancelReservation(
                            reservationId
                    );

            if (!cancelSuccess) {
                response.getWriter().write(
                        "取消失败"
                );

                return;
            }

            boolean increaseSuccess =
                    projectDao.increaseRemainingCount(
                            reservation.getProjectId()
                    );

            if (!increaseSuccess) {
                response.getWriter().write(
                        "恢复名额失败"
                );

                return;
            }

            response.getWriter().write(
                    "取消成功"
            );

            return;
        }

        // 预约
        Integer projectId =
                Integer.parseInt(
                        request.getParameter(
                                "projectId"
                        )
                );

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
                "R" + System.currentTimeMillis();

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

    // 我的预约
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
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
        ReservationDao reservationDao = new ReservationDao();

        List<Reservation> list =
                reservationDao.getReservationsByUserId(
                        userId
                );

        response.setContentType(
                "application/json;charset=UTF-8"
        );

        Gson gson = new Gson();

        response.getWriter().write(
                gson.toJson(list)
        );
    }
}