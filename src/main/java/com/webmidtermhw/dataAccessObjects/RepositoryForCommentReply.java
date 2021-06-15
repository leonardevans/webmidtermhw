package com.webmidtermhw.dataAccessObjects;

import com.webmidtermhw.model.Entities.Comment;
import com.webmidtermhw.model.Entities.CommentReply;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositoryForCommentReply extends MongoRepository<CommentReply, Long> {
    List<CommentReply> findByComment(Comment comment);
}
