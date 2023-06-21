package com.study.boardvue3.repository.comment;

import com.study.boardvue3.dto.CommentDTO;

import java.util.List;

public interface CommentRepository {

    /**
     * boardId를 외래키로 갖는 댓글들의 목록을 반환한다.
     *
     * @param boardId board의 primary key
     * @return
     */
    List<CommentDTO> findCommentDetailByBoardId(Long boardId);

    /**
     * CommentDTO를 DB에 저장한다.
     *
     * @param commentDTO 저장될 데이터가 담긴 객체
     */
    void save(CommentDTO commentDTO);
}
