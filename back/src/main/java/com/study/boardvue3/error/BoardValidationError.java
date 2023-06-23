package com.study.boardvue3.error;

import com.study.boardvue3.response.ResponseType;
import lombok.Getter;

@Getter
public class BoardValidationError extends BoardError {

    private final String field;

    public BoardValidationError(ResponseType responseType) {
        super(responseType);
        this.field = responseType.getField();
    }
}
