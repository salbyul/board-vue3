package com.study.boardvue3.response;

import lombok.Builder;
import lombok.Getter;

// TODO: 성공시 code, message를 작성하는 것이 과연 맞나?

@Getter
@Builder
public class APIResponse<T> {

    private Integer code;
    private String message;
    private T result;
}
