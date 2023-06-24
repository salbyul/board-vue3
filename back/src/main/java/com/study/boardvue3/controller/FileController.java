package com.study.boardvue3.controller;

import com.study.boardvue3.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
public class FileController {

    private final FileService fileService;

    /**
     * 파일 전송 메소드
     *
     * @param response
     * @param realName 전송할 파일의 이름
     * @throws IOException
     */
    @GetMapping("/file/download/{boardId}/{fileName}")
    public void downloadFile(HttpServletResponse response, @PathVariable("fileName") String realName, @PathVariable Long boardId) throws IOException {
        FileInputStream fileInputStream = fileService.getFileInputStream(realName, boardId);

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + realName);

        ServletOutputStream out = response.getOutputStream();
        int read = 0;
        byte[] buffer = new byte[1024];
        while ((read = fileInputStream.read(buffer, 0, buffer.length)) != -1) {
            out.write(buffer, 0, read);
        }

        out.flush();
        out.close();
        fileInputStream.close();
    }
}
