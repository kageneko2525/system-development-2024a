package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.ThreadDto;


/**
 * 
 *@author x22u004
 */
public class ThreadListDao extends BaseDao {

	/**
	 * IDが新しいThredを10件返す？　未完につき放置しておいてください
	 * @return ThredDto型のリストで返す
	 */
	public List<ThreadDto> LatestThread(int base,int count) {
	   
	    //戻り値となるリスト宣言
	    List<ThreadDto> list = new ArrayList<ThreadDto>();
	    

	    try {
	        this.connect();

	        String sql = "SELECT * "
	                   + "FROM thread "
	                   + "WHERE thread_state = 0 "
	                   + "ORDER BY thread_id ASC "
	                   + "LIMIT ?;";

	        PreparedStatement ps = con.prepareStatement(sql);
	        
	        ps.setInt(1, count);

	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	        	
	        	ThreadDto thread = new ThreadDto();
	        }
	        

	    } catch (Exception e) {
	        e.printStackTrace();

	    } finally {
	        try {
	            this.disConnect();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    return list; // データがなかった場合はデフォルト値1を返す
	}
}