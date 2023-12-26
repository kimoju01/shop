package com.study.shop.service;

import com.study.shop.dto.ItemFormDto;
import com.study.shop.dto.ItemImgDto;
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

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
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

    @Override
    @Transactional(readOnly = true) // 읽기 전용 설정. JPA가 더티체킹(변경감지)을 수행하지 않아서 성능 향상시킬 수 있다
    public ItemFormDto getItemDetails(Long itemId) {
        // itemId에 해당하는 상품 정보 조회

        // itemId에 해당하는 모든 itemImg 엔티티를 조회. 등록순으로 가져오기 위해 상품 이미지 id 오름차순으로 가지고 옴
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        // 조회한 itemImg 엔티티 담을 리스트 생성. 엔티티를 DTO로 변환 후 리스트에 넣을 거임.
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();

        for (ItemImg itemImg : itemImgList) {
            // 조회한 ItemImg 엔티티를 ItemImgDto 객체로 변환 후 리스트에 추가
            ItemImgDto itemImgDto = modelMapper.map(itemImg, ItemImgDto.class);
            itemImgDtoList.add(itemImgDto);
        }

        // itemId에 해당하는 Item 엔티티 조회
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
        // 조회한 Item 엔티티를 ItemFormDto 객체로 변환
        ItemFormDto itemFormDto = modelMapper.map(item, ItemFormDto.class);
        // ItemFormDto가 변환된 Item 엔티티랑 이미지 리스트를 모두 포함
        itemFormDto.setItemImgDtoList(itemImgDtoList);

        return itemFormDto;
    }

    @Override
    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {
        // 상품 수정
        // 상품 등록 화면으로부터 전달 받은 상품 id를 이용해 상품 엔티티 조회
        Item item = itemRepository.findById(itemFormDto.getId()).orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto);

        // 상품 이미지 id 리스트 조회
        List<Long> itemImgIds = itemFormDto.getItemImgIds();

        // 이미지 등록
        for (int i=0; i<itemImgFileList.size(); i++) {
            // 상품 이미지 id와 이미지 파일을 이용해 해당 이미지 업데이트
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
        }

        return item.getId();
    }
}
