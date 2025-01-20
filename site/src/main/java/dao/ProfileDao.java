package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Profile;

public class ProfileDao extends BaseDao {

    public Profile getUserById(String userId) {
        Profile prof = null;
        String sql = "SELECT " +
                     "    p.user_name, p.user_icon, p.user_birth, p.user_comment, " +
                     "    pv.public_name, pv.public_birth, pv.public_display, pv.public_comment " +
                     "FROM " +
                     "    profile p " +
                     "JOIN " +
                     "    privacy pv ON p.user_id = pv.user_id " +
                     "WHERE " +
                     "    p.user_id = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // データベース接続
            this.connect();

            // PreparedStatement の作成
            ps = con.prepareStatement(sql);
            ps.setString(1, userId); // パラメータを設定

            // クエリ実行
            rs = ps.executeQuery();

            if (rs.next()) {
                // Profile オブジェクトにデータをセット
                prof = new Profile();
                prof.setUserId(userId);
                prof.setUserName(rs.getString("user_name"));
                prof.setUserIcon(rs.getString("user_icon"));
                prof.setUserBirth(rs.getString("user_birth"));
                prof.setUserComment(rs.getString("user_comment"));
                prof.setPublicName(rs.getInt("public_name"));
                prof.setPublicBirth(rs.getInt("public_birth"));
                prof.setPublicDisplay(rs.getInt("public_display"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // リソースをクローズ
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                this.disConnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return prof;
    }
}
