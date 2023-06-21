package com.study.boardvue3.repository.board;

import com.study.boardvue3.dto.BoardDTO;
import com.study.boardvue3.dto.CategoryDTO;
import com.study.boardvue3.dto.SearchCondition;

import java.util.List;

public interface BoardRepository {

    /**
     * 모든 카테고리 목록을 가져온다.
     *
     * @return
     */
    List<CategoryDTO> findAllCategories();

    /**
     * condition에 기반한 게시글 목록을 반환한다.
     *
     * @param condition 검색조건
     * @return
     */
    List<BoardDTO> findSmallBoardByCondition(SearchCondition condition);

    /**
     * condition에 기반한 게시글들의 수를 반환한다.
     *
     * @param condition 검색조건
     * @return
     */
    Integer countBoardByCondition(SearchCondition condition);

    /**
     * boardId를 primary key로 갖고 있는 boardDTO를 반환한다.
     *
     * @param boardId primary key
     * @return
     */
    BoardDTO findBoardDetail(Long boardId);

    /**
     * board의 views를 1 더한다.
     *
     * @param boardId
     */
    void addOneToViews(Long boardId);
}
