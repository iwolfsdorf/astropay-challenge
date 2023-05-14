package com.astropay.api.controllers;

import com.astropay.api.entities.Post;
import com.astropay.api.entities.Comment;
import com.astropay.api.exceptions.PostException;
import com.astropay.api.services.PostService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/posts")
    public ResponseEntity<String> savePost(@RequestBody Post post) {
        try {
            postService.savePost(post);
            return ResponseEntity.ok("Post saved successfully");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/posts")
    public ResponseEntity<Page<Post>> getPosts(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        try {
            Page<Post> postCalls = postService.getPosts(page, size);
            return ResponseEntity.ok(postCalls);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/postsTitle")
    public ResponseEntity<List<Post>> getPostsWithTitle(@RequestParam String containTitle) {
        try {
            List<Post> posts = postService.getPostsWithTitle(containTitle);
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/posts/{post_id}")
    public ResponseEntity<Post> getPost(@PathVariable(value = "post_id") Long postId) {
        try {
            Post post = postService.getPost(postId);
            return ResponseEntity.ok(post);
        } catch (PostException e){
            throw new ResponseStatusException(e.getHttpStatus());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}