package com.study.boardvue3.service;

import com.study.boardvue3.dto.BoardDTO;
import com.study.boardvue3.dto.CategoryDTO;
import com.study.boardvue3.dto.SearchCondition;
import com.study.boardvue3.encoder.Encryptor;
import com.study.boardvue3.exception.BoardValidationException;
import com.study.boardvue3.repository.board.BoardRepository;
import com.study.boardvue3.repository.file.FileRepository;
import com.study.boardvue3.response.ResponseType;
import com.study.boardvue3.validator.BoardValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
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
        return boardRepository.findBoardDetail(boardId);
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
     * @param boardDTO 저장할 데이터가 담긴 객체
     * @return 저장된 레코드의 primary key
     * @throws NoSuchAlgorithmException
     */
    public Long saveBoard(BoardDTO boardDTO, List<MultipartFile> files) throws NoSuchAlgorithmException {
        boardValidator.validateForSaving(boardDTO, files);

        String encryptedPassword = encryptor.encrypt(boardDTO.getPassword());
        boardDTO.setEncryptedPassword(encryptedPassword);
        boardDTO.setGenerationTimestamp(LocalDateTime.now());
        boardRepository.save(boardDTO);
        return boardDTO.getBoardId();
    }

    /**
     * boardId를 primary key로 갖고 있는 게시글의 비밀번호와 password가 같은지 확인합니다.
     *
     * @param password 확인할 비밀번호
     * @param boardId  게시글의 primary key
     * @return
     * @throws NoSuchAlgorithmException
     */
    public void verifyPassword(String password, Long boardId) throws NoSuchAlgorithmException {
        if (password == null) throw new BoardValidationException(ResponseType.PASSWORD_NOT_NULL, LocalDateTime.now());
        String encryptedPassword = encryptor.encrypt(password);
        String foundPassword = boardRepository.findPasswordByBoardId(boardId);
        if (!encryptedPassword.equals(foundPassword)) {
            throw new BoardValidationException(ResponseType.PASSWORD_WRONG, LocalDateTime.now());
        }
    }

    /**
     * boardId를 primary key로 갖고 있는 게시글을 DB에서 제거한다.
     *
     * @param boardId 게시글의 primary key
     */
    public void delete(Long boardId) {
        boardRepository.deleteBoardByBoardId(boardId);
    }

    /**
     * 먼저 수정된 객체의 유효성 검사를 진행한다.
     * boardDTO의 modificationTimestamp 속서에 현재 시각을 주입한 후
     * boardId를 primary key로 갖는 게시글의 데이터를 수정한다.
     *
     * @param boardId  수정될 게시글의 primary key
     * @param boardDTO 수정될 게시글의 데이터가 담긴 객체
     * @param files    새로 업로드될 파일들
     */
    public void modify(Long boardId, BoardDTO boardDTO, List<MultipartFile> files) {
        boardValidator.validateForModification(boardDTO, files);
        boardDTO.setModificationTimestamp(LocalDateTime.now());
        boardRepository.update(boardId, boardDTO);
    }
}
