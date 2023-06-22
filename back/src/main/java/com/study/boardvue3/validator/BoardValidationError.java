package com.study.boardvue3.validator;

import com.study.boardvue3.response.ResponseType;
import lombok.Getter;

@Getter
public class BoardValidationError {

    private final String code;
    private final String field;
    private final String message;

    public BoardValidationError(ResponseType responseType) {
        this.code = responseType.getCode();
        this.field = responseType.getField();
        this.message = responseType.getMessage();
    }
}
