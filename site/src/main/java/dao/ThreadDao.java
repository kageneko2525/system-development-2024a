package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import model.ThreadDto;

public class ThreadDao extends BaseDao {

	/**
	 * IDが新しいThredを10件返す？　未完につき放置しておいてください
	 * @return ThredDto型のリストで返す
	 */
	public List<ThreadDto> LatestThread(int base, int count) {

		//戻り値となるリスト宣言
		List<ThreadDto> list = new ArrayList<ThreadDto>();

		try {
			this.connect();

			String sql = "SELECT * "
					+ "FROM thread "
					+ "WHERE thread_state = 0 "
					+ "ORDER BY thread_id ASC "
					+ "LIMIT ?;";

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setInt(1, count);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				ThreadDto thread = new ThreadDto();
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

		return list; // データがなかった場合はデフォルト値1を返す
	}

	/**
	 * 最新のスレッドIDを検索
	 * @param ThreadNumber int型
	 * @return 最新のスレッドIDをint型で返す
	 * @author x22u004
	 */
	public int findLatestThread() {
		int thread_id = 0; // デフォルト値を0に設定

		try {
			this.connect();

			String sql = "SELECT thread_id "
					+ "FROM thread "
					+ "WHERE thread_id = (SELECT MAX(thread_id) FROM thread);";

			PreparedStatement ps = con.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				// データが存在する場合はその値を取得
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

		return thread_id; // データがなかった場合はデフォルト値0を返す
	}

	/**
	 * 新しいスレッドをDB登録	
	 * @author x22u004
	 * @return 
	 */
	public void newCreateThread(int threadNo, String threadName, String threadUrl, Timestamp threadTime,
			int threadState) {
		System.out.println("DB Thread登録");
		try {

			this.connect();

			String sql = "INSERT INTO thread(thread_id,thread_name,thread_url,thread_make,thread_state) VALUES(?,?,?,?,?);";

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setInt(1, threadNo);
			ps.setString(2, threadName);
			ps.setString(3, threadUrl);
			ps.setTimestamp(4, threadTime);
			ps.setInt(5, threadState);

			ps.executeUpdate();

			ps.close();

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			try {

				this.disConnect();

			} catch (SQLException e) {

				e.printStackTrace();

			}
		}
	}

	public void newCreateThread(ThreadDto threadDto) {
		System.out.println("DB Thread登録");
		try {

			this.connect();

			String sql = "INSERT INTO thread(thread_id,thread_name,thread_url,thread_make,thread_state) VALUES(?,?,?,?,?);";

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setInt(1, Integer.parseInt(threadDto.getId()));
			ps.setString(2, threadDto.getTitle());
			ps.setString(3, "");
			ps.setTimestamp(4, threadDto.getCreateTime());
			ps.setInt(5, 0);

			ps.executeUpdate();

			ps.close();

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			try {

				this.disConnect();

			} catch (SQLException e) {

				e.printStackTrace();

			}
		}
	}

}
