package com.study.boardvue3.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseType {

    /*
    CODE Description
    100의 자리 : 도메인
    10의 자리 : 속성
    1의 자리 : 에러 유형
     */

    //    Board
    CATEGORY_POSITIVE("101", "Category", "양수가 아닙니다."),
    CATEGORY_NOT_NULL("102", "Category", "값이 null입니다."),

    WRITER_SIZE("113", "Writer", "길이 규정에 위반합니다."),
    WRITER_NOT_NULL("112", "Writer", "값이 null입니다."),

    TITLE_SIZE("123", "Title", "길이 규정에 위반합니다."),
    TITLE_NOT_NULL("122", "Title", "값이 null입니다."),

    PASSWORD_SIZE("133", "Password", "길이 규정에 위반합니다."),
    PASSWORD_PATTERN("134", "Password", "패턴 규정에 위반합니다."),
    PASSWORD_NOT_NULL("132", "Password", "값이 null입니다."),

    CONTENT_SIZE("143", "Content", "길이 규정에 위반합니다."),
    CONTENT_NOT_NULL("142", "Content", "값이 null입니다."),

    UNKNOWN_ERROR("000", "Unknown", "알 수 없는 에러입니다.");

    private final String code;
    private final String field;
    private final String message;
    }