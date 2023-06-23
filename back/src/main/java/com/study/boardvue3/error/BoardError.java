package com.study.boardvue3.error;

import com.study.boardvue3.response.ResponseType;
import lombok.Getter;

@Getter
public class BoardError extends GlobalError {

    public BoardError(ResponseType responseType) {
        super(responseType);
    }
}
