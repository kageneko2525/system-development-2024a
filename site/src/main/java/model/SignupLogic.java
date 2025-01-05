package model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLIntegrityConstraintViolationException;

import dao.UserDao;

public class SignupLogic {
	
	/*受け取ったパスワードのハッシュ化 ログインでも使うためスタティックで宣言*/
	public static String hashPassword(String password) {
	    try {
	        MessageDigest md = MessageDigest.getInstance("SHA-256");
	        byte[] hashedBytes = md.digest(password.getBytes());
	        StringBuilder sb = new StringBuilder();
	        for (byte b : hashedBytes) {
	            sb.append(String.format("%02x", b));
	        }
	        return sb.toString();
	        
	    } catch (NoSuchAlgorithmException e) {
	        throw new RuntimeException("Hashing algorithm not found.", e);
	    }
	}

	
	/*渡された値から新規登録*/	
	
	
	public User signup(String email, String password) {
	    UserDao userDao = new UserDao();
	    User user = new User();
	    String hashPass = null;

	    int flag = 0;

	    try {
	        //パスワードのハッシュ化
	        hashPass = hashPassword(password);

	        // userオブジェクトに設定
	        user.setBanFlg(flag);
	        user.setMail(email);
	        user.setId(email);

	        // ユーザを追加
	        boolean isSuccess = userDao.addUser(user, hashPass);

	        // 追加が失敗した場合、userをnullに設定
	        if (!isSuccess) {
	            user = null;
	        }
	        
	    } catch (SQLIntegrityConstraintViolationException e) {
	    	// 重複エントリの例外をキャッチして処理
	    	System.err.println("Duplicate entry: " + e.getMessage());
	    	user = null;
	    } catch (Exception e) {
	        e.printStackTrace();
	        user = null; // エラーが発生した場合、userをnullに設定
	    }

	    return user;
	}

	
}
