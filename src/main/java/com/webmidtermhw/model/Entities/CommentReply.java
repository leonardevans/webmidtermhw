package com.webmidtermhw.model.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;

@Document
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "comment"})
public class CommentReply {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String text;

    @DBRef
    @JsonIgnore
    private Comment comment;

    private String username;

    public CommentReply() {
    }

    public CommentReply( String text, Comment comment, User user) {
        this.text = text;
        this.comment = comment;
        this.username = user.getFirstname() + " " + user.getLastname();
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

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
