package com.study.boardvue3.controller;

import com.study.boardvue3.error.Error;
import com.study.boardvue3.exception.BoardNullException;
import com.study.boardvue3.exception.BoardValidationException;
import com.study.boardvue3.response.ErrorResponse;
import com.study.boardvue3.error.BoardValidationError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice(basePackageClasses = {BoardController.class})
public class BoardControllerAdvice {

    /**
     * 정의되지 않은 에러 처리
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse<Error> unknownError(Exception e) {
        e.printStackTrace();
        return ErrorResponse.generateByUnknownError(LocalDateTime.now());
    }

    /**
     * Exception에 담긴 데이터로 ErrorResopnse를 만들어 전송한다.
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BoardValidationException.class)
    public ErrorResponse<Error> handleBoardError(BoardValidationException e) {
        log.error("ERROR: [{}: {}]", e.getError().getField(), e.getError().getMessage());
        return ErrorResponse.generateBoardResponse(e.getTimestamp(), e.getError());
    }
}
