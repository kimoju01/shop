package com.study.shop.service;

import com.study.shop.constant.ItemSellStatus;
import com.study.shop.dto.ItemFormDto;
import com.study.shop.entity.Item;
import com.study.shop.entity.ItemImg;
import com.study.shop.repository.ItemImgRepository;
import com.study.shop.repository.ItemRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Log4j2
class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemImgRepository itemImgRepository;

    List<MultipartFile> createMultipartFiles() throws Exception {
        List<MultipartFile> multipartFileList = new ArrayList<>();

        for (int i=0; i<5; i++) {
            String imgName = "image" + i + ".jpg";
            String path = "C:/Study/Project_documents/shop_documents/item_image/";

            // MockMultipartFile(String name, @Nullable String originalFilename, @Nullable String contentType, byte[] content)
            // 파일 파라미터 이름(경로 역할. 테스트용이라 큰 의미 X), 파일 원래 이름, 파일 컨텐츠 타입, 파일의 내용물(바이트 배열. 테스트용이라 임의의 4바이트로 지정해줌)
            // new byte[]{1, 2, 3, 4} => byte[0] = 1, byte[1] = 2, ... 길이가 4이고 각 요소가 1, 2, 3, 4인 바이트 배열
            MockMultipartFile multipartFile = new MockMultipartFile(path, imgName, "image/jpg", new byte[]{1, 2, 3, 4});
            multipartFileList.add(multipartFile);
        }

        return multipartFileList;
    }

    @Test
    @DisplayName("상품 등록 테스트")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void saveItem() throws Exception {
        ItemFormDto itemFormDto = ItemFormDto.builder()
                .name("테스트 상품")
                .price(10000)
                .stock(100)
                .detail("테스트 상품 설명")
                .itemSellStatus(ItemSellStatus.SELL)
                .build();

        // 가상의 MultipartFile 리스트 생성
        List<MultipartFile> itemImgFileList = createMultipartFiles();
        // 상품 데이터와 이미지 정보를 파라미터로 넘겨서 저장 후 상품 ID 값 반환
        Long itemId = itemService.saveItem(itemFormDto, itemImgFileList);

        // 상품 이미지 조회
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        // 상품 조회
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);

        // 등록한 상품 정보 검증
        assertEquals(itemFormDto.getName(), item.getName());
        assertEquals(itemFormDto.getPrice(), item.getPrice());
        assertEquals(itemFormDto.getStock(), item.getStock());
        assertEquals(itemFormDto.getDetail(), item.getDetail());
        assertEquals(itemFormDto.getItemSellStatus(), item.getItemSellStatus());
        // 첫 번째 이미지 파일명 검증
        assertEquals(itemImgFileList.get(0).getOriginalFilename(), itemImgList.get(0).getOriginName());
    }

}