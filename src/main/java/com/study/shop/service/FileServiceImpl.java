package com.study.shop.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log4j2
public class FileServiceImpl implements FileService {

    @Override
    public String uploadFile(String uploadPath, String originFileName, byte[] fileData) throws Exception {
        String extension = originFileName.substring(originFileName.lastIndexOf("."));
        String savedFileName = UUID.randomUUID() + extension;
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;

        // 파일이 저장될 위치와 파일의 이름을 넘겨 파일에 쓸 파일 출력 스트림 만듦
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        // 업로드된 파일의 데이터를 담고 있는 byte 배열을 파일 출력 스트림에 입력
        fos.write(fileData);
        fos.close();

        return savedFileName;
    }

    @Override
    public void deleteFile(String filePath) throws Exception {
        File deleteFile = new File(filePath);   // 파일 객체 생성

        if (deleteFile.exists()) {  // 해당 파일이 존재하면
            deleteFile.delete();
            log.info("파일을 삭제했습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }
}
