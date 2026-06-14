import dao.ReservationDao;
import dao.ProjectDao;
import entity.Project;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        ProjectDao dao =
                new ProjectDao();

        Project project =
                new Project();

        project.setCategoryId(1);

        project.setProjectName(
                "羽毛球场"
        );

        project.setDescription(
                "羽批圣地"
        );

        project.setLocation(
                "体育馆一楼"
        );

        project.setAppointmentTime(
                "周日上午"
        );

        project.setCapacity(6);

        boolean result =
                dao.addProject(
                        project
                );

        System.out.println(
                result
        );

    }

}