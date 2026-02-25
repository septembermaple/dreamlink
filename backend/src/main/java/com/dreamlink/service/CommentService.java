package com.dreamlink.service;

import com.dreamlink.dto.comment.CreateCommentRequest;
import com.dreamlink.entity.Comment;

import java.util.List;

public interface CommentService {

    Comment createComment(CreateCommentRequest request);

    List<Comment> getDreamComments(Long dreamId);

}
