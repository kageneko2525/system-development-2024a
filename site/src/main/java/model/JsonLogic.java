package model;
import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * json生成・追加・取得
 */
public class JsonLogic {
	
	public void createJson(String id, String title ,ArrayList<String> tags) {
		System.out.println("json開始");
		Date date = new Date();
		Timestamp createTime = new Timestamp(date.getTime());
		
		
		ThreadDto threadDto  = new ThreadDto(id, title,createTime);
//		threadDto.addContents(new ContentDto());
		
		
		ObjectMapper mapper = new ObjectMapper();
		
	
		try {
			System.out.println("try入り");
			//mapper.writeValue(new File("I:/git/system-development-2024-Eclipse/site/src/main/webapp/json/"+id+".json"), threadDto);
			
			
			//neko用
			mapper.writeValue(new File("I:/git/system-development-2024a/site/src/main/webapp/json"+id+".json"), threadDto);
			System.out.println("try終わり");
		} catch (Exception e) {
			System.out.println(e);
		}
	
	}
}