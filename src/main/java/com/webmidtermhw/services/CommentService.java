package com.webmidtermhw.services;

import com.webmidtermhw.model.Entities.Book;
import com.webmidtermhw.model.Entities.Comment;

import java.util.List;

public interface CommentService {
    Comment addComment(Comment comment);
    List<Comment> findByBook(Book book);
    List<Comment> findByBookId(long id);
    Comment getById(Long id);
}
