package com.study.shop.service;

import com.study.shop.dto.ItemFormDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ItemService {
    // 상품 등록

    Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception;

}
