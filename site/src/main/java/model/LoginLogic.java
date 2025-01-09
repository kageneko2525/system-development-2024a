package model;

import java.sql.SQLException;

import dao.UserDao;

public class LoginLogic {
	
	/**
	 * ログイン
	 */
	  public User loginUser(String id, String password) {
	       UserDao userDao = new UserDao();
	       User user = null;
	        try {
	            // パスワードをハッシュ化
	            String hashedPassword = SignupLogic.hashPassword(password);
	            // user_hashテーブルからuser_idを取得し、そこからuserテーブルからユーザー情報を取得
	            user = userDao.loginUser(id, hashedPassword);
	            
	        } catch (SQLException | ClassNotFoundException e) {
	            e.printStackTrace();
	            user = null;
	        } catch(Exception e) {
	        	e.printStackTrace();
	        	user = null;
	        }
	        return user;
	    }
	}

