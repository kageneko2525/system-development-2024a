package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 *@author x22u004
 */
public class ThreadMakeDao extends BaseDao {

	/**
	 * 最新のスレッドIDを検索
	 * @param ThreadNumber int型
	 * @return 最新のスレッドIDをint型で返す
	 */
	public int findLatestThread() {
		//Existence:存在
		int thread_id = 0;

		try {

			this.connect();

			String sql = "SELECT thread_id"
					+ "FROM thread"
					+ "WHERE thread_id = (SELECT MAX(thread_id) FROM thread);";

			PreparedStatement ps = con.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
		            thread_id = rs.getInt("thread_id");
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

		return thread_id;
	}

}