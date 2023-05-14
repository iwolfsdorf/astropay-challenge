package com.astropay.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="post_id")
    @JsonIgnore
    private Post post;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Column(name = "message")
    private String message;

    public Comment() {

    }

    public Comment(Post post, LocalDateTime dateTime, String message) {
        this.post = post;
        this.dateTime = dateTime;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void setDateTime(LocalDateTime date) {
        this.dateTime = date;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
