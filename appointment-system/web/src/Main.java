import dao.ReservationDao;
import dao.ProjectDao;
import entity.Project;

import java.util.List;

public class Main {

    public static void main(String[] args) {

ReservationDao reservationDao =
        new ReservationDao();

System.out.println(
        reservationDao
                .existsActiveReservation(1)
);

    }

}