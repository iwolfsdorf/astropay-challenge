package com.astropay.api.services;

import com.astropay.api.entities.Comment;
import com.astropay.api.entities.Post;
import com.astropay.api.exceptions.CommentException;
import com.astropay.api.exceptions.PostException;
import com.astropay.api.repositories.CommentRepository;
import com.astropay.api.repositories.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    void whenSavePost_success_expect_PostSaved() throws Exception {
        final Post expected = new Post(LocalDateTime.now(), "Title", "Message");

        when(postRepository.save(any(Post.class))).thenReturn(expected);

        Post post = new Post();
        post.setMessage("This is a message");
        post.setTitle("This is a title");

        final Post actual = postService.savePost(post);

        assertEquals(actual.getTitle(), expected.getTitle());
        assertEquals(actual.getMessage(), expected.getMessage());

        verify(postRepository, atLeast(1)).save(any(Post.class));
    }

    @Test
    void whenGetPost_success_expect_PageOfPosts() throws Exception {
        final Post post1 = new Post(LocalDateTime.now(), "Title 1", "Message 1");
        final Post post2 = new Post(LocalDateTime.now(), "Title 2", "Message 2");
        final Post post3 = new Post(LocalDateTime.now(), "Title 3", "Message 3");
        final Page<Post> expected = new PageImpl<>(Arrays.asList(post1, post2, post3));

        when(postRepository.findAllByOrderByDateTimeDesc(any())).thenReturn(expected);

        final Page<Post> actual = postService.getPosts(0, 10);

        assertEquals(actual, expected);
        verify(postRepository, atLeast(1)).findAllByOrderByDateTimeDesc(any());
    }

    @Test
    void whenGetPostsWithTitle_success_expect_ListOfPosts() throws Exception {
        final Post post1 = new Post(LocalDateTime.now(), "Title 1", "Message 1");
        final Post post2 = new Post(LocalDateTime.now(), "Title 2", "Message 2");
        final Post post3 = new Post(LocalDateTime.now(), "Title 3", "Message 3");
        final List<Post> expected = Arrays.asList(post1, post2, post3);

        when(postRepository.findByTitleContaining(anyString())).thenReturn(expected);

        final List<Post> actual = postService.getPostsWithTitle("Title");

        assertEquals(actual, expected);
        verify(postRepository, atLeast(1)).findByTitleContaining(anyString());
    }

    @Test
    void whenGetPost_success_expect_Post() throws Exception {
        final Post expected = new Post(LocalDateTime.now(), "Title", "Message");

        when(postRepository.findById(anyLong())).thenReturn(Optional.of(expected));

        final Post actual = postService.getPost(1L);

        assertEquals(actual, expected);
        verify(postRepository, atLeast(1)).findById(anyLong());
    }

    @Test
    void whenGetPost_error_expect_PostException() throws Exception {
        when(postRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(PostException.class, () -> postService.getPost(1L));

        verify(postRepository, atLeast(1)).findById(anyLong());
    }

}
