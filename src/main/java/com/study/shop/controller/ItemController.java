package com.study.shop.controller;

import com.study.shop.dto.ItemFormDto;
import com.study.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/admin/item/register")
    public String itemRegisterGet(Model model) {
        log.info("Item Register GET..........");

        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/itemRegister";
    }

    @PostMapping("/admin/item/register")
    public String itemRegisterPost(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model,
                                   @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {
        // @Vailid, BindingResult를 통해 DTO 검증
        log.info("item Register POST..........");

        // 검증 시 에러가 있다면(필수 값이 없다면) 상품 등록 페이지로 이동
        if (bindingResult.hasErrors()) {
            log.info("has error..........");
            return "item/itemRegister";
        }

        // 상품 등록 시 첫 번째 이미지(대표 이미지)가 없으면
        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫 번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemRegister";
        }

        try {
            // 상품 정보와 상품 이미지 정보를 담고 있는 itemImgFileList 넘겨줌
            itemService.saveItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생했습니다.");
            return "item/itemRegister";
        }

        // 상품이 정상적으로 등록되었다면 메인 페이지로 이동
        return "redirect:/";
    }

    @GetMapping("/admin/item/{itemId}")
    public String itemDetail(@PathVariable("itemId") Long itemId, Model model) {
        log.info("item Detail GET..........");

        try {
            ItemFormDto itemFormDto = itemService.getItemDetails(itemId);
            // 조회한 상품 데이터를 모델에 담아서 뷰로 전달
            model.addAttribute("itemFormDto", itemFormDto);
        } catch (EntityNotFoundException e) {
            // 상품 엔티티가 존재하지 않을 경우 에러 메시지를 담아서 상품 등록 페이지로 이동
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
            model.addAttribute("itemFormDto", new ItemFormDto());
            return "item/itemRegister";
        }

        return "item/itemModify";

    }

    @PostMapping("/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {
        log.info("item Update POST..........");

        // 검증 시 에러가 있다면(필수 값이 없다면) 상품 수정 페이지로 이동
        if (bindingResult.hasErrors()) {
            return "item/itemModify";
        }

        // 첫 번째 이미지(대표 이미지)가 없고 상품 id가 없으면 상품 수정 페이지로 이동
        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫 번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemModify";
        }

        try {
            itemService.updateItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생했습니다.");
            return "item/itemModify";
        }

        // 상품이 정상적으로 수정되었다면 메인 페이지로 이동
        return "redirect:/";
    }

}
