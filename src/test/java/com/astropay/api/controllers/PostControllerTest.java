package com.astropay.api.controllers;

import com.astropay.api.controllers.PostController;
import com.astropay.api.entities.Post;
import com.astropay.api.exceptions.PostException;
import com.astropay.api.services.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.jfr.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.GsonTester;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(controllers = PostController.class)
class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PostService postService;

    @Test
    void whenSavePost_success_expect_ResponseOk() throws Exception {
        mockMvc.perform(post("/posts").contentType(MediaType.APPLICATION_JSON).content("{\n" +
                        "    \"title\" : \"Test Title\",\n" +
                        "    \"message\" : \"Message Title\"\n" +
                        "}"))
            .andExpect(status().is2xxSuccessful())
            .andDo(print());

        verify(postService, atLeast(1)).savePost(any(Post.class));
    }

    @Test
    void whenSavePost_error_expect_InternalServerError() throws Exception {
        when(postService.savePost(any(Post.class))).thenThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));

        mockMvc.perform(post("/posts").contentType(MediaType.APPLICATION_JSON).content("{\n" +
                        "    \"title\" : \"Test Title\",\n" +
                        "    \"message\" : \"Message Title\"\n" +
                        "}"))
                .andExpect(status().is5xxServerError())
                .andExpect(status().is(500))
                .andDo(print());

        verify(postService, atLeast(1)).savePost(any(Post.class));
    }

    @Test
    void whenGetPosts_success_expected_PageWithTwoResults() throws Exception {
        final Post post1 = new Post(LocalDateTime.now(), "Title one", "This is a message");
        final Post post2 = new Post(LocalDateTime.now(), "Title two", "This is another message");
        final Page<Post> expected = new PageImpl<>(Arrays.asList(post1, post2));

        when(postService.getPosts(anyInt(), anyInt())).thenReturn(expected);
        mockMvc.perform(get("/posts?page=0&size=10"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andDo(print());

        verify(postService, atLeast(1)).getPosts(anyInt(), anyInt());
    }

    @Test
    void whenGetPosts_error_expected_InternalServerError() throws Exception {
        when(postService.getPosts(anyInt(), anyInt())).thenThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));

        mockMvc.perform(get("/posts?page=0&size=10"))
                .andExpect(status().is5xxServerError())
                .andDo(print());

        verify(postService, atLeast(1)).getPosts(anyInt(), anyInt());
    }

    @Test
    void whenGetPostsTitle_success_expected_PageWithTwoResults() throws Exception {
        final Post post1 = new Post(LocalDateTime.now(), "Title one", "This is a message");
        final Post post2 = new Post(LocalDateTime.now(), "Title two", "This is another message");
        final List<Post> expected = Arrays.asList(post1, post2);

        when(postService.getPostsWithTitle(anyString())).thenReturn(expected);
        mockMvc.perform(get("/postsTitle?containTitle=Title"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].dateTime").exists())
                .andExpect(jsonPath("$[0].dateTime").isNotEmpty())
                .andExpect(jsonPath("$[0].title").exists())
                .andExpect(jsonPath("$[0].title").isNotEmpty())
                .andExpect(jsonPath("$[0].message").exists())
                .andExpect(jsonPath("$[0].message").isNotEmpty())
                .andExpect(jsonPath("$[1].dateTime").exists())
                .andExpect(jsonPath("$[1].dateTime").isNotEmpty())
                .andExpect(jsonPath("$[1].title").exists())
                .andExpect(jsonPath("$[1].title").isNotEmpty())
                .andExpect(jsonPath("$[1].message").exists())
                .andExpect(jsonPath("$[1].message").isNotEmpty())
                .andDo(print());

        verify(postService, atLeast(1)).getPostsWithTitle(anyString());
    }

    @Test
    void whenGetPostsTitle_error_expected_InternalServerError() throws Exception {
        when(postService.getPostsWithTitle(anyString())).thenThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));

        mockMvc.perform(get("/postsTitle?containTitle=Title"))
                .andExpect(status().is5xxServerError())
                .andDo(print());

        verify(postService, atLeast(1)).getPostsWithTitle(anyString());
    }

    @Test
    void whenGetPost_success_expected_Post() throws Exception {
        final Post expected = new Post(LocalDateTime.now(), "Title one", "This is a message");

        when(postService.getPost(anyLong())).thenReturn(expected);
        mockMvc.perform(get("/posts/1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.dateTime").exists())
                .andExpect(jsonPath("$.dateTime").isNotEmpty())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.title").isNotEmpty())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andDo(print());

        verify(postService, atLeast(1)).getPost(anyLong());
    }

    @Test
    void whenGetPost_error_expected_NotFound() throws Exception {
        when(postService.getPost(anyLong())).thenThrow(new PostException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/posts/1"))
                .andExpect(status().is4xxClientError())
                .andDo(print());

        verify(postService, atLeast(1)).getPost(anyLong());
    }

    @Test
    void whenGetPost_error_expected_InternalServerError() throws Exception {
        when(postService.getPost(anyLong())).thenThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));

        mockMvc.perform(get("/posts/1"))
                .andExpect(status().is5xxServerError())
                .andDo(print());

        verify(postService, atLeast(1)).getPost(anyLong());
    }
}
