package com.study.shop.repository;

import com.study.shop.constant.ItemSellStauts;
import com.study.shop.entity.Item;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("상품 저장 테스트")
    public void createItemTest() {

        Item item = Item.builder()
                .itemCode("op123")
                .itemName("테스트 상품")
                .price(100000)
                .itemDetail("상품 설명")
                .stockNumber(100)
                .itemSellStauts(ItemSellStauts.SELL)
                .build();

        Item savedItem = itemRepository.save(item);
        log.info(savedItem.toString());

    }

    // 테스트 상품 생성
    public void createItemList() {
        for (int i=1; i<10; i++) {
            Item item = Item.builder()
                    .itemCode("pt" + i)
                    .itemName("테스트 상품" + i)
                    .price(10000 + i)
                    .itemDetail("상품 설명" + i)
                    .stockNumber(100)
                    .itemSellStauts(ItemSellStauts.SELL)
                    .build();

            Item savedItem = itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNameTest() {

        this.createItemList();

        List<Item> itemList = itemRepository.findByItemName("테스트 상품1");
        for (Item item : itemList) {
            log.info(item.toString());
        }

    }

}