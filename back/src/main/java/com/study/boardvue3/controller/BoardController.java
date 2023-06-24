package com.study.boardvue3.controller;

import com.study.boardvue3.dto.BoardDTO;
import com.study.boardvue3.dto.FileDTO;
import com.study.boardvue3.dto.SearchCondition;
import com.study.boardvue3.exception.BoardValidationException;
import com.study.boardvue3.response.APIResponse;
import com.study.boardvue3.response.ResponseType;
import com.study.boardvue3.service.BoardService;
import com.study.boardvue3.service.CommentService;
import com.study.boardvue3.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.study.boardvue3.response.APIResponse.*;

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
    public APIResponse main() {
        return generateResponse()
                .setResult(boardService.getCategories())
                .setTimestamp(LocalDateTime.now());
    }

    /**
     * SearchCondition에 기반한 게시글 목록을 반환한다.
     *
     * @param condition 검색조건
     * @return
     */
    @GetMapping("/board/boards")
    public APIResponse getBoards(SearchCondition condition) {
        return generateResponse()
                .setResult(boardService.getBoards(condition))
                .setTimestamp(LocalDateTime.now());
    }

    /**
     * SearchCondition에 기반한 게시글들의 수를 반환한다.
     *
     * @param condition 검색조건
     * @return
     */
    @GetMapping("/board/counts")
    public APIResponse getCounts(SearchCondition condition) {
        return generateResponse()
                .setResult(boardService.getCountOfBoards(condition))
                .setTimestamp(LocalDateTime.now());
    }

    /**
     * boardId를 받아 해당 게시글의 정보를 boardDTO에 담아 반환한다.
     *
     * @param boardId primary key
     * @return
     */
    @GetMapping("/board/detail/{id}")
    public APIResponse getBoardDetail(@PathVariable("id") Long boardId) {
        BoardDTO boardDetail = boardService.getBoardDetail(boardId);
        if (boardDetail == null) throw new BoardValidationException(ResponseType.BOARD_NULL, LocalDateTime.now());
        boardDetail.setFileDTOs(fileService.getFileNames(boardId));
        boardDetail.setCommentDTOs(commentService.getCommentDTOs(boardId));

        boardService.updateViews(boardId);

        return generateResponse()
                .setResult(boardDetail)
                .setTimestamp(LocalDateTime.now());
    }

    /**
     * boardCreateDTO에 있는 데이터를 기반으로 Board 레코드를, files에 있는 데이터를 기반으로 File 레코드를 생성한 후 파일을 로컬에 저장한다.
     * 생성된 게시글의 primary key를 반환한다.
     *
     * @param boardDTO 사용자가 입력한 게시글의 데이터가 담겨있는 객체
     * @param files    사용자가 게시글에 업로드하고자 하는 파일 목록
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    @PostMapping("/board/create")
    public APIResponse createBoard(@RequestPart BoardDTO boardDTO, @RequestPart(required = false) List<MultipartFile> files) throws NoSuchAlgorithmException, IOException {
        Long boardId = boardService.saveBoard(boardDTO, files);
        fileService.saveFiles(files, boardId);
        return generateResponse()
                .setResult(boardId)
                .setTimestamp(LocalDateTime.now());
    }

    /**
     * boardId를 primary key로 갖고 있는 Board 레코드를 제거한다.
     *
     * @param boardId 삭제하고자 하는 게시글의 primary key
     * @return
     */
    @DeleteMapping("/board/delete/{id}")
    public APIResponse deleteBoard(@PathVariable("id") Long boardId, String password) throws NoSuchAlgorithmException {
        boardService.verifyPassword(password, boardId);
        boardService.delete(boardId);
        return generateResponse()
                .setTimestamp(LocalDateTime.now());
    }

    /**
     * boardId를 primary key로 갖고 있는 게시글의 비밀번호와 password가 일치한지 확인한다.
     *
     * @param boardId  게시글의 primary key
     * @param password 확인될 password
     * @return
     */
    @GetMapping("/board/password/{id}")
    public APIResponse checkPassword(@PathVariable("id") Long boardId, String password) throws NoSuchAlgorithmException {
        boardService.verifyPassword(password, boardId);
        return generateResponse()
                .setTimestamp(LocalDateTime.now());
    }

    /**
     * 수정될 게시글의 데이터를 전송한다.
     *
     * @param boardId 수정될 게시글의 primary key
     * @return
     */
    @GetMapping("/board/modify/{id}")
    public APIResponse getDetailForModify(@PathVariable("id") Long boardId) {
        BoardDTO boardDetail = boardService.getBoardDetail(boardId);
        if (boardDetail == null) throw new BoardValidationException(ResponseType.BOARD_NULL, LocalDateTime.now());
        boardDetail.setFileDTOs(fileService.getFileNames(boardId));

        return generateResponse()
                .setResult(boardDetail)
                .setTimestamp(LocalDateTime.now());
    }

    /**
     * boardId를 primary key로 갖는 게시글을 boardDTO에 담긴 데이터로 수정한다.
     * 지워진 파일과 새로 업로드된 파일들을 로컬에 저장하고, DB에 반영한다.
     *
     * @param boardId  수정될 게시글의 primary key
     * @param boardDTO 수정된 게시글의 데이터가 담긴 객체
     * @param files    새로 업로드된 파일들
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    @PutMapping("/board/modify/{id}")
    public APIResponse modifyBoard(@PathVariable("id") Long boardId, @RequestPart BoardDTO boardDTO, @RequestPart(required = false) List<MultipartFile> files) throws NoSuchAlgorithmException, IOException {
        boardService.verifyPassword(boardDTO.getPassword(), boardId);

        boardService.modify(boardId, boardDTO, files);

        List<String> realNames = boardDTO.getFileDTOs().stream().map(FileDTO::getRealName).collect(Collectors.toList());
        fileService.removeFileByModification(boardId, realNames);
        fileService.saveFiles(files, boardId);
        return generateResponse()
                .setTimestamp(LocalDateTime.now());
    }
}
