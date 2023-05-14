package com.astropay.api.controllers;

import com.astropay.api.entities.Comment;
import com.astropay.api.exceptions.CommentException;
import com.astropay.api.exceptions.PostException;
import com.astropay.api.services.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/posts/{post_id}/comments")
    public ResponseEntity<String> saveComment(@PathVariable(value = "post_id") Long postId, @RequestBody Comment comment) {
        try {
            commentService.saveComment(postId, comment);
            return ResponseEntity.ok("Added comment in post with id " + postId);
        } catch (CommentException e) {
            throw new ResponseStatusException(e.getHttpStatus());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/posts/{post_id}/comments")
    public ResponseEntity<List<Comment>> getCommentsByPostId(@PathVariable(value = "post_id") Long postId) {
        try {
            List<Comment> comments = commentService.getComments(postId);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}