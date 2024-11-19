package model;

public class Profile {
    private String userId;
    private String userName;
    private String userIcon;
    private String userBirth;
    private String userComment;
    private int publicName;
    private int publicBirth;
    private int publicDisplay;

    // GetterとSetter
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

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getUserBirth() {
        return userBirth;
    }

    public void setUserBirth(String userBirth) {
        this.userBirth = userBirth;
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    public int getPublicName() {
        return publicName;
    }

    public void setPublicName(int publicName) {
        this.publicName = publicName;
    }

    public int getPublicBirth() {
        return publicBirth;
    }

    public void setPublicBirth(int publicBirth) {
        this.publicBirth = publicBirth;
    }

    public int getPublicDisplay() {
        return publicDisplay;
    }

    public void setPublicDisplay(int publicDisplay) {
        this.publicDisplay = publicDisplay;
    }
}
