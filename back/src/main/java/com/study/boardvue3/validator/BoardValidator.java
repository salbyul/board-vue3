package com.study.boardvue3.validator;

import com.study.boardvue3.dto.BoardDTO;
import com.study.boardvue3.exception.BoardValidationException;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.study.boardvue3.response.ResponseType.*;

public class BoardValidator {

    private final static Pattern passwordPattern = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{4,16}$");

    public void validateBoardForSaving(BoardDTO boardDTO) {
        validateCategory(boardDTO.getCategoryId());
        validateTitle(boardDTO.getTitle());
        validateWriter(boardDTO.getWriter());
        validatePassword(boardDTO.getPassword());
        validateContent(boardDTO.getContent());
    }
    private void validateCategory(Long categoryId) {
        if (categoryId < 1) {
            throw new BoardValidationException(CATEGORY_POSITIVE, LocalDateTime.now());
        }
    }
    private void validateWriter(String writer) {
        if (writer == null) {
            throw new BoardValidationException(WRITER_NOT_NULL, LocalDateTime.now());
        }
        if (writer.length() < 3 || writer.length() > 4) {
            throw new BoardValidationException(WRITER_SIZE, LocalDateTime.now());
        }
    }

    private void validateTitle(String title) {
        if (title == null) {
            throw new BoardValidationException(TITLE_NOT_NULL, LocalDateTime.now());
        }
        if (title.length() < 4 || title.length() > 99) {
            throw new BoardValidationException(TITLE_SIZE, LocalDateTime.now());
        }
    }

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

    private boolean validatePasswordPattern(String password) {
        Matcher matcher = passwordPattern.matcher(password);
        return matcher.matches();
    }

    private void validateContent(String content) {
        if (content == null) {
            throw new BoardValidationException(CONTENT_NOT_NULL, LocalDateTime.now());
        }

        if (content.length() < 4 || content.length() > 1999) {
            throw new BoardValidationException(CONTENT_SIZE, LocalDateTime.now());
        }
    }
}
