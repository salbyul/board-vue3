package com.study.boardvue3.service;

import com.study.boardvue3.dto.BoardDTO;
import com.study.boardvue3.dto.CategoryDTO;
import com.study.boardvue3.dto.SearchCondition;
import com.study.boardvue3.repository.board.BoardRepository;
import com.study.boardvue3.repository.file.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final FileRepository fileRepository;

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
}
