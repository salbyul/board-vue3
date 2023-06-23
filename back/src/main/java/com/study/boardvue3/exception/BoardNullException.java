package com.study.boardvue3.exception;

import com.study.boardvue3.error.GlobalError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class BoardNullException extends RuntimeException {

    private final GlobalError error;
    private final LocalDateTime timestamp;
    private final Long boardId;
}
