package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.ThreadDto;

public class ThreadTagDao extends BaseDao {

	public void setTag(ThreadDto dto) {

		try {
			this.connect();

			String sql = "INSERT INTO thread_tag VALUES ";

			for (int i = 0; i < dto.getTags().size(); i++) {

				sql += 0 < i ? "," : "";
				sql += "(" + dto.getId() + ", ?)";
			}
			sql += ";";
			System.out.println(sql);
			PreparedStatement ps = con.prepareStatement(sql);

			int tmp = 1;
			for (String tag : dto.getTags()) {
				ps.setString(tmp, tag);
				tmp++;

			}
			ps.executeUpdate();
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

	public List<Integer> getThreadTitleSortNew(List<String> tagList) {
		//戻り値となるリスト宣言
		List<Integer> list = new ArrayList<Integer>();

		try {
			this.connect();

			String sql = "SELECT * "
					+ "FROM thread "
					+ "WHERE thread_state = ? "
					+ "AND thread_name LIKE '%?%'"
					+ "ORDER BY thread_id ASC "
					+ "LIMIT ? OFFSET ?;";

			PreparedStatement ps = con.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

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

	public List<ThreadDto> setTag(List<ThreadDto> threadDtos) {

		if (threadDtos == null || threadDtos.size() == 0) {
			return null;
		}

		try {

			this.connect();

			String sql = "SELECT * "
					+ "FROM thread_tag "
					+ "WHERE thread_id in (";

			for (int i = 0; i < threadDtos.size(); i++) {

				sql += 0 < i ? ", ? " : " ? ";
			}
			sql += " ) "
					+ "ORDER BY thread_id ASC";

			System.out.println(sql);

			PreparedStatement ps = con.prepareStatement(sql);

			for (int i = 0; i < threadDtos.size(); i++) {

				ps.setString(i + 1, threadDtos.get(i).getId());
			}

			System.out.println("threadtag_sql : " + ps.toString());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				for (ThreadDto dto : threadDtos) {
					if (dto.getId().equals(rs.getString("thread_id"))) {
						dto.addTag(rs.getString("tag"));
						dto.printThread();
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			try {
				this.disConnect();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return threadDtos;
	}

}
