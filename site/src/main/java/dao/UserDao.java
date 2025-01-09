package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.User;

public class UserDao extends SampleDao{

	
	
	
	
	
	/**
	 * ユーザー登録
	 */
	public boolean addUser(User user, String password) throws SQLException, ClassNotFoundException {
	    try {
	        connect();
	        con.setAutoCommit(false); // トランザクション開始
	        
	        // userテーブルを更新
	        updateUserTable(con, user);
	        
	        // user_hashテーブルを更新
	        updateUserHashTable(con, user, password);
	        
	        con.commit(); // トランザクションをコミット
	        return true; // 成功時にtrueを返す
	    } catch (SQLException e) {
	        if (con != null) {
	            try {
	                con.rollback(); // エラーが発生した場合はロールバック
	            } catch (SQLException rollbackException) {
	                rollbackException.printStackTrace();
	            }
	        }
	        throw e; // 例外をスロー
	    } finally {
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

	//userテーブル
	private void updateUserTable(Connection connection, User user) throws SQLException {
		String sql = "INSERT INTO user (user_id , mail , ban_flag) VALUES (?,?,?);";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, user.getId());
			ps.setString(2, user.getMail());
			ps.setInt(3, user.getBanFlg());
			
			ps.executeUpdate();
		}
	}
	//user_hashテーブル
	private void updateUserHashTable(Connection connection, User user, String password) throws SQLException {
		String sql = "INSERT INTO user_hash (user_id , hash) VALUES (?,?);";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, user.getId());
			ps.setString(2, password);
			
			ps.executeUpdate();
		}
	}


	/**
	 * ログイン処理 現在IDはメールアドレスを使用
	 */
	public User loginUser(String id, String hashedPassword) throws SQLException, ClassNotFoundException {
	    User user = null;

	    try {
	        connect();
	        con.setAutoCommit(false); // トランザクション開始

	        // user_hashテーブルからuser_idを取得
	        String userId = getUserIdByIdAndHashedPassword(id, hashedPassword);

	        if (userId != null) {
	            // userテーブルからユーザー情報を取得
	            user = getUserById(userId);
	        }

	        con.commit(); // トランザクションをコミット

	    } catch (SQLException | ClassNotFoundException e) {
	        if (con != null) {
	            con.rollback(); // エラーが発生した場合はロールバック
	        }
	        throw e;
	    } finally {
	        if (con != null) {
	            con.setAutoCommit(true);
	            disConnect();
	        }
	    }

	    return user;
	}
	
	private String getUserIdByIdAndHashedPassword(String id, String hashedPassword) throws SQLException {
	    String userId = null;

	    String sql = "SELECT user_id FROM user_hash WHERE user_id = ? AND hash = ?";
	    try (PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setString(1, id);
	        ps.setString(2, hashedPassword);
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                userId = rs.getString("user_id");
	            }
	        }
	    }

	    return userId;
	}

	private User getUserById(String userId) throws SQLException {
	    User user = null;

	    String sql = "SELECT * FROM user WHERE user_id = ?";
	    try (PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setString(1, userId);
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                user = new User();
	                user.setId(rs.getString("user_id"));
	                user.setMail(rs.getString("mail"));
	                user.setBanFlg(rs.getInt("ban_flag"));
	            }
	        }
	    }

	    return user;
	}
	
	

}
