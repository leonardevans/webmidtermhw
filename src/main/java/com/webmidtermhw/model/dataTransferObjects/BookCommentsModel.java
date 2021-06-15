package com.webmidtermhw.model.dataTransferObjects;

import com.webmidtermhw.model.Entities.Book;
import com.webmidtermhw.model.Entities.Category;
import com.webmidtermhw.model.Entities.Comment;

import java.util.List;

public class BookCommentsModel {
    private String name;
    private Category category;
    private String author;
    private String published_at;
    private Boolean isAvailable;
    List<Comment> comments;

    public BookCommentsModel(List<Comment> comments, Book book) {
        this.comments = comments;
        this.name = book.getName();
        this.author = book.getAuthor();
        this.category = book.getCategory();
        this.published_at = book.getPublished_at();
        this.isAvailable = book.getAvailable();
        this.comments = comments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublished_at() {
        return published_at;
    }

    public void setPublished_at(String published_at) {
        this.published_at = published_at;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
