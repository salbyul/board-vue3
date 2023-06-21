package com.study.boardvue3.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileDTO {

    private Long fileId;
    private String fileName;
    private String realName;
    private Long boardId;
}
