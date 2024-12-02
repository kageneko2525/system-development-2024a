//ThreadMakeServlet


package servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import dao.ThreadDao;
import dao.ThreadTagDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.JsonLogic;
import model.ThreadDto;

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
		
		
		
		
		int threadNo = 1;

//		 //tagsListは選択されたタグ、threadNameはスレッド名。これらを使ってJSONファイル作成
//		 //最新のスレッドidを取得する　変数はthred_no
		ThreadDao th = new ThreadDao();
		threadNo = th.findLatestThread();
		threadNo++;
		
		//DB登録のためこちら側で時刻取得
		Date date = new Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		
		//JSON作成
		JsonLogic jsonLogic = new JsonLogic();
		jsonLogic.createJson(String.valueOf(threadNo), threadName, timestamp, tagsList);
		
		//threadのurl設定(DB登録のパス)
		//"I:/git/system-development-2024a/site/src/main/webapp/json" デフォルト
		String threadUrl = "I:/git/system-development-2024a/site/src/main/webapp/json"+threadNo+".json";
		
		//threadState 0で生存？
		int threadState = 0;

		//DB側に登録
		//登録項目はthreadNo(スレッドID),threadName(スレッドの名前),threadUrl(スレッドのパス),timestamp(スレッドの生成時間),threadState(スレッドの状態)
		th.newCreateThread(threadNo, threadName, threadUrl, timestamp, threadState);

		
		
		ThreadDto threadDto = new ThreadDto(Integer.toString(threadNo), threadName,tagsList,timestamp);
		
		
		ThreadTagDao threadTagDao = new ThreadTagDao();
		threadTagDao.setTag(threadDto);
		
		//どこにとばせばええですか
		//いったんテストへ飛ばして。そのうちthread.jspにする予定。
		
		
		
//		// データをリクエスト属性にセットして、JSPに渡す
//		request.setAttribute("threadName", threadName);
//		request.setAttribute("tagsList", tagsList);
//		
//		
		//threadIDを渡すためのセッションスコープ
		
		request.getSession().setAttribute("threadId", threadNo);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		
//		 threadServletに送信
		response.sendRedirect(request.getContextPath() + "/ThreadServlet");
		

	}
}