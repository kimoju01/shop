package com.study.shop.service;

import com.study.shop.entity.ItemImg;
import org.springframework.web.multipart.MultipartFile;

public interface ItemImgService {
    // 상품 이미지 업로드, 상품 이미지 정보 저장

    void saveItemImg(ItemImg itemImg, MultipartFile multipartFile) throws Exception;
    void updateItemImg(Long itemImgId, MultipartFile multipartFile) throws Exception;

}
