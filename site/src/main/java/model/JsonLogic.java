package model;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;


/**
 * json生成・追加・取得
 */
public class JsonLogic {

	//jsonの保存場所の指定。各クラスで使用するためクラス変数で宣言。環境に合わせて実行時に変えてもらえると〇
	//"I:/git/system-development-2024a/site/src/main/webapp/json/"　デフォルト値
	private String filePath = "I:/git/system-development-2024a/site/src/main/webapp/json/";

	//jsonの生成
	public void createJson(String id, String title, Timestamp time, ArrayList<String> tags) {

		//Timestamp→String
		//いったんなしで
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//String ConvertedTime = sdf.format(time);

		ObjectMapper mapper = new ObjectMapper();

		//ThreadDto threadDto = new ThreadDto(id, title, ConvertedTime);
		ThreadDto threadDto = new ThreadDto(id, title,time);

		try {
			//タグが存在する場合、タグをセット
			if (tags != null) {
				threadDto.setTags(tags);
			}

			mapper.writeValue(new File(filePath + "json" + id + ".json"), threadDto);

		} catch (Exception e) {

			System.out.print(e);

		}
	}

	
	
	
	
	/**
	 * Contentsに保存　optionない用
	 * @param threadId スレッドコンテントID
	 * @param content 内容
	 * @param timestamp 登校時間
	 * @author x22u011
	 */
	public void addContent(String threadId, String content, Timestamp timestamp) {
		//配列名
		String arrayName = "contents";
		
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			
			JsonNode rootNode = mapper.readTree(new File(filePath + "json" + id + ".json"));
			ArrayNode arrayNode = (ArrayNode)rootNode.get(arrayName);
			
            if (arrayNode == null) {
                // 配列が存在しない場合、新しく作成する
                arrayNode = mapper.createArrayNode();
                ((ObjectNode) rootNode).set(arrayName, arrayNode);
            }
            
            //contentのID
            int id = arrayNode.size()+1;
            ObjectNode newcontent = mapper.createObjectNode();
			newcontent.put("id",id);
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
