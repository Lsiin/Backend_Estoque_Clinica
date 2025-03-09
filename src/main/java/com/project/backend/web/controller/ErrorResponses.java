package com.project.backend.web.controller;

import lombok.Getter;

@Getter
public class ErrorResponses {
    private final String message;

    public ErrorResponses(String message) {
        this.message = message;
    }
}

