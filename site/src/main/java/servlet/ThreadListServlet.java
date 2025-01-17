package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import dao.ThreadDao;
import dao.ThreadTagDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ThreadDto;
import model.ThreadListLogic;


public class ThreadListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public ThreadListServlet() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.sendRedirect("/site/html/thread_list.html");
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		ThreadListLogic threadListLogic = new ThreadListLogic();
		String jsonBody = threadListLogic.changeJson(request);
		System.out.println("jsonBody : "+jsonBody);
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(jsonBody.toString());
		
		String searchType= jsonNode.get("searchType").asText();
		String searchText = jsonNode.get("searchText").asText();
		String sortType = jsonNode.get("sortType").asText();
		int page = Integer.parseInt(jsonNode.get("page").asText());
		int maxThread = Integer.parseInt(jsonNode.get("maxThread").asText());
		
		
		ThreadDao threadDao = new ThreadDao();
		List<ThreadDto> threadList = new ArrayList<ThreadDto>();
		
		if(searchText.isEmpty() || searchText ==null) {
			threadList = threadDao.getLatestThread(maxThread, (page-1)*maxThread);
		}
		
		ThreadTagDao threadTagDao = new ThreadTagDao();
		threadTagDao.setTag(threadList);
		
		
		mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(threadList);
		
		PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
		
		
	}

}
