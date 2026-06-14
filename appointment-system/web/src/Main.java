import dao.ReservationDao;
import dao.ProjectDao;
import entity.Reservation;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        ReservationDao dao =
                new ReservationDao();

        List<Reservation> list =
                dao.getAllReservations();

        for (Reservation reservation : list) {

            System.out.println(

                    reservation.getReservationCode()

                            + " | "

                            + reservation.getProjectName()

                            + " | "

                            + reservation.getUsername()

                            + " | "

                            + reservation.getPhone()

                            + " | "

                            + reservation.getStatus()

            );
        }
    }
}