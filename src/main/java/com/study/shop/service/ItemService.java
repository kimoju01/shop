package com.study.shop.service;

import com.study.shop.dto.ItemFormDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ItemService {
    // 상품 등록
    Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception;
    // 등록된 상품 정보 조회
    ItemFormDto getItemDetails(Long itemId);
    // 상품 수정
    Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception;

}
