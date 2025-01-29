package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.ThreadDto;

public class ThreadTagDao extends BaseDao {

	/**
	 * スレッドDTOにタグをセット
	 * @param dto
	 */
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

	/**
	 * スレッドにタグをセット
	 * @param threadDtos
	 * @return 引数返してるけどあんまり意味なし
	 */
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

	public List<ThreadDto> searchTag(String tag) {
		ArrayList<ThreadDto> threadDtoList = new ArrayList<ThreadDto>();
		
		
		try {
			
			this.connect();
			
				
			String sql = "SELECT DISTINCT * "
					+ "FROM thread_tag "
					+ "WHERE tag = ? "
					+ "ORDER BY thread_id ASC";

			System.out.println(sql);

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setString(1, tag);
			
			ResultSet rs = ps.executeQuery();

			
			while (rs.next()) {
				ThreadDto threadDto = new ThreadDto();
				threadDto.setId(rs.getString("thread_id"));
				threadDtoList.add(threadDto);
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
		
		return threadDtoList;
		
	}

}
