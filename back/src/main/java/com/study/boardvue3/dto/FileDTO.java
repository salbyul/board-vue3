package com.study.boardvue3.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FileDTO {

    private Long fileId;
    private String fileName;
    private String realName;
    private Long boardId;

    public FileDTO(String fileName, String realName, Long boardId) {
        this.fileName = fileName;
        this.realName = realName;
        this.boardId = boardId;
    }

    public FileDTO(Long fileId, String realName) {
        this.fileId = fileId;
        this.realName = realName;
    }
}
