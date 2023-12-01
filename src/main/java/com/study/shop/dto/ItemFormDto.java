package com.study.shop.dto;

import com.study.shop.constant.ItemSellStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data   // @ToString, @EqualsAndHashCode, @Getter, @Setter, @RequiredArgsConstructor
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemFormDto {

    private Long id;

//    private String code;    // 상품 코드

    @NotBlank(message = "상품명을 입력해 주세요.")
    private String name;    // 상품 이름

    @NotNull(message = "상품 가격을 입력해 주세요.")
    private int price;          // 상품 가격

    @NotNull(message = "상품 재고를 입력해 주세요.")
    private int stock;    // 재고 수량

    @NotBlank(message = "상품 설명을 입력해 주세요.")
    private String detail;  // 상품 설명

    private ItemSellStatus itemSellStatus;  // 상품 판매 상태(SELL, SOLD_OUT)

    // 상품 저장 후 수정할 때 상품 이미지 정보를 저장하는 리스트
    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    // 상품의 이미지 아이디를 저장하는 리스트. 상품 수정 시에 이미지 아이디 담아둠.
    private List<Long> itemImgIds = new ArrayList<>();

}
