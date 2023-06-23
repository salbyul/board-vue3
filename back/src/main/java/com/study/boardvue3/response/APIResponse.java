package com.study.boardvue3.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class APIResponse {

    private LocalDateTime timestamp;
    private Object result;

    public APIResponse setResult(Object object) {
        result = object;
        return this;
    }

    public APIResponse setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public static APIResponse generateResponse() {
        return new APIResponse();
    }
}
