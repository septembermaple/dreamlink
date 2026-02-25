package com.dreamlink.service.impl;

import com.dreamlink.dto.comment.CreateCommentRequest;
import com.dreamlink.entity.Comment;
import com.dreamlink.repository.CommentRepository;
import com.dreamlink.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Comment createComment(CreateCommentRequest request) {
        Comment comment = new Comment();
        comment.setDreamId(request.getDreamId());
        comment.setUserId(request.getUserId());
        comment.setContent(request.getContent());

        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getDreamComments(Long dreamId) {
        return commentRepository.findByDreamIdOrderByCreatedAtDesc(dreamId);
    }

}
