package com.study.boardvue3.service;

import com.study.boardvue3.dto.CommentDTO;
import com.study.boardvue3.repository.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    /**
     * boardId를 외래키로 갖는 댓글들의 목록을 반환한다.
     *
     * @param boardId board의 primary key
     * @return
     */
    public List<CommentDTO> getCommentDTOs(Long boardId) {
        return commentRepository.findCommentDetailByBoardId(boardId);
    }

    /**
     * CommentDTO에 담긴 데이터를 DB에 저장한다.
     *
     * @param commentDTO 데이터가 담긴 객체
     */
    public void saveCommentDTO(CommentDTO commentDTO) {
        commentDTO.setGenerationTimestamp(LocalDateTime.now());
        commentRepository.save(commentDTO);
    }
}
