package com.study.boardvue3.repository.file;

import java.util.List;

public interface FileRepository {

    /**
     * boardId를 외래키로 갖는 파일들의 이름을 반환한다.
     *
     * @param boardId board의 primary key
     * @return
     */
    List<String> findFileNamesByBoardId(Long boardId);

    /**
     * 파일의 업로드 당시 이름을 이용해 저장된 파일의 이름을 찾아 반환한다.
     *
     * @param realName 파일 업로드 당시 이름
     * @param boardId  해당 파일의 게시글 외래키
     * @return
     */
    String findFileNameByRealName(String realName, Long boardId);
}
