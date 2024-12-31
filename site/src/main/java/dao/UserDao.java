package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.User;

public class UserDao extends SampleDao{

	
	
	
	
	
	/**
	 * ユーザー登録(未完成)
	 * @param user 登録したいユーザー name hashを入力して渡してください。IDが設定されます。
	 * @return true:登録成功 false:登録失敗
	 */
	public boolean addUser(User user,String password) {
		
		try {
			connect();
			con.setAutoCommit(false); // トランザクション開始
			
			// userテーブルを更新
			updateUserTable(con, user);
			
			// user_hashテーブルを更新
			updateUserHashTable(con, user, password);
			
			con.commit(); // トランザクションをコミット
			return true; // 成功時にtrueを返す
		} catch (SQLException | ClassNotFoundException e) {	
			if (con != null) {
				try {
					con.rollback(); // エラーが発生した場合はロールバック
				} catch (SQLException rollbackException) {
					rollbackException.printStackTrace();
				}
			}
			e.printStackTrace();
			return false; // エラー発生時にfalseを返す		
		}finally {
			if (con != null) {
				try {
					con.setAutoCommit(true);
					disConnect();
				} catch (SQLException closeException) {
					closeException.printStackTrace();
				}
			}
		}
		
	}
	
	private void updateUserTable(Connection connection, User user) throws SQLException {
		String sql = "INSERT INTO user (user_id , mail , ban_flag) VALUES (?,?,?);";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			//idどうするか判断後
			int id = Integer.parseInt(user.getId());
			ps.setInt(1, id);
			ps.setString(2, user.getMail());
			ps.setInt(3, user.getBanFlg());
			
			ps.executeUpdate();
		}
	}
	
	private void updateUserHashTable(Connection connection, User user, String password) throws SQLException {
		String sql = "INSERT INTO user_hash (user_id , hash) VALUES (?,?);";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			//idどうするか判断後
			int id = Integer.parseInt(user.getId());
			ps.setInt(1, id);
			ps.setString(2, password);
			
			ps.executeUpdate();
		}
	}
	

	/**
	 * 最新ユーザIDを帰す（連番のため）
	 * @param int
	 */
	
	public int findLatestUser() {
	    int user_id = 0; // デフォルト値を0に設定

	    try {
	        this.connect();

	        String sql = "SELECT user_id "
	                   + "FROM user "
	                   + "WHERE user_id = (SELECT MAX(user_id) FROM user);";

	        PreparedStatement ps = con.prepareStatement(sql);

	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            // データが存在する場合はその値を取得
	            user_id = rs.getInt("user_id");
	        }

	    } catch (Exception e) {
	        e.printStackTrace();

	    } finally {
	        try {
	            this.disConnect();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    return user_id; // データがなかった場合はデフォルト値0を返す
	}

}
