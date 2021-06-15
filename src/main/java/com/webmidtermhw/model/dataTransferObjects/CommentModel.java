package com.webmidtermhw.model.dataTransferObjects;

public class CommentModel {
    private String text;
    private Long bookId;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
}
