package com.codingrecipe.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    //Default page call method
    @GetMapping("/")
    public String index(){
        return "index"; //template 폴더의 index.html을 찾아서 호출함
    }
}
