package com.study.shop.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.shop.constant.ItemSellStauts;
import com.study.shop.entity.Item;
import com.study.shop.entity.QItem;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext // 영속성 컨텍스트 사용하기 위해 EntityManager 빈 주입
    EntityManager em;

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

    @Test
    @DisplayName("JPQL을 이용한 상품 조회 테스트")
    public void findByItemDetailTest() {

        this.createItemList();

        List<Item> itemList = itemRepository.findByItemDetail("상품 설명");
        for(Item item : itemList) {
            log.info(item.toString());
        }

    }

    @Test
    @DisplayName("QueryDSL 상품 조회 테스트")
    public void queryDslTest() {

        this.createItemList();

        JPAQueryFactory queryFactory = new JPAQueryFactory(em); // 생성자 파라미터로 EntityManager 객체 넣어줌
        QItem qItem = QItem.item;
        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(qItem.itemSellStauts.eq(ItemSellStauts.SELL))
                .where(qItem.itemDetail.like("%" + "상품 설명" + "%"))
                .orderBy(qItem.price.desc());

        List<Item> itemList = query.fetch();    // fetch를 이용해 쿼리 결과를 리스트로 반환. fetch() 메서드 실행 시점에 쿼리문이 실행됨.

        for(Item item : itemList) {
            log.info(item.toString());
        }

    }

}