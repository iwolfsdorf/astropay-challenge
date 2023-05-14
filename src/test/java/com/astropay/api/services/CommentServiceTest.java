package com.astropay.api.services;

import com.astropay.api.entities.Comment;
import com.astropay.api.entities.Post;
import com.astropay.api.exceptions.CommentException;
import com.astropay.api.repositories.CommentRepository;
import com.astropay.api.repositories.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private PostRepository postRepository;
    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;


    @Test
    void whenSaveComment_success_expect_CommentSaved() throws Exception {
        final Post post = new Post(LocalDateTime.now(), "Title", "Message");
        final Comment expected = new Comment(post, LocalDateTime.now(), "This is a message");

        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(commentRepository.save(any(Comment.class))).thenReturn(expected);

        Comment comment = new Comment();
        comment.setMessage("This is a message");

        final Comment actual = commentService.saveComment(1L, comment);

        assertEquals(actual.getPost().getTitle(), expected.getPost().getTitle());
        assertEquals(actual.getPost().getMessage(), expected.getPost().getMessage());
        assertEquals(actual.getMessage(), expected.getMessage());

        verify(postRepository, atLeast(1)).findById(anyLong());
        verify(commentRepository, atLeast(1)).save(any(Comment.class));
    }

    @Test
    void whenSaveComment_error_expect_CommentException() throws Exception {
        when(postRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CommentException.class, () -> commentService.saveComment(1L, new Comment()));

        verify(postRepository, atLeast(1)).findById(anyLong());
    }

    @Test
    void whenGetComments_success_expect_ListOfComments() throws Exception {
        final Post post = new Post(LocalDateTime.now(), "Title", "Message");
        final Comment comment1 = new Comment(post, LocalDateTime.now(), "This is a message 1");
        final Comment comment2 = new Comment(post, LocalDateTime.now(), "This is a message 2");
        final Comment comment3 = new Comment(post, LocalDateTime.now(), "This is a message 3");
        final List<Comment> expected = Arrays.asList(comment1, comment2, comment3);

        when(commentRepository.findAllByPostIdOrderByDateTimeDesc(anyLong())).thenReturn(expected);

        final List<Comment> actual = commentService.getComments(1L);

        assertEquals(actual, expected);

        verify(commentRepository, atLeast(1)).findAllByPostIdOrderByDateTimeDesc(anyLong());
    }

}
