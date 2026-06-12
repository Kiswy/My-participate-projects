import dao.ProjectDao;
import entity.Project;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        ProjectDao dao =
                new ProjectDao();

        List<Project> list =
                dao.getProjectsByCategory(1);

        for(Project project : list){

            System.out.println(
                    project.getProjectName()
                    + " | "
                    + project.getLocation()
                    + " | "
                    + project.getAppointmentTime()
            );

        }

    }
}