package com.study.boardvue3.controller;

import com.study.boardvue3.dto.CommentDTO;
import com.study.boardvue3.response.APIResponse;
import com.study.boardvue3.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    /**
     * commentDTO에 담긴 데이터를 DB에 저장한다.
     *
     * @param commentDTO 저장될 데이터가 담긴 객체
     * @return
     */
    @PostMapping("/comment/save")
    public APIResponse saveComment(@RequestBody CommentDTO commentDTO) {
        commentService.saveCommentDTO(commentDTO);
        return APIResponse.builder()
                .code(201)
                .message("ok")
                .build();
    }
}
