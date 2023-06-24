package com.study.boardvue3.response;

import com.study.boardvue3.error.Error;
import com.study.boardvue3.error.GlobalError;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse<T extends Error> {

    private final LocalDateTime timestamp;
    private final T error;

    public static ErrorResponse<Error> generateBoardResponse(LocalDateTime timestamp, Error error) {
        return new ErrorResponse<>(timestamp, error);
    }

    public static ErrorResponse<Error> generateByUnknownError(LocalDateTime timestamp) {
        GlobalError error = new GlobalError(ResponseType.UNKNOWN_ERROR);
        return new ErrorResponse<>(timestamp, error);
    }
}
