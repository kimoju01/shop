package com.study.shop.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.shop.constant.ItemSellStatus;
import com.study.shop.entity.Item;
import com.study.shop.entity.QItem;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

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
                .code("op123")
                .name("테스트 상품")
                .price(100000)
                .detail("상품 설명")
                .stock(100)
                .itemSellStatus(ItemSellStatus.SELL)
                .build();

        Item savedItem = itemRepository.save(item);
        log.info(savedItem.toString());

    }

    // 테스트 상품 생성
    public void createItemList() {
        for (int i=1; i<10; i++) {
            Item item = Item.builder()
                    .code("pt" + i)
                    .name("테스트 상품" + i)
                    .price(10000 + i)
                    .detail("상품 설명" + i)
                    .stock(100)
                    .itemSellStatus(ItemSellStatus.SELL)
                    .build();

            Item savedItem = itemRepository.save(item);
        }
    }

    public void createItemList2() {
        for (int i=1; i<5; i++) {
            Item item = Item.builder()
                    .code("pt" + i)
                    .name("테스트 상품" + i)
                    .price(10000 + i)
                    .detail("상품 설명" + i)
                    .stock(100)
                    .itemSellStatus(ItemSellStatus.SELL)
                    .build();

            Item savedItem = itemRepository.save(item);
        }

        for (int i=6; i<10; i++) {
            Item item = Item.builder()
                    .code("pt" + i)
                    .name("테스트 상품" + i)
                    .price(10000 + i)
                    .detail("상품 설명" + i)
                    .stock(0)
                    .itemSellStatus(ItemSellStatus.SOLD_OUT)
                    .build();

            Item savedItem = itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNameTest() {

        this.createItemList();

        List<Item> itemList = itemRepository.findByName("테스트 상품1");
        for (Item item : itemList) {
            log.info(item.toString());
        }

    }

    @Test
    @DisplayName("JPQL을 이용한 상품 조회 테스트")
    public void findByItemDetailTest() {

        this.createItemList();

        List<Item> itemList = itemRepository.findByDetail("상품 설명");
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
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.detail.like("%" + "상품 설명" + "%"))
                .orderBy(qItem.price.desc());

        List<Item> itemList = query.fetch();    // fetch를 이용해 쿼리 결과를 리스트로 반환. fetch() 메서드 실행 시점에 쿼리문이 실행됨.

        for(Item item : itemList) {
            log.info(item.toString());
        }

    }

    @Test
    @DisplayName("QuerydslPredicateExecutor 인터페이스 상품 조회 테스트")
    public void queryDslTest2() {

        this.createItemList2();

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QItem qItem = QItem.item;

        String itemDetail = "상품 설명";
        int price = 10003;
        String itemSellStatus = "SELL";

        // BooleanBuilder로 쿼리 만들어줌
        booleanBuilder.and(qItem.detail.like("%" + "상품 설명" + "%"));
        booleanBuilder.and(qItem.price.gt(price));

        if (StringUtils.equals(itemSellStatus, ItemSellStatus.SELL)) {  // 판매 상태가 SELL이 때만 booleanBuilder에 조건 동적으로 추가
            booleanBuilder.and(qItem.itemSellStatus.eq(ItemSellStatus.SELL));
        }

        Pageable pageable = PageRequest.of(0, 5);   // PageRequest.of(조회할 페이지 번호, 한 페이지당 조회할 데이터 개수)
        // QuerydslPredicateExecutor 인터페이스의 findAll() 메서드로 조건에 맞는 데이터를 Page 객체로 받아옴
        Page<Item> itemPagingResult = itemRepository.findAll(booleanBuilder, pageable);
        log.info("total element : " + itemPagingResult.getTotalElements());

        List<Item> resultItemList = itemPagingResult.getContent();
        for (Item resultItem : resultItemList) {
            log.info(resultItem.toString());
        }

    }

}