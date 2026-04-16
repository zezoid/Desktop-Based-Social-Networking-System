package model;

import java.io.Serializable;

public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    private int postId;
    private int userId;
    private String username;
    private String content;

    // NEW (for media support)
    private String mediaPath;   // file path
    private String mediaType;   // IMAGE / VIDEO / null

    /* ================= EXISTING CONSTRUCTOR (DO NOT REMOVE) ================= */
    public Post(int postId, int userId, String username, String content) {
        this.postId = postId;
        this.userId = userId;
        this.username = username;
        this.content = content;
    }

    /* ================= NEW CONSTRUCTOR (MEDIA POSTS) ================= */
    public Post(int postId, int userId, String username,
                String content, String mediaPath, String mediaType) {
        this.postId = postId;
        this.userId = userId;
        this.username = username;
        this.content = content;
        this.mediaPath = mediaPath;
        this.mediaType = mediaType;
    }

    /* ================= GETTERS ================= */
    public int getId() {
        return postId;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public String getMediaType() {
        return mediaType;
    }

    /* ================= HELPERS ================= */
    public boolean hasMedia() {
        return mediaPath != null && mediaType != null;
    }

    public boolean isImage() {
        return "IMAGE".equals(mediaType);
    }

    public boolean isVideo() {
        return "VIDEO".equals(mediaType);
    }
}
