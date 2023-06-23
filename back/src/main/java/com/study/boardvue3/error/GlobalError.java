package com.study.boardvue3.error;

import com.study.boardvue3.response.ResponseType;
import lombok.Getter;

@Getter
public class GlobalError implements Error {

    private final String code;
    private final String message;

    public GlobalError(ResponseType responseType) {
        this.code = responseType.getCode();
        this.message = responseType.getMessage();
    }
}
