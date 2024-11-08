package model;

/**
 * DB:userと対応
 * @author ねこ
 */
public class User {

	private String id;
	private String mail;
	private int banFlg;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public int getBanFlg() {
		return banFlg;
	}

	public void setBanFlg(int banFlg) {
		this.banFlg = banFlg;
	}
}
