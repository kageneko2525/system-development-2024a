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
	
		/*ユーザのidに関して
		 * 現在は、DB側もモデルもStringあるいはVARCHARで保存
		 * IDを1からの連番にする関係上、DBに最も番号が大きいIDを検索して∔1するという処理
		 * DB側のデータの持ち方とUserモデルのデータの持ち方をintに変更してもらえると
		 * いったんstringに変換してます。*/
	
	public User signup(String email, String password) {
	    UserDao userDao = new UserDao();
	    User user = new User();

	    int flag = 0;

	    try {
	        // パラメータの検証
	        if (email == null || email.isEmpty()) {
	            throw new IllegalArgumentException("Email cannot be null or empty.");
	        }
	        if (password == null || password.isEmpty()) {
	            throw new IllegalArgumentException("Password cannot be null or empty.");
	        }

	        // 最新のユーザIDを取得し、インクリメント
	        int id = userDao.findLatestUser();
	        id++;

	        // ユーザのIDを文字列に変換
	        String strid = Integer.toString(id);

	        // userオブジェクトに設定
	        user.setBanFlg(flag);
	        user.setMail(email);
	        user.setId(strid);

	        // ユーザを追加
	        boolean isSuccess = userDao.addUser(user, password);

	        // 追加が失敗した場合、userをnullに設定
	        if (!isSuccess) {
	            user = null;
	        }
	        
	    } catch (SQLIntegrityConstraintViolationException e) {
	    	// 重複エントリの例外をキャッチして処理
	    	System.err.println("Duplicate entry: " + e.getMessage());
	    	user = null;

	    } catch (IllegalArgumentException e) {
	        System.err.println("Invalid input: " + e.getMessage());
	        user = null;
	    } catch (Exception e) {
	        e.printStackTrace();
	        user = null; // エラーが発生した場合、userをnullに設定
	    }

	    return user;
	}

	
}
