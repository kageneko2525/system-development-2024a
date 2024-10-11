package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//-------------------------------------------------------------

//これはサンプルです。
//jsとの兼ね合いがまだわからないので画面遷移をリダイレクトと、フォワードのどちらでやるか悩み中です。

//------------------------------------------------------------
@WebServlet("/Sample")
public class SampleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public SampleServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
