package com.study.boardvue3.controller;

import com.study.boardvue3.dto.BoardDTO;
import com.study.boardvue3.dto.CategoryDTO;
import com.study.boardvue3.dto.SearchCondition;
import com.study.boardvue3.response.APIResponse;
import com.study.boardvue3.service.BoardService;
import com.study.boardvue3.service.CommentService;
import com.study.boardvue3.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static com.study.boardvue3.dto.BoardDTO.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;
    private final FileService fileService;

    /**
     * 전체 카테고리를 반환한다.
     *
     * @return
     */
    @GetMapping("/categories")
    public APIResponse<List<CategoryDTO>> main() {
        return APIResponse.<List<CategoryDTO>>builder()
                .result(boardService.getCategories())
                .code(200)
                .message("ok")
                .build();
    }

    /**
     * SearchCondition에 기반한 게시글 목록을 반환한다.
     *
     * @param condition 검색조건
     * @return
     */
    @GetMapping("/board/boards")
    public APIResponse<List<BoardDTO>> getBoards(SearchCondition condition) {
        System.out.println("condition = " + condition);
        return APIResponse.<List<BoardDTO>>builder()
                .result(boardService.getBoards(condition))
                .code(200)
                .message("ok")
                .build();
    }

    /**
     * SearchCondition에 기반한 게시글들의 수를 반환한다.
     *
     * @param condition 검색조건
     * @return
     */
    @GetMapping("/board/counts")
    public APIResponse<Integer> getCounts(SearchCondition condition) {
        return APIResponse.<Integer>builder()
                .result(boardService.getCountOfBoards(condition))
                .code(200)
                .message("ok")
                .build();
    }

    /**
     * boardId를 받아 해당 게시글의 정보를 boardDTO에 담아 반환한다.
     *
     * @param boardId primary key
     * @return
     */
    @GetMapping("/board/detail/{id}")
    public APIResponse<BoardDTO> getBoardDetail(@PathVariable("id") Long boardId) {
        BoardDTO boardDetail = boardService.getBoardDetail(boardId);
        boardDetail.setCommentDTOs(commentService.getCommentDTOs(boardId));
        boardService.updateViews(boardId);
        return APIResponse.<BoardDTO>builder()
                .result(boardDetail)
                .code(200)
                .message("ok")
                .build();
    }

//    TODO: result에 boardId 하나 넘기는 거 불편
    @PostMapping("/board/create")
    public APIResponse createBoard(@RequestPart("boardDTO") BoardCreateDTO boardCreateDTO, @RequestPart(required = false) List<MultipartFile> files) throws NoSuchAlgorithmException, IOException {
        Long boardId = boardService.saveBoard(boardCreateDTO);
        fileService.saveFiles(files, boardId);
        return APIResponse.builder()
                .code(201)
                .message("ok")
                .result(boardId)
                .build();
    }
}
