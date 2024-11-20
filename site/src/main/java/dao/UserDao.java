package dao;

import model.User;

public class UserDao extends SampleDao{

	
	
	
	
	
	/**
	 * ユーザー登録(未完成)
	 * @param user 登録したいユーザー name hashを入力して渡してください。IDが設定されます。
	 * @return true:登録成功 false:登録失敗
	 */
	public boolean addUser(User user) {
		try {
			this.connect();
			
			String sql = "Insert into user (user_id , mail , ban_flag) Values (?,?,?);";
			
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			
		}
		return false;
	}
	
	
}
