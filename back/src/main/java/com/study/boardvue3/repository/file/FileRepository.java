package com.study.boardvue3.repository.file;

import com.study.boardvue3.dto.FileDTO;

import java.util.List;

public interface FileRepository {

    /**
     * boardId를 외래키로 갖는 파일들의 이름과 primary key를 가진 DTO를 반환한다.
     *
     * @param boardId board의 primary key
     * @return
     */
    List<FileDTO> findFileNamesByBoardId(Long boardId);

    /**
     * 파일의 업로드 당시 이름을 이용해 저장된 파일의 이름을 찾아 반환한다.
     *
     * @param realName 파일 업로드 당시 이름
     * @param boardId  해당 파일의 게시글 외래키
     * @return
     */
    String findFileNameByRealName(String realName, Long boardId);

    /**
     * fileDTO에 담긴 데이터들을 DB에 저장한다.
     *
     * @param fileDTO 데이터가 담긴 객체
     */
    void save(FileDTO fileDTO);
}
