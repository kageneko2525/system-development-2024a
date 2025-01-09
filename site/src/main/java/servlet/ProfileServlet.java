package servlet;

import java.io.IOException;

import dao.ProfileDao;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Profile;

@WebServlet("/Profile") // エンドポイント
public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // クエリパラメータからユーザーIDを取得
        String userId = request.getParameter("id");

        if (userId == null || userId.isEmpty()) {
            // ユーザーIDが指定されていない場合、エラーページにフォワード
            request.setAttribute("errorMessage", "ユーザーIDが指定されていません。");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/Profile.jsp");
            dispatcher.forward(request, response);
            return;
        }

        try {
            // DAOを使ってデータベースからプロフィール情報を取得
            ProfileDao profileDao = new ProfileDao();
            Profile profile = profileDao.getUserById(userId);

            if (profile != null) {
                // プロフィールデータをリクエストスコープに格納
                request.setAttribute("profile", profile);

                // JSPにフォワード
                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/Profile.jsp");
                dispatcher.forward(request, response);
            } else {
                // ユーザーが見つからなかった場合
                request.setAttribute("errorMessage", "指定されたユーザーは存在しません。");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/Profile.jsp");
                dispatcher.forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // サーバーエラーが発生した場合
            request.setAttribute("errorMessage", "サーバーエラーが発生しました。");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/Profile.jsp");
            dispatcher.forward(request, response);
        }
    }
}