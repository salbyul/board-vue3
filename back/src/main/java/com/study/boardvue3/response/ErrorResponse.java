package com.study.boardvue3.response;

import com.study.boardvue3.validator.BoardValidationError;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {

    private final LocalDateTime timestamp;
    private final List<BoardValidationError> errors;

    public static ErrorResponse generate(LocalDateTime timestamp, List<BoardValidationError> errors) {
        return new ErrorResponse(timestamp, errors);
    }

    public static ErrorResponse generateByUnknownError(LocalDateTime timestamp) {
        List<BoardValidationError> errors = new ArrayList<>();
        errors.add(new BoardValidationError(ResponseType.UNKNOWN_ERROR));
        return new ErrorResponse(timestamp, errors);
    }
}
