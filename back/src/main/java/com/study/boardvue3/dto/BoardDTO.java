package com.study.boardvue3.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
    private List<String> fileNames;
    private List<CommentDTO> commentDTOs;

    /**
     * Board를 저장하기 위한 객체
     */
    @Getter
    @Setter
    @ToString
    public static class BoardCreateDTO extends BoardDTO {

        private Long boardId;
        private Long categoryId;
        private String writer;
        private String title;
        private String password;
        private String content;
        private String encryptedPassword;
        private LocalDateTime generationTimestamp;
    }
}
