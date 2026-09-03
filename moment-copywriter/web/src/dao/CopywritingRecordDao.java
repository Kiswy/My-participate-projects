package dao;

import entity.CopywritingRecord;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class CopywritingRecordDao {
    public int add(CopywritingRecord record) {
        String sql = "INSERT INTO copywriting_records("
                + "user_id, scene, mood, style, keywords, generated_content, ai_model"
                + ") VALUES(?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            if (record.getUserId() > 0) {
                ps.setInt(1, record.getUserId());
            } else {
                ps.setNull(1, Types.INTEGER);
            }

            ps.setString(2, record.getScene());
            ps.setString(3, record.getMood());
            ps.setString(4, record.getStyle());
            ps.setString(5, record.getKeywords());
            ps.setString(6, record.getGeneratedContent());
            ps.setString(7, record.getAiModel());

            if (ps.executeUpdate() > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        record.setId(id);
                        return id;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public List<CopywritingRecord> listByUserId(int userId) {
        List<CopywritingRecord> list = new ArrayList<>();
        String sql = "SELECT TOP 50 id, user_id, scene, mood, style, keywords, "
                + "generated_content, ai_model, create_time "
                + "FROM copywriting_records WHERE user_id = ? "
                + "ORDER BY create_time DESC, id DESC";

        try (
                Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRecord(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean deleteById(int id, int userId) {
        if (id <= 0 || userId <= 0) {
            return false;
        }

        String sql = "DELETE FROM copywriting_records WHERE id = ? AND user_id = ?";

        try (
                Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, id);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private CopywritingRecord mapRecord(ResultSet rs) throws Exception {
        CopywritingRecord record = new CopywritingRecord();
        record.setId(rs.getInt("id"));
        record.setUserId(rs.getInt("user_id"));
        record.setScene(rs.getString("scene"));
        record.setMood(rs.getString("mood"));
        record.setStyle(rs.getString("style"));
        record.setKeywords(rs.getString("keywords"));
        record.setGeneratedContent(rs.getString("generated_content"));
        record.setAiModel(rs.getString("ai_model"));
        record.setCreateTime(rs.getString("create_time"));
        return record;
    }
}
