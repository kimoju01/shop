package com.study.shop.controller;

import com.study.shop.dto.ItemFormDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Log4j2
public class ItemController {

    @GetMapping("/admin/item/register")
    public String itemRegisterGet(Model model) {
        log.info("Item Register GET..........");

        model.addAttribute("itemFormDto", new ItemFormDto());
        return "/item/itemRegister";
    }

}
