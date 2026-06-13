import dao.ReservationDao;
import entity.Reservation;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        ReservationDao dao =
                new ReservationDao();

        List<Reservation> list =
                dao.getReservationsByUserId(
                        1
                );

        for (Reservation reservation : list) {

            System.out.println(
                    reservation.getReservationCode()
            );

        }

    }

}