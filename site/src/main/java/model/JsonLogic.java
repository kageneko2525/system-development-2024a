package model;
import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * json生成・追加・取得
 */
public class JsonLogic {
	
	//jsonの保存場所の指定。各クラスで使用するためクラス変数で宣言。環境に合わせて実行時に変えてもらえると〇
	//"I:/git/system-development-2024a/site/src/main/webapp/json/"　デフォルト値
	private String filePath = "I:/git/system-development-2024a/site/src/main/webapp/json/";
	
	public void createJson(String id, String title, Timestamp time, ArrayList<String> tags) {
		
		//Timestamp→String
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String ConvertedTime = sdf.format(time);
		
		ObjectMapper mapper = new ObjectMapper();
		
		ThreadDto threadDto = new ThreadDto(id, title, ConvertedTime);
		
		try {
			//タグが存在する場合、タグをセット
			if(tags != null) {
				threadDto.setTags(tags);
			}
			
			mapper.writeValue(new File(filePath + "json" + id + ".json"), threadDto);
			
		} catch(Exception e) {
			
			System.out.print(e);
			
		}
	}
}