package com.study.shop.service;

public interface FileService {
    // 파일 업로드, 파일 삭제

    String uploadFile(String uploadPath, String originFileName, byte[] fileData) throws Exception;
    void deleteFile(String filePath) throws Exception;

}
