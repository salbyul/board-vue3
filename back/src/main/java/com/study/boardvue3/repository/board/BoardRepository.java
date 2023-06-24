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
     * @param boardId 게시글의 primary key
     */
    void addOneToViews(Long boardId);

    /**
     * boardCreateDTO에 담긴 데이터를 DB에 저장한 후, 해당 레코드의 primary key를 반환한다.
     *
     * @param boardDTO 저장하기 위한 게시글의 데이터가 담긴 객체
     * @return
     */
    Long save(BoardDTO boardDTO);

    /**
     * DB에서 boardId를 primary key로 갖는 레코드를 제거한다.
     *
     * @param boardId 해당 게시글의 primary key
     */
    void deleteBoardByBoardId(Long boardId);

    /**
     * boardId를 primary key로 갖고 있는 게시글의 비밀번호를 반환한다.
     *
     * @param boardId 게시글의 primary key
     * @return
     */
    String findPasswordByBoardId(Long boardId);

    /**
     * boardId를 primary key로 갖는 게시글을 찾아 Writer, Title, Content를 boardDTO에 있는 데이터로 업데이트한다.
     *
     * @param boardId  게시글의 primary key
     * @param boardDTO 교체될 데이터가 담겨있는 객체
     */
    void update(Long boardId, BoardDTO boardDTO);
}
