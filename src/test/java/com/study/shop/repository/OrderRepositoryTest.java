package com.study.shop.repository;

import com.study.shop.constant.ItemSellStatus;
import com.study.shop.entity.Item;
import com.study.shop.entity.Member;
import com.study.shop.entity.OrderItem;
import com.study.shop.entity.Orders;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Log4j2
class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    public Item createItem() {
        Item item = Item.builder()
                .code("op123")
                .name("테스트 상품")
                .price(100000)
                .detail("상품 설명")
                .stock(100)
                .itemSellStatus(ItemSellStatus.SELL)
                .build();

        return item;
    }

    public Orders createOrders() {
        Orders orders = new Orders();

        for (int i=0; i<3; i++) {
            Item item = createItem();
            itemRepository.save(item);

            OrderItem orderItem = OrderItem.builder()
                    .item(item)
                    .count(10)
                    .price(1000)
                    .orders(orders)
                    .build();
            orders.getOrderItems().add(orderItem);
        }

        Member member = new Member();
        memberRepository.save(member);

        Orders.builder().member(member).build();
        orderRepository.save(orders);

        return orders;
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest() {

        Orders orders = new Orders();

        for (int i = 0; i < 3; i++) {
            Item item = this.createItem();
            itemRepository.save(item);
            OrderItem orderItem = OrderItem.builder()
                    .item(item)
                    .count(10)
                    .price(1000)
                    .orders(orders)
                    .build();
            orders.getOrderItems().add(orderItem);  // 영속성 컨텍스트에 저장되지 않은 orderItem 엔티티를 orders 엔티티에 담음
        }

        orderRepository.saveAndFlush(orders);   // orders 엔티티 저장하면서 flush() 호출해 영속성 컨텍스트에 있는 객체들을 DB에 반영
        em.clear(); // 영속성 컨텍스트의 상태 초기화

        Orders saveOrder = orderRepository.findById(orders.getId()) // 영속성 컨텍스트를 초기화했기 때문에 DB에서 Orders 엔티티 조회
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(3, saveOrder.getOrderItems().size());  // ItemOrder 엔티티 3개가 실제로 DB에 저장되었는지 검사

    }

    @Test
    @DisplayName("고아 객체 제거 테스트")
    public void orphanRemovalTest() {
        Orders orders = this.createOrders();
        // Orders 엔티티에서 관리하고 있는 OrderItem 리스트의 0번째 인덱스 요소 제거
        // => 부모 엔티티와 연관 관계 끊어져서 고아 객체 삭제하는 쿼리문 실행
        // Cascaee remove 옵션과 다른 점? remove 옵션은 부모 엔티티가! 삭제될 때 자식 엔티티도 함께 삭제 됨.
        orders.getOrderItems().remove(0);
        em.flush();
    }

    @Test
    @DisplayName("지연 로딩 테스트")
    public void lazyLoadingTest() {
        Orders orders = this.createOrders();

        Long orderItemId = orders.getOrderItems().get(0).getId();
        em.flush();
        em.clear();

        // 영속성 컨텍스트 상태 초기화 후
        // orders 엔티티에 저장했던 주문 상품 아이디를 이용해 orderItem을 DB에서 다시 조회
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(EntityNotFoundException::new);
        log.info("Order class : " + orderItem.getOrders().getClass());
        // 즉시 로딩 결과 -> orderItem 엔티티에 있는 orders 객체의 클래스를 출력. Order 클래스가 출력된다.
        // 지연 로딩 결과 -> 실제 엔티티 대신 프록시 객체를 넣어두고 프록시 객체는 실제 사용되기 전까지
        // 데이터 로딩을 하지 않고, 실제 사용 시점에 조회 쿼리문이 실행된다.
        log.info("==========");
        orderItem.getOrders().getOrderDate();
        log.info("==========");
    }

}