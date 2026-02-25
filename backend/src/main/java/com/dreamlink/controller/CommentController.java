package com.dreamlink.controller;

import com.dreamlink.dto.ApiResponse;
import com.dreamlink.dto.comment.CreateCommentRequest;
import com.dreamlink.entity.Comment;
import com.dreamlink.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public ApiResponse<?> createComment(@RequestBody CreateCommentRequest request) {
        try {
            Comment comment = commentService.createComment(request);
            return ApiResponse.success(comment);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/dream/{dreamId}")
    public ApiResponse<?> getDreamComments(@PathVariable Long dreamId) {
        try {
            List<Comment> comments = commentService.getDreamComments(dreamId);
            return ApiResponse.success(comments);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

}
