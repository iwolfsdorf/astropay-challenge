package com.astropay.api.services;

import com.astropay.api.entities.Comment;
import com.astropay.api.entities.Post;
import com.astropay.api.exceptions.PostException;
import com.astropay.api.repositories.CommentRepository;
import com.astropay.api.repositories.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    Logger log = LoggerFactory.getLogger(PostService.class);

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post savePost(Post post) {
        post.setDateTime(LocalDateTime.now());
        return postRepository.save(post);
    }

    public Page<Post> getPosts(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findAllByOrderByDateTimeDesc(pageable);
    }

    public List<Post> getPostsWithTitle(String containTitle) {
        return postRepository.findByTitleContaining(containTitle);
    }

    public Post getPost(Long postId) throws PostException{
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()){
            return postOptional.get();
        } else {
            throw new PostException(HttpStatus.NOT_FOUND);
        }
    }

}