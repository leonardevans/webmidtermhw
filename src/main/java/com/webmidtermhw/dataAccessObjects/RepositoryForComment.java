package com.webmidtermhw.dataAccessObjects;

import com.webmidtermhw.model.Entities.Book;
import com.webmidtermhw.model.Entities.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositoryForComment extends MongoRepository<Comment, Long> {
    List<Comment> findByBookId(Long id);
}
