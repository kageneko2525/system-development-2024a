package model;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;

public class ThreadListLogic {

	
	public String changeJson(HttpServletRequest request) {
		StringBuilder jsonBody = new StringBuilder();
		try {
			BufferedReader reader = request.getReader();
			String line;
			
			while ((line = reader.readLine())!= null) {
				jsonBody.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBody.toString();
	}
	
	public List<String> getTagList(String tagText) {
		List<String> tagList = Arrays.stream(tagText.split("[\\s　]+"))
			    .filter(word -> !word.isEmpty())
			    .collect(Collectors.toList());
		return tagList;
	}
	
	
}
