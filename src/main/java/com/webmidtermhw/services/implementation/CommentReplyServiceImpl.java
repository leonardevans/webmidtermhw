package com.webmidtermhw.services.implementation;

import com.webmidtermhw.dataAccessObjects.RepositoryForCommentReply;
import com.webmidtermhw.model.Entities.Comment;
import com.webmidtermhw.model.Entities.CommentReply;
import com.webmidtermhw.services.CommentReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentReplyServiceImpl implements CommentReplyService {
    @Autowired
    RepositoryForCommentReply repositoryForCommentReply;

    @Override
    public CommentReply addCommentReply(CommentReply commentReply) {
        try{
            return repositoryForCommentReply.save(commentReply);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<CommentReply> findByComment(Comment comment) {
        return repositoryForCommentReply.findByComment(comment);
    }
}
