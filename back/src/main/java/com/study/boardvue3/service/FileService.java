package com.study.boardvue3.service;

import com.study.boardvue3.dto.FileDTO;
import com.study.boardvue3.repository.board.BoardRepository;
import com.study.boardvue3.repository.file.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileService {

    @Value("${file.dir}")
    private String PATH;

    private final FileRepository fileRepository;
    private final BoardRepository boardRepository;

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

    /**
     * MultipartFile List를 받아 로컬과 DB에 저장한다.
     *
     * @param files   파일 리스트
     * @param boardId 게시글의 Primary Key
     * @throws IOException
     */
    public void saveFiles(List<MultipartFile> files, Long boardId) throws IOException {
        if (isFilesEmpty(files)) return;

        for (MultipartFile file : files) {
            saveFile(file, boardId);
        }
    }

    private boolean isFilesEmpty(List<MultipartFile> files) {
        return files == null;
    }

    /**
     * 파일의 이름을 체크 후 알맞은 형태로 변경, 로컬에 저장한 후 DB에 파일 정보를 저장한다.
     * <p>
     * 파일의 이름이 Null, Empty, Blank 일 경우 IllegalArgumentException 발생
     *
     * @param file    파일
     * @param boardId 게시글의 Primary Key
     * @throws IOException
     */
    private void saveFile(MultipartFile file, Long boardId) throws IOException {
        String fileName = file.getOriginalFilename();

        if (StringUtils.hasText(fileName)) {
            String renamedName = fileRename(fileName);
            saveToLocal(file, renamedName);
            FileDTO fileDTO = new FileDTO(renamedName, fileName, boardId);
            fileRepository.save(fileDTO);
        } else {
//            파일의 이름이 체크에 실패하면 생성된 Board를 DB에서 제거하고 IllegalArgumentException 발생
            boardRepository.deleteBoardByBoardId(boardId);
            throw new IllegalArgumentException("File Null Name");
        }
    }

    /**
     * UUID + 현재 날짜 시각 + fileName 형태의 String을 리턴한다.
     *
     * @param fileName 파일 이름
     * @return 변경된 파일 이름
     */
    private String fileRename(String fileName) {
        String body;
        String ext;
        int dot = fileName.lastIndexOf(".");
        if (dot != -1) {
            body = fileName.substring(0, dot);
            ext = fileName.substring(dot);
        } else {
            body = fileName;
            ext = "";
        }

        String uuid = String.valueOf(UUID.randomUUID());
        LocalDateTime now = LocalDateTime.now();
        return uuid + now + "[" + body + "]" + ext;
    }

    /**
     * 로컬에 파일을 저장한다.
     *
     * @param file        파일
     * @param renamedName 저장할 파일의 이름
     * @throws IOException
     */
    private void saveToLocal(MultipartFile file, String renamedName) throws IOException {
        file.transferTo(new File(PATH + renamedName));
    }

    /**
     * boardId를 외래키로 갖고 있는 레코드들의 real_name과 primary key 리스트를 반환한다.
     *
     * @param boardId Board의 primary key
     * @return
     */
    public List<FileDTO> getFileNames(Long boardId) {
        return fileRepository.findFileNamesByBoardId(boardId);
    }

    /**
     * 제거될 File의 real_name들을 골라 제거한다.
     *
     * @param boardId   외래키
     * @param fileNames 제거되지 않을 real_name 목록
     */
    public void removeFileByModification(Long boardId, List<String> fileNames) {
        List<String> existRealNames = fileRepository.findFileNamesByBoardId(boardId).stream().map(FileDTO::getRealName).collect(Collectors.toList());
        if (existRealNames.isEmpty()) return;

        if (!fileNames.isEmpty()) {
            List<String> listToRemove = selectToRemove(fileNames, existRealNames);
            removeFiles(boardId, listToRemove);
        } else {
            fileRepository.deleteByBoardId(boardId);
        }
    }

    /**
     * existRealNames에는 존재하지만 fileNames에는 존재하지 않는 이름들의 목록을 리턴한다.
     * (제거될 파일 이름을 골라낸다.)
     * 주의: 얕은 복사이다.
     *
     * @param fileNames      제거되지 않을 이름 목록
     * @param existRealNames 기존에 존재하는 이름 목록
     * @return
     */
    private List<String> selectToRemove(List<String> fileNames, List<String> existRealNames) {
        List<String> result = new ArrayList<>(existRealNames);
        for (String fileName : fileNames) {
            result.remove(fileName);
        }
        return result;
    }

    /**
     * listToRemove에 있는 값 중 boardId를 외래키로 갖고, real_name과 같은 값들을 DB에서 제거한다.
     *
     * @param boardId           외래키
     * @param realNamesToRemove 제거될 real_name 목록
     */
    private void removeFiles(Long boardId, List<String> realNamesToRemove) {
        if (!realNamesToRemove.isEmpty()) {
            fileRepository.deleteFileByRealNameAndBoardId(boardId, realNamesToRemove);
        }
    }
}
