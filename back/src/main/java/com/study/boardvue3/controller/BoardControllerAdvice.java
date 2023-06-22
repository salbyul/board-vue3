package com.study.boardvue3.controller;

import com.study.boardvue3.exception.BoardValidationException;
import com.study.boardvue3.response.ErrorResponse;
import com.study.boardvue3.response.ResponseType;
import com.study.boardvue3.validator.BoardValidationError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
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
    public ErrorResponse unknownError(Exception e) {
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
    public ErrorResponse handleBoardError(BoardValidationException e) {
        log.error("error: {}", e.getErrors().stream().map(BoardValidationError::getMessage).collect(Collectors.toList()));
        return ErrorResponse.generate(e.getTimestamp(), e.getErrors());
    }
}
