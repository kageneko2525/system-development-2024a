package servlet;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.JsonLogic;

/**
 * Servlet implementation class ThreadMakeServlet
 * @author x22u004 x22u011
 */
@WebServlet("/ThreadMake")
public class ThreadMakeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ThreadMakeServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// フォームから送信されたスレッド名を取得
		String threadName = request.getParameter("threadName");

		// フォームから送信されたタグ（カンマ区切りの文字列）を取得
		String allTags = request.getParameter("allTags");

		// タグが存在する場合、カンマで分割してリストに変換
		ArrayList<String> tagsList = null;
		if (allTags != null && !allTags.isEmpty()) {
			tagsList = new ArrayList<String>(Arrays.asList(allTags.split(",")));
		}
		
		
		int thread_no = 0;

//		        //tagsListは選択されたタグ、threadNameはスレッド名。これらを使ってJSONファイル作成
//		        //最新のスレッドidを取得する　変数はthred_no
//		        ThreadMakeDao th = new ThreadMakeDao();
//		        thread_no = th.findLatestThread();
//		        thread_no ++;

		//いったんデフォルト値
		thread_no = 1;

		// 現在時刻を取得
		LocalDateTime now = LocalDateTime.now();
		// フォーマットを定義
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		// フォーマット済みの現在時刻を取得
		String formattedTime = now.format(formatter);

		//JSON作成
		JsonLogic jsonLogic = new JsonLogic();

		jsonLogic.createJson(String.valueOf(thread_no), formattedTime, tagsList);

		//本来はここにJSONの保存先のパス thread_urlを設定

		//DB側に登録

//		
//		// データをリクエスト属性にセットして、JSPに渡す
//		request.setAttribute("threadName", threadName);
//		request.setAttribute("tagsList", tagsList);
//		
//		
		//threadIDを渡すためのリクエストスコープ
		request.setAttribute("id", thread_no);
		// テスト用のJSPに転送
		request.getRequestDispatcher("/WEB-INF/jsp/test.jsp").forward(request, response);
		

	}
}