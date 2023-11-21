package com.study.shop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Log4j2
public class ItemController {

    @GetMapping("/admin/item/register")
    public String itemRegisterGet() {
        return "/item/itemRegister";
    }

}
