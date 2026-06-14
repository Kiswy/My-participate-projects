package servlet;

import com.google.gson.Gson;
import dao.ReservationDao;
import entity.Reservation;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/reservations")
public class AdminReservationServlet
        extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        ReservationDao dao = new ReservationDao();
        List<Reservation> list = dao.getAllReservations();
        Gson gson = new Gson();
        String json = gson.toJson(list);

        response.getWriter()
                .print(json);
    }
}