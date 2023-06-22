package com.study.boardvue3.validator;

import com.study.boardvue3.dto.BoardDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.study.boardvue3.response.ResponseType.*;

public class BoardValidator {

    private final static Pattern passwordPattern = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{4,16}$");

    public List<BoardValidationError> validateBoardForSaving(BoardDTO boardDTO) {
        List<BoardValidationError> errors = new ArrayList<>();
        validateCategory(boardDTO.getCategoryId(), errors);
        validateTitle(boardDTO.getTitle(), errors);
        validateWriter(boardDTO.getWriter(), errors);
        validatePassword(boardDTO.getPassword(), errors);
        validateContent(boardDTO.getContent(), errors);
        return errors;
    }
    private void validateCategory(Long categoryId, List<BoardValidationError> errors) {
        if (categoryId < 1) {
            errors.add(new BoardValidationError(CATEGORY_POSITIVE));
        }
    }
    private void validateWriter(String writer, List<BoardValidationError> errors) {
        if (writer == null) {
            errors.add(new BoardValidationError(WRITER_NOT_NULL));
            return;
        }
        if (writer.length() < 3 || writer.length() > 4) {
            errors.add(new BoardValidationError(WRITER_SIZE));
        }
    }

    private void validateTitle(String title, List<BoardValidationError> errors) {
        if (title == null) {
            errors.add(new BoardValidationError(TITLE_NOT_NULL));
            return;
        }
        if (title.length() < 4 || title.length() > 99) {
            errors.add(new BoardValidationError(TITLE_SIZE));
        }
    }

    private void validatePassword(String password, List<BoardValidationError> errors) {
        if (password == null) {
            errors.add(new BoardValidationError(PASSWORD_NOT_NULL));
            return;
        }
        if (password.length() < 4 || password.length() > 15) {
            errors.add(new BoardValidationError(PASSWORD_SIZE));
        }
        if (!validatePasswordPattern(password)) {
            errors.add(new BoardValidationError(PASSWORD_PATTERN));
        }
    }

    private boolean validatePasswordPattern(String password) {
        Matcher matcher = passwordPattern.matcher(password);
        return matcher.matches();
    }

    private void validateContent(String content, List<BoardValidationError> errors) {
        if (content == null) {
            errors.add(new BoardValidationError(CONTENT_NOT_NULL));
            return;
        }

        if (content.length() < 4 || content.length() > 1999) {
            errors.add(new BoardValidationError(CONTENT_SIZE));
        }
    }
}
