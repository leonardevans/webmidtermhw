package com.webmidtermhw.services;

import com.webmidtermhw.model.Entities.Comment;
import com.webmidtermhw.model.Entities.CommentReply;

import java.util.List;

public interface CommentReplyService {
    CommentReply addCommentReply(CommentReply commentReply);
    List<CommentReply> findByComment(Comment comment);
}
