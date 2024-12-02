package model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ThreadDto {
	public ThreadDto() {
		contentList = new ArrayList<ContentDto>();
	}

	public ThreadDto(String id, String title, Timestamp createTime) {
		this.id = id;
		this.title = title;
		//this.createTime = createTime;
		contentList = new ArrayList<ContentDto>();
		tags = new ArrayList<String>();
	}

	public ThreadDto(String id, String title, ArrayList<String> tagsList, Timestamp createTime) {
		this.id = id;
		this.title = title;
		//this.createTime = createTime;
		contentList = new ArrayList<ContentDto>();
		tags = tagsList;
	}

	private String id;
	private String title;
	private List<String> tags;
	private Timestamp createTime;
	private List<ContentDto> contentList;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String name) {
		this.title = name;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public void addTags(String tag) {
		this.tags.add(tag);
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public List<ContentDto> getContents() {
		return contentList;
	}

	public void setContents(List<ContentDto> contentList) {
		this.contentList = contentList;
	}

	public void addContents(ContentDto content) {
		this.contentList.add(content);
	}

	public void printThread() {
		System.out.println("id:" + id);
		System.out.println("title:" + title);
		tags.forEach(s -> System.out.println(s));
		System.out.println("createTime:" + createTime);
		contentList.forEach(s -> s.printContent());

	}
}