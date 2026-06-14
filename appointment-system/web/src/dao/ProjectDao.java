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

    // 查询全部项目
    public List<Project> getAllProjects() {
        List<Project> list = new ArrayList<>();
        String sql =
                "SELECT " +
                        "p.*, " +
                        "c.category_name " +
                        "FROM projects p " +
                        "JOIN categories c " +
                        "ON p.category_id = c.id";

        try {
            Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Project project = new Project();

                project.setId(rs.getInt("id"));
                project.setCategoryId(rs.getInt("category_id"));
                project.setCategoryName(rs.getString("category_name"));
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

    // 新增项目
    public boolean addProject(Project project) {
        String sql =
                "INSERT INTO projects " +
                        "(category_id, " +
                        "project_name, " +
                        "description, " +
                        "location, " +
                        "appointment_time, " +
                        "capacity, " +
                        "remaining_count) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, project.getCategoryId());
            ps.setString(2, project.getProjectName());
            ps.setString(3, project.getDescription());
            ps.setString(4, project.getLocation());
            ps.setString(5, project.getAppointmentTime());
            ps.setInt(6, project.getCapacity());
            ps.setInt(7, project.getCapacity());

            int rows = ps.executeUpdate();

            ps.close();
            conn.close();

            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // 删除项目
    public boolean deleteProject(
            Integer projectId
    ) {
        String sql =
                "DELETE " +
                "FROM projects " +
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