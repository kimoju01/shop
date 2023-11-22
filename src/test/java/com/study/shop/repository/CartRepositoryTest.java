package com.study.shop.repository;

import com.study.shop.dto.MemberFormDto;
import com.study.shop.entity.Cart;
import com.study.shop.entity.Member;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Log4j2
class CartRepositoryTest {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PersistenceContext
    EntityManager em;

    public Member createMember() {
        return Member.builder()
                .email("test@naver.com")
                .name("홍길동")
                .address("부산광역시 해운대구")
                .password(passwordEncoder.encode("1234"))
                .build();
    }

    @Test
    @DisplayName("장바구니 회원 엔티티 매핑 조회 테스트")
    public void findCartAndMemberTest() {
        Member member = createMember();
        memberRepository.save(member);

        Cart cart = Cart.builder()
                .member(member)
                .build();
        cartRepository.save(cart);

        // 영속성 컨텍스트에 데이터 저장 후 트랜잭션이 끝날 때 flush() 호출하여 DB에 반영
        // 회원 엔티티와 장바구니 엔티티를 영속성 컨텍스트에 저장 후 엔티티 매니저로부터 강제로 flush()를 호출해 DB에 반영
        em.flush();
        // 영속성 컨텍스트로부터 엔티티를 조회 후 영속성 컨텍스트에 엔티티가 없을 경우 DB를 조회
        // 실제 DB에서 장바구니 엔티티를 가지고 올 때 회원 엔티티도 같이 가지고오는지 보기 위해 영속성 컨텍스트 비워줌
        em.clear();

        Cart savedCart = cartRepository.findById(cart.getId())
                .orElseThrow(EntityNotFoundException::new); // 조회된 결과가 없을 경우 Exception 발생시킴
        assertEquals(savedCart.getMember().getId(), member.getId());
}

}