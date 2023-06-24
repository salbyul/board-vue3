package com.study.boardvue3.validator;

import com.study.boardvue3.dto.BoardDTO;
import com.study.boardvue3.exception.BoardValidationException;
import com.study.boardvue3.response.ResponseType;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.study.boardvue3.response.ResponseType.*;

public class BoardValidator {

    private final static Pattern passwordPattern = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{4,16}$");

    /**
     * 게시글의 저장을 위한 유효성 검사 메소드
     * @param boardDTO 유효성 검사 대상 객체
     * @param files 업로드될 파일 목록
     */
    public void validateForSaving(BoardDTO boardDTO, List<MultipartFile> files) {
        validateCategory(boardDTO.getCategoryId());
        validateTitle(boardDTO.getTitle());
        validateWriter(boardDTO.getWriter());
        validatePassword(boardDTO.getPassword());
        validateContent(boardDTO.getContent());
    }

    /**
     * 게시글의 수정을 위한 유효성 검사 메소드
     * @param boardDTO 유효성 검사 대상 객체
     * @param files 업로드될 파일 목록
     */
    public void validateForModification(BoardDTO boardDTO, List<MultipartFile> files) {
        validateTitle(boardDTO.getTitle());
        validateWriter(boardDTO.getWriter());
        validateContent(boardDTO.getContent());
        validateNumbersOfFile(boardDTO, files);
    }

    /**
     * 파일 갯수의 유효성 검사를 합니다.
     *
     * @param boardDTO 수정된 게시글 데이터가 담긴 객체
     * @param files    새로 업로드한 파일
     */
    private void validateNumbersOfFile(BoardDTO boardDTO, List<MultipartFile> files) {
        if (files != null) {
            int fileCounts = boardDTO.getFileDTOs().size() + files.size();
            System.out.println("기존 파일: " + boardDTO.getFileDTOs());
            if (fileCounts > 3) throw new BoardValidationException(ResponseType.FILE_COUNT, LocalDateTime.now());
        }
    }

    /**
     * categoryId의 유효성 검사 메소드
     *
     * @param categoryId category의 primary key
     */
    private void validateCategory(Long categoryId) {
        if (categoryId < 1) {
            throw new BoardValidationException(CATEGORY_POSITIVE, LocalDateTime.now());
        }
    }

    /**
     * writer의 유효성 검사 메소드
     *
     * @param writer Board의 writer 속성
     */
    private void validateWriter(String writer) {
        if (writer == null) {
            throw new BoardValidationException(WRITER_NOT_NULL, LocalDateTime.now());
        }
        if (writer.length() < 3 || writer.length() > 4) {
            throw new BoardValidationException(WRITER_SIZE, LocalDateTime.now());
        }
    }

    /**
     * title의 유효성 검사 메소드
     *
     * @param title Board의 title 속성
     */
    private void validateTitle(String title) {
        if (title == null) {
            throw new BoardValidationException(TITLE_NOT_NULL, LocalDateTime.now());
        }
        if (title.length() < 4 || title.length() > 99) {
            throw new BoardValidationException(TITLE_SIZE, LocalDateTime.now());
        }
    }

    /**
     * passwor의 유효성 검사 메소드
     *
     * @param password Board의 password 속성
     */
    private void validatePassword(String password) {
        if (password == null) {
            throw new BoardValidationException(PASSWORD_NOT_NULL, LocalDateTime.now());
        }
        if (password.length() < 4 || password.length() > 15) {
            throw new BoardValidationException(PASSWORD_SIZE, LocalDateTime.now());
        }
        if (!validatePasswordPattern(password)) {
            throw new BoardValidationException(PASSWORD_PATTERN, LocalDateTime.now());
        }
    }

    /**
     * password의 패턴 유효성 검사 메소드
     *
     * @param password Board의 password 속성
     * @return
     */
    private boolean validatePasswordPattern(String password) {
        Matcher matcher = passwordPattern.matcher(password);
        return matcher.matches();
    }

    /**
     * content의 유효성 검사 메소드
     *
     * @param content Board의 content 속성
     */
    private void validateContent(String content) {
        if (content == null) {
            throw new BoardValidationException(CONTENT_NOT_NULL, LocalDateTime.now());
        }

        if (content.length() < 4 || content.length() > 1999) {
            throw new BoardValidationException(CONTENT_SIZE, LocalDateTime.now());
        }
    }
}
