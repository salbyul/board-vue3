package com.study.boardvue3.exception;

import com.study.boardvue3.error.BoardValidationError;
import com.study.boardvue3.response.ResponseType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
public class BoardValidationException extends RuntimeException{

    private final BoardValidationError error;
    private final LocalDateTime timestamp;

    public BoardValidationException(ResponseType responseType, LocalDateTime timestamp) {
        this.error = new BoardValidationError(responseType);
        this.timestamp = timestamp;
    }
}
