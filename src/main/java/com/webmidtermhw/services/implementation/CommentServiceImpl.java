package com.webmidtermhw.services.implementation;

import com.webmidtermhw.dataAccessObjects.RepositoryForComment;
import com.webmidtermhw.model.Entities.Book;
import com.webmidtermhw.model.Entities.Comment;
import com.webmidtermhw.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    RepositoryForComment repositoryForComment;

    @Override
    public Comment addComment(Comment comment) {
        try {
            return repositoryForComment.save(comment);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Comment> findByBook(Book book) {
        return repositoryForComment.findByBookId(book.getId());
    }

    @Override
    public List<Comment> findByBookId(long id) {
        return repositoryForComment.findByBookId(id);
    }

    @Override
    public Comment getById(Long id) {
        return repositoryForComment.findById(id).orElse(null);
    }
}
