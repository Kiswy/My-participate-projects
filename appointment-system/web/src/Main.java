import dao.ReservationDao;
import entity.Reservation;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        ReservationDao dao =
                new ReservationDao();

        List<Reservation> list =
                dao.getReservationsByUserId(1);

boolean result =
        dao.cancelReservation(
                1
        );

System.out.println(result);

    }

}