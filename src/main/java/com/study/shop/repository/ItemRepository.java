package com.study.shop.repository;

import com.study.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item> {
    // QuerydslPredicateExecutor<Item> 인터페이스? 조건이 맞다고 판단하는 근거를 함수로 제공. 조건에 맞는 데이터 반환.
    List<Item> findByName(String name);

    // JPQL. 데이터베이스 종류에 상관 없다.
    @Query("select i from Item i where i.detail like %:detail% order by i.price desc")
    // 아래는 nativeQuery=true여서 MySQL일때만 쓸 수 있는 쿼리
//    @Query(value = "select * from item i where i.item_detail like %:itemDetail% order by i.price desc", nativeQuery = true)
    List<Item> findByDetail(@Param("detail") String detail);
}
