package servlet;

import java.io.IOException;
import java.net.URLEncoder;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.LoginLogic;
import model.SignupLogic;
import model.User;

/** ログイン用サーブレット
 * @author ねこ
 */
@WebServlet("/Login")
public class LoginServlet extends HttpServlet {

	public LoginServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false); // 既存のセッションがあれば取得、なければnull
		
		if (session != null && session.getAttribute("user") != null) {
			// セッションにユーザ情報がある場合、index.htmlにリダイレクト
			response.sendRedirect("/site/html/index.html");
		} else {
			// セッションにユーザ情報がない場合、login.htmlにリダイレクト
			response.sendRedirect(request.getContextPath() + "/site/html/login.html");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("email");
		String plainPassword = request.getParameter("password");
		
		LoginLogic loginLogic = new LoginLogic();
		
		// パスワードをハッシュ化
	    String hashedPassword = SignupLogic.hashPassword(plainPassword);
	    
	    User user = loginLogic.loginUser(email, hashedPassword);
	    
	    if (user != null) {
	    	HttpSession session = request.getSession(true); // 新しいセッションを作成
	    	session.setAttribute("user", user); // ユーザ情報をセッションに保存
	    	response.sendRedirect("/site/html/index.html"); // ログイン成功時にindex.htmlにリダイレクト
	    } else {
	    	// ログイン失敗時にエラーメッセージをクエリパラメータとして渡す
	    	String errorMessage = "ログインに失敗しました。";
	    	response.sendRedirect("/site/html/login.html?error=" + URLEncoder.encode(errorMessage, "UTF-8"));
	    }
	    
	}

}
