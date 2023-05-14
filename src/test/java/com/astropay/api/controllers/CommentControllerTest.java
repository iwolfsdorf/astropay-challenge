package com.astropay.api.controllers;

import com.astropay.api.entities.Comment;
import com.astropay.api.entities.Post;
import com.astropay.api.exceptions.CommentException;
import com.astropay.api.exceptions.PostException;
import com.astropay.api.services.CommentService;
import com.astropay.api.services.PostService;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CommentController.class)
class CommentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CommentService commentService;

    @Test
    void whenSaveComment_success_expect_ResponseOk() throws Exception {
        mockMvc.perform(post("/posts/1/comments").contentType(MediaType.APPLICATION_JSON).content("{\n" +
                        "    \"message\" : \"Message Title\"\n" +
                        "}"))
            .andExpect(status().is2xxSuccessful())
            .andDo(print());

        verify(commentService, atLeast(1)).saveComment(anyLong(), any(Comment.class));
    }

    @Test
    void whenSaveComment_error_expect_NotFound() throws Exception {
        when(commentService.saveComment(anyLong(), any(Comment.class))).thenThrow(new CommentException(HttpStatus.NOT_FOUND));

        mockMvc.perform(post("/posts/1/comments").contentType(MediaType.APPLICATION_JSON).content("{\n" +
                        "    \"message\" : \"Message Title\"\n" +
                        "}"))
                .andExpect(status().is4xxClientError())
                .andExpect(status().is(404))
                .andDo(print());

        verify(commentService, atLeast(1)).saveComment(anyLong(), any(Comment.class));
    }

    @Test
    void whenSaveComment_error_expect_InternalServerError() throws Exception {
        when(commentService.saveComment(anyLong(), any(Comment.class))).thenThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));

        mockMvc.perform(post("/posts/1/comments").contentType(MediaType.APPLICATION_JSON).content("{\n" +
                        "    \"message\" : \"Message Title\"\n" +
                        "}"))
                .andExpect(status().is5xxServerError())
                .andExpect(status().is(500))
                .andDo(print());

        verify(commentService, atLeast(1)).saveComment(anyLong(), any(Comment.class));
    }

    @Test
    void whenGetCommentsById_success_expected_ListWithTwoComments() throws Exception {
        final Post post = new Post(LocalDateTime.now(), "Title", "This is a message");
        final Comment comment1 = new Comment(post, LocalDateTime.now(), "Comment Message");
        final Comment comment2 = new Comment(post, LocalDateTime.now(), "Comment Message 2");
        final List<Comment> expected = Arrays.asList(comment1, comment2);

        when(commentService.getComments(anyLong())).thenReturn(expected);
        mockMvc.perform(get("/posts/1/comments"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].dateTime").exists())
                .andExpect(jsonPath("$[0].dateTime").isNotEmpty())
                .andExpect(jsonPath("$[0].message").exists())
                .andExpect(jsonPath("$[0].message").isNotEmpty())
                .andDo(print());

        verify(commentService, atLeast(1)).getComments(anyLong());
    }

    @Test
    void whenGetCommentsById_error_expected_InternalServerError() throws Exception {
        when(commentService.getComments(anyLong())).thenThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));

        mockMvc.perform(get("/posts/1/comments"))
                .andExpect(status().is5xxServerError())
                .andDo(print());

        verify(commentService, atLeast(1)).getComments(anyLong());
    }

}
