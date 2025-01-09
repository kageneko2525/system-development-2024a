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
 * IDの新しいスレッドを指定した場所から指定した数値分取得
 * @param count 何件ほしいか
 * @param offset 最初の何件飛ばすか
 * @return Threadのリスト
 */
	public List<ThreadDto> getLatestThread(int count,int offset) {

		//戻り値となるリスト宣言
		List<ThreadDto> list = new ArrayList<ThreadDto>();

		try {
			this.connect();

			String sql = "SELECT * "
					+ "FROM thread "
					+ "WHERE thread_state = 0 "
					+ "ORDER BY thread_id ASC "
					+ "LIMIT ? OFFSET ?;";

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setInt(1, count);
			ps.setInt(2, offset);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				ThreadDto thread = new ThreadDto(rs.getString("thread_id"),rs.getString("thread_name"),rs.getTimestamp("thread_make"));

				list.add(thread);
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
	 * タイトル検索 新しいもの
	 * @param title タイトル
	 * @param count 何件取得するか
	 * @param offset 最初に何件を飛ばすか
	 * @param threadState 生きてるスレッドか
	 * @return リスト
	 */
public List<ThreadDto> getThreadTitleSortNew(String title,int count,int offset,int threadState) {
	//戻り値となるリスト宣言
	List<ThreadDto> list = new ArrayList<ThreadDto>();

	try {
		this.connect();

		String sql = "SELECT * "
				+ "FROM thread "
				+ "WHERE thread_state = ? "
				+ "AND thread_name LIKE '%?%'"
				+ "ORDER BY thread_id ASC "
				+ "LIMIT ? OFFSET ?;";
		
		PreparedStatement ps = con.prepareStatement(sql);

		ps.setInt(1, threadState);
		ps.setString(2, title);
		ps.setInt(3, count);
		ps.setInt(4, offset);

		ResultSet rs = ps.executeQuery();

		while (rs.next()) {

			ThreadDto thread = new ThreadDto();
			thread.setId(rs.getNString("id"));
			thread.setTitle(rs.getString("title"));
			thread.setCreateTime(rs.getTimestamp("CreateTime"));
			list.add(thread);
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

	
	/**
	 * 新しいthreadをDB登録
	 * @param threadDto 登録したいスレッドのDTO
	 * @author neko
	 */
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
