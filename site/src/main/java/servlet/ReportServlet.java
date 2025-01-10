package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/Report")
public class ReportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	

public ReportServlet() {
	 super();
  }
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
  } 
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	String radioButton = request.getParameter("report");
	String[] reasons = request.getParameterValues("reason");
	
	String text = request.getParameter("text");
	
	
	
	
	doGet(request, response);
  }
}
