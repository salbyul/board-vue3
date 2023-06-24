package com.study.boardvue3.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDTO {

    private Long commentId;
    private String writer;
    private String content;
    private Long boardId;
    private LocalDateTime generationTimestamp;
}
