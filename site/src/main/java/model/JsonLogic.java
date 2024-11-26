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
		ThreadDto threadDto = new ThreadDto(id, title, time);

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
 * jsonのcontentに新しいcontentを追加
 * @param threadId 追加したいスレッドのId
 * @param contentDto 中身を入力したcontent
 */
	public void addContent(String threadId ,ContentDto contentDto) {
		//配列名
		String arrayName = "contents";

		ObjectMapper mapper = new ObjectMapper();
		String fileName = filePath + "json" + threadId + ".json";
		try {
			JsonNode rootNode = mapper.readTree(new File(fileName));
			ArrayNode arrayNode = (ArrayNode) rootNode.get(arrayName);
			
			
			ObjectNode contentNode = changeContentDtoToObjectNode(contentDto);
			arrayNode.add(contentNode);
			
			mapper.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), rootNode);

			

		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
	
	public ObjectNode changeContentDtoToObjectNode(ContentDto contentDto) {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.valueToTree(contentDto);
		
	}

	/**
	 * Contentの最終IDを取得
	 * @param threadId 取得したいスレッドId
	 * @return contentのID
	 */
	public int getContentId(String threadId) {
		int id = 0 ;
		String arrayName = "contents";

		ObjectMapper mapper = new ObjectMapper();

		try {

			JsonNode rootNode = mapper.readTree(new File(filePath + "json" + threadId + ".json"));
			ArrayNode arrayNode = (ArrayNode) rootNode.get(arrayName);

			if (arrayNode == null) {
				// 配列が存在しない場合、新しく作成する
				arrayNode = mapper.createArrayNode();
				((ObjectNode) rootNode).set(arrayName, arrayNode);
			}

			//contentのID
			 id = arrayNode.size() ;

		} catch (Exception e) {
			// TODO: handle exception
		}

		return id;
	}

}
