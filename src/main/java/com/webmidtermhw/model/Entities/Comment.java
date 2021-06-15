package com.webmidtermhw.model.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Document
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String text;

    @JsonIgnore
    private long bookId;

    @DBRef(lazy = false)
    private List<CommentReply> replies = new ArrayList<>();

    public Comment() {
    }

    public Comment( String text, Book book, User user) {
        this.text = text;
        this.bookId = book.getId();
        this.username = user.getFirstname() + " " +  user.getLastname();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<CommentReply> getReplies() {
        return replies;
    }

    public void setReplies(List<CommentReply> replies) {
        this.replies = replies;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
