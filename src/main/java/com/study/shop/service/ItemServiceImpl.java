package com.study.shop.service;

import com.study.shop.dto.ItemFormDto;
import com.study.shop.entity.Item;
import com.study.shop.entity.ItemImg;
import com.study.shop.repository.ItemImgRepository;
import com.study.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional  // DB 작업 처리하다가 에러 하나라도 발생하면 로직 수행 전 상태로 모두 되돌려줌
@RequiredArgsConstructor
@Log4j2
public class ItemServiceImpl implements ItemService {

    private final ModelMapper modelMapper;
    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;

    private final ItemImgService itemImgService;

    @Override
    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {
        // 상품 등록
        Item item = modelMapper.map(itemFormDto, Item.class);
        Long id = itemRepository.save(item).getId();

        // 이미지 등록
        for (int i = 0; i < itemImgFileList.size(); i++) {
            ItemImg itemImg = ItemImg.builder()
                    .item(item)
                    .repImg(i == 0 ? "Y" : "N")  // 첫 번째 이미지일 경우 대표 이미지 여부 Y, 아니면 N
                    .build();
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }

        return id;
    }

}
