package com.astropay.api.services;

import com.astropay.api.entities.Comment;
import com.astropay.api.entities.Post;
import com.astropay.api.exceptions.CommentException;
import com.astropay.api.repositories.CommentRepository;
import com.astropay.api.repositories.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    Logger log = LoggerFactory.getLogger(CommentService.class);

    public CommentService(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public Comment saveComment(Long postId, Comment comment) throws CommentException {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()){
            Post post = postOptional.get();
            comment.setPost(post);
            comment.setDateTime(LocalDateTime.now());
            return commentRepository.save(comment);
        } else {
            throw new CommentException(HttpStatus.NOT_FOUND);
        }
    }

    public List<Comment> getComments(Long postId) {
        return commentRepository.findAllByPostIdOrderByDateTimeDesc(postId);
    }

}