package servlet;

import java.io.IOException;
import java.net.URLEncoder;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.SignupLogic;
import model.User;

@WebServlet("/Signup")
public class SignupServlet extends HttpServlet {
	
	public SignupServlet() {
		super();
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String email = request.getParameter("email");
	    String plainPassword = request.getParameter("password");

	    SignupLogic signuplogic = new SignupLogic();

	    // 登録処理
	    User user = signuplogic.signup(email, plainPassword);

	    if (user == null) {
	        // userがnullの場合、signup.htmlに戻してエラーメッセージ表示（登録失敗）
	    	String errorMessage = "新規登録が失敗しました。";
	    	response.sendRedirect("/site/html/signup.html?error=" + URLEncoder.encode(errorMessage, "UTF-8"));
	    } else {
	        // userがnullでない場合、login.htmlにリダイレクト（登録成功）
	        response.sendRedirect("/site/html/login.html");
	    }
	}

}
