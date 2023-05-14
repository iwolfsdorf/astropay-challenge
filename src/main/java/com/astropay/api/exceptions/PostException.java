package com.astropay.api.exceptions;

import org.springframework.http.HttpStatus;

public class PostException extends Exception {

    private static final long serialVersionUID = 2278100290639382168L;

    private final HttpStatus httpStatus;

    public PostException(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}