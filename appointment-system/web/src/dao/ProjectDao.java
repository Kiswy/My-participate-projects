package dao;

import entity.Project;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProjectDao {
    public List<Project> getProjectsByCategory(
            Integer categoryId) {
        List<Project> list = new ArrayList<>();
        String sql =
                "SELECT * FROM projects " +
                        "WHERE category_id = ?";

        try {
            Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, categoryId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Project project = new Project();

                project.setId(rs.getInt("id"));
                project.setCategoryId(rs.getInt("category_id"));
                project.setProjectName(rs.getString("project_name"));
                project.setDescription(rs.getString("description"));
                project.setLocation(rs.getString("location"));
                project.setAppointmentTime(rs.getString("appointment_time"));
                project.setCapacity(rs.getInt("capacity"));
                project.setRemainingCount(rs.getInt("remaining_count"));

                list.add(project);
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // 扣减剩余名额
    public boolean decreaseRemainingCount(
            Integer projectId) {
        String sql =
                "UPDATE projects " +
                        "SET remaining_count = " +
                        "remaining_count - 1 " +
                        "WHERE id = ?";

        try {
            Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, projectId);

            int rows = ps.executeUpdate();

            ps.close();
            conn.close();

            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // 恢复剩余名额
    public boolean increaseRemainingCount(
            Integer projectId) {
        String sql =
                "UPDATE projects " +
                        "SET remaining_count = " +
                        "remaining_count + 1 " +
                        "WHERE id = ?";

        try {
            Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, projectId);

            int rows = ps.executeUpdate();

            ps.close();
            conn.close();

            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}