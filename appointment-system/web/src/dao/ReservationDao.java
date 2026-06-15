package dao;

import entity.Reservation;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ReservationDao {
    // 查询是否存在预约
    public boolean existsReservation(
            Integer userId,
            Integer projectId
    ) {
        String sql =
                "SELECT * " +
                        "FROM reservations " +
                        "WHERE user_id = ? " +
                        "AND project_id = ? " +
                        "AND status = '已预约'";

        try {

            Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, userId);
            ps.setInt(2, projectId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                rs.close();
                ps.close();
                conn.close();

                return true;
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // 创建预约记录
    public boolean createReservation(
            Integer userId,
            Integer projectId,
            String reservationCode
    ) {

        String sql =
                "INSERT INTO reservations " +
                        "(reservation_code, user_id, project_id) " +
                        "VALUES (?, ?, ?)";

        try {
            Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, reservationCode);
            ps.setInt(2, userId);
            ps.setInt(3, projectId);

            int rows = ps.executeUpdate();

            ps.close();
            conn.close();

            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // 查询用户的所有预约
    public List<Reservation> getReservationsByUserId(
            Integer userId) {
        List<Reservation> list = new ArrayList<>();

        String sql =
                "SELECT " +
                        "r.*, " +
                        "p.project_name, " +
                        "u.username " +
                        "FROM reservations r " +
                        "JOIN projects p " +
                        "ON r.project_id = p.id " +
                        "JOIN users u " +
                        "ON r.user_id = u.id " +
                        "WHERE r.user_id = ? " +
                        "AND r.status = ?";

        try {
            Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, userId);
            ps.setString(2, "已预约");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Reservation reservation = new Reservation();

                reservation.setId(
                        rs.getInt("id")
                );

                reservation.setReservationCode(
                        rs.getString("reservation_code")
                );

                reservation.setUserId(
                        rs.getInt("user_id")
                );

                reservation.setProjectId(
                        rs.getInt("project_id")
                );

                reservation.setProjectName(
                        rs.getString("project_name")
                );

                reservation.setUsername(
                        rs.getString("username")
                );

                reservation.setReserveTime(
                        rs.getString("reserve_time")
                );

                reservation.setStatus(
                        rs.getString("status")
                );

                list.add(
                        reservation
                );
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // 根据ID查询预约记录
    public Reservation getReservationById(
            Integer reservationId
    ) {
        String sql =
                "SELECT * " +
                        "FROM reservations " +
                        "WHERE id = ?";

        try {
            Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, reservationId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Reservation reservation = new Reservation();

                reservation.setId(
                        rs.getInt("id")
                );

                reservation.setReservationCode(
                        rs.getString("reservation_code")
                );

                reservation.setUserId(
                        rs.getInt("user_id")
                );

                reservation.setProjectId(
                        rs.getInt("project_id")
                );

                reservation.setReserveTime(
                        rs.getString("reserve_time")
                );

                reservation.setStatus(
                        rs.getString("status")
                );

                rs.close();
                ps.close();
                conn.close();

                return reservation;
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;
    }

    // 取消预约
    public boolean cancelReservation(
            Integer reservationId) {
        String sql =
                "UPDATE reservations " +
                        "SET status = '已取消' " +
                        "WHERE id = ?";

        try {
            Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, reservationId);

            int rows = ps.executeUpdate();

            ps.close();
            conn.close();

            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // 检查项目是否存在有效预约
    public boolean existsActiveReservation(
            Integer projectId
    ) {
        String sql =
                "SELECT * " +
                        "FROM reservations " +
                        "WHERE project_id = ? " +
                        "AND status = '已预约'";

        try {
            Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, projectId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                rs.close();
                ps.close();
                conn.close();

                return true;
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // 查询全部预约（管理员预约总览）
    public List<Reservation> getAllReservations() {
        List<Reservation> list = new ArrayList<>();
        String sql =
                "SELECT " +
                "r.*, " +
                "u.username, " +
                "u.phone, " +
                "p.project_name " +
                "FROM reservations r " +
                "JOIN users u " +
                "ON r.user_id = u.id " +
                "JOIN projects p " +
                "ON r.project_id = p.id";

        try {
            Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Reservation reservation = new Reservation();

                reservation.setId(rs.getInt("id"));
                reservation.setReservationCode(rs.getString("reservation_code"));
                reservation.setUserId(rs.getInt("user_id"));
                reservation.setProjectId(rs.getInt("project_id"));
                reservation.setReserveTime(rs.getString("reserve_time"));
                reservation.setStatus(rs.getString("status"));
                reservation.setUsername(rs.getString("username"));
                reservation.setPhone(rs.getString("phone"));
                reservation.setProjectName(rs.getString("project_name"));

                list.add(reservation);
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
