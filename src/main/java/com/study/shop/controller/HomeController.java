package com.study.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/ex")
public class HomeController {

    @GetMapping(value = "/test1")
    public String thymeleafEx1(Model model) {
        model.addAttribute("data", "타임리프 테스트");
        return "hello";
    }

}
