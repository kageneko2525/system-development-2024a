package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.SignupLogic;
import model.User;

@WebServlet("/Signup")
public class SignupServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
        String email = request.getParameter("email");
        String plainPassword = request.getParameter("password");
        
        SignupLogic signuplogic = new SignupLogic();

        // パスワードをハッシュ化
        String hashedPassword = SignupLogic.hashPassword(plainPassword);
        
        //DBへ登録
        User user = signuplogic.signup(email, hashedPassword);
        
        if (user == null) {
        	// userがnullの場合、signup.htmlに戻してエラーメッセージ表示（登録失敗）
        	request.setAttribute("errorMessage", "Registration failed. Please try again.");
        	request.getRequestDispatcher("signup.html").forward(request, response);
        } else {
        	// userがnullでない場合、login.htmlにリダイレクト（登録成功）
        	response.sendRedirect("login.html");
        }
        
    }
}
