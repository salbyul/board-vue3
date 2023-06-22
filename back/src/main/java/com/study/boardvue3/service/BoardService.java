package com.study.boardvue3.service;

import com.study.boardvue3.dto.BoardDTO;
import com.study.boardvue3.dto.CategoryDTO;
import com.study.boardvue3.dto.SearchCondition;
import com.study.boardvue3.encoder.Encryptor;
import com.study.boardvue3.exception.BoardValidationException;
import com.study.boardvue3.repository.board.BoardRepository;
import com.study.boardvue3.repository.file.FileRepository;
import com.study.boardvue3.validator.BoardValidationError;
import com.study.boardvue3.validator.BoardValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;

import static com.study.boardvue3.dto.BoardDTO.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final FileRepository fileRepository;
    private final Encryptor encryptor;
    private final BoardValidator boardValidator;

    /**
     * 모든 카테고리 목록을 리턴한다.
     *
     * @return
     */
    public List<CategoryDTO> getCategories() {
        return boardRepository.findAllCategories();
    }

    /**
     * 검색조건에 따른 게시글의 목록을 반환한다.
     *
     * @param condition 검색조건
     * @return
     */
    public List<BoardDTO> getBoards(SearchCondition condition) {
        condition.setOffset(condition.getLimit() * condition.getPage());
        return boardRepository.findSmallBoardByCondition(condition);
    }

    /**
     * 검색조건에 따른 게시글들의 수를 반환한다.
     *
     * @param condition 검색조건
     * @return
     */
    public Integer getCountOfBoards(SearchCondition condition) {
        return boardRepository.countBoardByCondition(condition);
    }

    /**
     * boardId를 primary key로 갖고 있는 boardDTO를 반환한다.
     *
     * @param boardId primary key
     * @return
     */
    public BoardDTO getBoardDetail(Long boardId) {
        BoardDTO boardDetail = boardRepository.findBoardDetail(boardId);
        List<String> fileNames = fileRepository.findFileNamesByBoardId(boardId);
        boardDetail.setFileNames(fileNames);
        return boardDetail;
    }

    /**
     * boardId를 primary key로 갖는 게시글의 views 를 1 더한다.
     *
     * @param boardId 게시글의 primary key
     */
    public void updateViews(Long boardId) {
        boardRepository.addOneToViews(boardId);
    }

    /**
     * boardCreateDTO의 비밀번호를 SHA256 알고리즘으로 암호화한 후, 생성시각을 주입한다.
     * 그 후 Board 테이블에 저장한다.
     *
     * @param boardCreateDTO 저장할 데이터가 담긴 객체
     * @return 저장된 레코드의 primary key
     * @throws NoSuchAlgorithmException
     */
    public Long saveBoard(BoardCreateDTO boardCreateDTO) throws NoSuchAlgorithmException {
        List<BoardValidationError> errors = boardValidator.validateBoardForSaving(boardCreateDTO);
        if (!errors.isEmpty()) {
            throw new BoardValidationException(errors, LocalDateTime.now());
        }

        String encryptedPassword = encryptor.encrypt(boardCreateDTO.getPassword());
        boardCreateDTO.setEncryptedPassword(encryptedPassword);
        boardCreateDTO.setGenerationTimestamp(LocalDateTime.now());
//        boardRepository.save(boardCreateDTO);
        return boardCreateDTO.getBoardId();
    }
}
