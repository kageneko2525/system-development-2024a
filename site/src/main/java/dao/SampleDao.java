package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

//------------------------------------------------------------------------

//これはサンプルです！

//------------------------------------------------------------------------




/**
 * user表のDAO
 * @author ねこ
 *
 */
public class SampleDao extends BaseDao {

	/**
	 * IDとハッシュに一致する名前があるか
	 * 　　もらったUserにIDをセットします
	 * @param user ユーザークラス ID,NAMEを設定済みにしてね
	 * @return true:ある（ログイン成功）false:ない(ログイン失敗)
	 * @author ねこ
	 */
	public boolean findByIdAndHash(User user) {

		boolean isLogin = false;

		try {

			this.connect();

			String sql = "SELECT user_id, user_name, hash "
					+ "FROM user "
					+ "WHERE user_name = ? "
					+ "AND hash = ? ";

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setString(1, user.getUserName());
			ps.setString(2, user.getHash());
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				int id = rs.getInt("user_id");
				user.setUserId(id);
				isLogin = true;

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

		return isLogin;
	}

	/**
	 * 引数と一致する名前のユーザーが存在するか
	 * @param userName 調べたい名前
	 * @return true;存在する false:存在しない
	 * @author ねこ
	 */
	public boolean findByName(String userName) {
		//Existence:存在
		boolean isExistence = false;

		try {

			this.connect();

			String sql = "SELECT user_id, user_name, hash "
					+ "FROM user "
					+ "WHERE user_name = ? ";

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setString(1, userName);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				isExistence = true;

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

		return isExistence;
	}
	
	
	/**
	 * 引数と一致する名前のユーザーが存在するか
	 * @param userName 調べたい名前
	 * @return true;存在する false:存在しない
	 * @author ねこ
	 */
	public boolean findByMail(String mail) {
		//Existence:存在
		boolean isExistence = false;

		try {

			this.connect();

			String sql = "SELECT user_id, user_name, hash "
					+ "FROM user "
					+ "WHERE mail = ? ";

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setString(1, mail);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				isExistence = true;

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

		return isExistence;
	}

	/**
	 * すべてのユーザー取得し、アレイリストに入れてもどす
	 * @return すべてのユーザーが入ったアレイリスト
	 * 			登録者０のとき空のリスト
	 * @author ねこ
	 */
	public ArrayList<User> findAllUser() {
		//ユーザーを入れるリスト
		ArrayList<User> userList = new ArrayList<User>();
		try {

			this.connect();

			String sql = "SELECT user_id, mail, user_name, hash, exist_flg, ban_flg "
					+ "FROM user ";

			PreparedStatement ps = con.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				User user = new User();
				user.setUserId(rs.getInt("user_id"));
				user.setMail(rs.getString("mail"));
				user.setUserName(rs.getString("user_name"));
				user.setHash(rs.getString("hash"));
				user.setIsExsit(rs.getInt("exist_flg"));
				user.setIsBan(rs.getInt("ban_flg"));
				userList.add(user);
			}
			return userList;
		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			try {

				this.disConnect();

			} catch (SQLException e) {

				e.printStackTrace();

			}
		}

		return null;
	}

	
	/**
	 * 退会してないユーザーを取得し、アレイリストに入れてもどす
	 * @return 退会してないユーザーが入ったアレイリスト
	 * 			登録者０のとき空のリスト
	 * @author 山
	 */
	public ArrayList<User> findExsitUser() {
		//ユーザーを入れるリスト
		ArrayList<User> userList = new ArrayList<User>();
		try {

			this.connect();

			String sql = "SELECT user_id, user_name, hash "
					+ "FROM user WHERE exist_flg = 1 "
					+ " or ban_flg = 1";

			PreparedStatement ps = con.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				User user = new User();
				user.setUserId(rs.getInt("user_id"));
				user.setUserName(rs.getString("user_name"));
				user.setHash(rs.getString("hash"));
				userList.add(user);
			}
			return userList;
		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			try {

				this.disConnect();

			} catch (SQLException e) {

				e.printStackTrace();

			}
		}

		return null;
	}

	/**
	 * ユーザー登録
	 * @param user 登録したいユーザー name hash設定済み IDが設定される
	 * @return true:登録成功 false:登録失敗
	 * @author ねこ
	 */
	public boolean setUser(User user) {
		try {

			this.connect();
			UserDao userDao = new UserDao();

			String sql = "INSERT INTO User (user_id ,mail , user_name , hash, exist_flg ) VALUES (?, ?, ?, ?, '1')";

			PreparedStatement ps = con.prepareStatement(sql);
			
			int id =1;
			if(userDao.findAllUser()!=null) {
				id = userDao.findAllUser().size() + 1;
				
			}

			ps.setString(1, Integer.toString(id));
			ps.setString(2, user.getMail());
			ps.setString(3, user.getUserName());
			ps.setString(4, user.getHash());

			ps.executeUpdate();
			user.setUserId(id);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				this.disConnect();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;

	}
	
	
	public boolean updateGame_Max_Point(GamePoint gamepoint) {
		boolean isUpdate = false;

		try {
			this.connect();

			String sql = "UPDATE game_point set max_game_point=?"
					+ " where user_id=?"
					+ " and game_id=?";

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setInt(1, 0);
			ps.setInt(2, gamepoint.getUserId());
			ps.setInt(3, gamepoint.getGameId());
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				int max = rs.getInt("max_game_point");
				gamepoint.setMaxGamePoint(max);
				isUpdate = true;
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
		return isUpdate;
	}

}