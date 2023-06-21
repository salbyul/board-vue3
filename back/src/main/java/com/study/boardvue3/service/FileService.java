package com.study.boardvue3.service;

import com.study.boardvue3.repository.file.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RequiredArgsConstructor
@Service
public class FileService {

    @Value("${file.dir}")
    private String PATH;

    private final FileRepository fileRepository;

    /**
     * realName 이용해 해당 파일의 FileInputStream 객체를 생성해 반환한다.
     *
     * @param realName 파일의 이름
     * @return
     * @throws FileNotFoundException 파일이 없을 경우
     */
    public FileInputStream getFileInputStream(String realName, Long boardId) throws FileNotFoundException {
        String fileName = fileRepository.findFileNameByRealName(realName, boardId);
        return new FileInputStream(PATH + fileName);
    }

}
