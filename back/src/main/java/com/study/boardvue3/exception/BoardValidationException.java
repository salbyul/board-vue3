package com.study.boardvue3.exception;

import com.study.boardvue3.validator.BoardValidationError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class BoardValidationException extends RuntimeException{

    private final List<BoardValidationError> errors;
    private final LocalDateTime timestamp;
}
