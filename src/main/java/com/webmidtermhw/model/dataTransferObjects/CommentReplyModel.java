package com.webmidtermhw.model.dataTransferObjects;

public class CommentReplyModel {
    private String text;
    private Long commentId;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }
}
