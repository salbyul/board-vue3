package com.study.boardvue3.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BoardDTO {

    private Long boardId;
    private Long categoryId;
    private String categoryName;
    private String writer;
    private String password;
    private String title;
    private String content;
    private Integer views;
    private Integer fileCounts;
    private LocalDateTime generationTimestamp;
    private LocalDateTime modificationTimestamp;
    private String encryptedPassword;
    private List<FileDTO> fileDTOs;
    private List<CommentDTO> commentDTOs;
}
