package model;
/**
 * サンプルです
 * @author ねこ
 *
 */
public class SampleLogic {
	
	//-------------------------------------------------------------------------------
	
	//前書いたコードなので悪しからず
	
	//-------------------------------------------------------------------------------

	/**
	 * 登録可能かチェックします
	 * もらったUserにID,mail, NAME , HASHをセットします
	 * @param userName 登録したい名前
	 * @param pass 登録したいパスワード
	 * @param samePass 再入力したパスワード
	 * @param registrationUser 登録したUserをいれる用のUser
	 * @return null:登録成功 それ以外:登録失敗。失敗内容に応じたエラーメッセージ
	 * @author ねこ
	 */
	public String chackRegistration(String userName, String mail, String pass, String samePass, User registrationUser) {
		hashLogic hashLogic = new hashLogic();

		String hash = "";
		String sameHash = "";

		hash = hashLogic.doHash(pass);
		sameHash = hashLogic.doHash(samePass);

		//登録したいユーザー

		registrationUser.setUserName(userName);
		registrationUser.setMail(mail);
		registrationUser.setHash(hash);

		//パスワードの中身がなかったり空白だったりか判断
		if (chackBlank(userName) || chackBlank(mail) || chackBlank(pass) || chackBlank(samePass)) {
			//そうだったらエラーメッセージを返す

			return "入力されていない項目があります";

		} else {
			//中身がはいっていたら

			//名前の長さ判定
			if (userName.length() > 16) {
				//長すぎたらエラーメッセージ
				return "ユーザー名は16文字以下までです";
			} else if (pass.length() < 8) {
				return "パスワードが短すぎます";

			} else if (mail.length() > 100) {
				return "メールアドレスが長すぎます";
				
			} else if (!isValidMailAddress(mail)) {
				return "使用できないメールアドレスです";

				//正しい名前とパスワードか判定
			} else if (checkCorrectName(userName) && checkCorrectPass(pass)) {
				//正しかったら

				//もしハッシュと再入力のハッシュが同じか判定
				if (hash.equals(sameHash)) {
					//一緒なら
					UserDao userDao = new UserDao();

					//すでに同じメールアドレスのユーザーかいるか検索し判定
					if (userDao.findByMail(mail)) {

						//いたらエラーメッセージを返す
						return "使用できないメールアドレスです";
					} else {

						//いないなら
						//登録処理をしてエラーメッセージなしで返す
						if (userDao.setUser(registrationUser)) {

							//登録成功
							if (detabeasSetUp(registrationUser)) {

								return null;
							} else {

								return "不明なエラーが発生しました。お手数ですが管理者にお問い合わせください。";
							}

						} else {

							//登録失敗
							return "登録に失敗しました。時間をおいて再度お試しください";
						}
					}

				} else {

					//一緒じゃないならエラーメッセージを返す
					return "パスワードが一致しません";
				}
			} else {

				//正しい名前とパスワードじゃないなら
				//エラーメッセージを返す
				return "使用できない文字が含まれています";
			}
		}

	}

	/**
	 * 正しい名前か判定メソッド
	 * Correct:正しい
	 * @param name 判定したい名前
	 * @return true:正しい名前　false:ダメな名前
	 * @author ねこ
	 */
	private static boolean checkCorrectName(String name) {
		// 正規表現パターン：小文字、大文字、英数字、アンダースコア以外の文字が含まれている場合にマッチ
		String pattern = "[\\w\\uFF10-\\uFF19\\uFF21-\\uFF3A\\uFF41-\\uFF5A\\u3041-\\u3096\\u30A1-\\u30FA\\u3400-\\u4DBF\\u4E00-\\u9FFF_()=;:$%&'.・*!?]+";

		// 正規表現パターンにマッチするかどうかを検査
		Pattern regex = Pattern.compile(pattern);
		Matcher matcher = regex.matcher(name);
		// '/'が含まれている場合にfalseを返す
		return matcher.find() && !name.contains("/");
	}

	/**
	 * 正しいパスワードか判定メソッド
	 * Correct:正しい
	 * @param name 判定したいパスワード
	 * @return true:正しいパスワード　false:ダメなパスワード
	 * @author ねこ
	 */
	private static boolean checkCorrectPass(String pass) {

		// 正規表現パターン：英字（大文字と小文字）および指定の記号以外の文字が含まれている場合にfalse
		String pattern = "[a-zA-Z0-9~`!@#$%^&*()_+=]";

		// 正規表現パターンにマッチするかどうかを検査
		Pattern regex = Pattern.compile(pattern);
		Matcher matcher = regex.matcher(pass);
		// マッチがあれば無効な文字が含まれているとみなす
		return matcher.find() && !pass.contains("/");
	}

	/**
	 * ユーザー登録時のポイントテーブル設定用
	 * @param user 
	 * @return true:成功　false:失敗
	 * @author ねこ
	 */
	public boolean detabeasSetUp(User user) {
		PointDao pointDao = new PointDao();

		if (pointDao.setUser(user)) {
			return true;

		}

		return false;

	}

	/**
	 * 中身がないかのチェック用
	 * @param st
	 * @return
	 */
	public boolean chackBlank(String st) {

		return st == null || st.isEmpty();

	}

	/**
	 * メールアドレスチェック
	 * @param mail 検証対象の値
	 * @return 結果（true：メールアドレス、false：メールアドレスではない）
	 */
	public static boolean isValidMailAddress(String mail) {
		boolean result = false;

		if (mail != null) {
			if (EmailValidator.getInstance().isValid(mail)) {
				result = true;
			}
		}

		return result;
	}

	
	
}
