package model;

public class Comment {

    private int commentId;
    private int userId;
    private String username;
    private String text;

    public Comment(int commentId, int userId, String username, String text) {
        this.commentId = commentId;
        this.userId = userId;
        this.username = username;
        this.text = text;
    }

    public int getCommentId() { return commentId; }
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getText() { return text; }
}
