package com.uth.quizclear.model.dto;

public class FeedbackDTO {
    private Long id;
    private String authorName;
    private String avatarLetter;
    private String text;
    private String dateAgo;
    private int likes;

    public FeedbackDTO() {}

    public FeedbackDTO(Long id, String authorName, String avatarLetter, String text, String dateAgo, int likes) {
        this.id = id;
        this.authorName = authorName;
        this.avatarLetter = avatarLetter;
        this.text = text;
        this.dateAgo = dateAgo;
        this.likes = likes;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public String getAvatarLetter() { return avatarLetter; }
    public void setAvatarLetter(String avatarLetter) { this.avatarLetter = avatarLetter; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getDateAgo() { return dateAgo; }
    public void setDateAgo(String dateAgo) { this.dateAgo = dateAgo; }

    public int getLikes() { return likes; }
    public void setLikes(int likes) { this.likes = likes; }
}
