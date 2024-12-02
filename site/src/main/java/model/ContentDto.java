package model;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
public class ContentDto {
    public ContentDto(){
        optionList = new ArrayList<OptionDto>();        
    }
    public ContentDto(int id , String userId ,String userName ,Timestamp postingTime,String content){
        this.id =id;
        this.userId = userId;
        this.userName = userName;
        this.postingTime = postingTime;
        this.content = content ;
        optionList = new ArrayList<OptionDto>();        
    }
    
    public ContentDto(int id , String userId ,String userName ,Timestamp postingTime,String content,ArrayList<OptionDto> optionList){
        this.id =id;
        this.userId = userId;
        this.userName = userName;
        this.postingTime = postingTime;
        this.content = content ;
        this.optionList = optionList;        
    }
    
    
    
    private int id;
    private String userId;
    private String userName;
    private Timestamp postingTime ;
    private String content;
    
    @JsonProperty("options")
    private List<OptionDto> optionList ;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public Timestamp getPostingTime() {
        return postingTime;
    }
    public void setPostingTime(Timestamp postingTime) {
        this.postingTime = postingTime;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    
    public List<OptionDto> getOptionList() {
        return optionList;
    }
        
    public void setTagList(List<OptionDto> optionList) {
        this.optionList = optionList;
    }
    public void printContent(){
        System.out.println("id:"+id);
        System.out.println("userId:"+userId);
        System.out.println("userName:"+userName);
        System.out.println("content:"+content);
        optionList.forEach(s->s.printTag());
        
    }
}