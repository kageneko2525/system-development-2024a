package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ThreadServlet
 */
public class ThreadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ThreadServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("ThreadDoGet");
		// ThreadMakeServletから送られたthreadIdを取得
		Integer threadId = (Integer) request.getSession().getAttribute("threadId");
		request.getSession().setAttribute("threadId", null);
		System.out.println(threadId);
		// 必要な処理があればここで実行
		if (threadId == null) {
			response.sendRedirect(request.getContextPath());

		} else {
			// リクエストスコープにthreadIdを設定（すでにあるが明示的に再設定）
			request.setAttribute("id", threadId);

			// test.jspに転送
			request.getRequestDispatcher("/WEB-INF/jsp/test.jsp").forward(request, response);
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("ThreadDoPost");

		// ThreadMakeServletから送られたthreadIdを取得
		Integer threadId = (Integer) request.getSession().getAttribute("threadId");
		request.getSession().setAttribute("threadId", null);
		System.out.println(threadId);
		// 必要な処理があればここで実行
		if (threadId == null) {
			response.sendRedirect(request.getContextPath());

		} else {
			// リクエストスコープにthreadIdを設定（すでにあるが明示的に再設定）
			request.setAttribute("id", threadId);
			

			// test.jspに転送
			request.getRequestDispatcher("/WEB-INF/jsp/test.jsp").forward(request, response);
		}
	}

}
