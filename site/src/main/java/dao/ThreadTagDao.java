package dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

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

}
