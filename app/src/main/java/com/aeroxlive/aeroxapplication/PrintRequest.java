package com.aeroxlive.aeroxapplication;

public class PrintRequest {
    private String userId;
    private String documentName;
    private long timestamp;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    int position;

    public String getUsersName() {
        return usersName;
    }

    public void setUsersName(String usersName) {
        this.usersName = usersName;
    }

    public String getUserImage() {
        return UserImage;
    }

    public void setUserImage(String userImage) {
        UserImage = userImage;
    }

    private String usersName;

    private String UserImage;

    public PrintRequest() {
        // Required empty constructor for Firebase Realtime Database
    }

    public PrintRequest(String userId, String documentName, long timestamp, String UsersName,String userImage) {
        this.userId = userId;
        this.documentName = documentName;
        this.timestamp = timestamp;
        this.usersName = UsersName;
        this.UserImage = userImage;

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
