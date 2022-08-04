package com.cos.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {
    @GetMapping({"", "/"})
    public String index() {
        return "index";
    }

    @GetMapping("/manager")
    @ResponseBody
    public String manager() {
        return "manager";
    }

    //SecurityConfig 설정전에는 주소를 낚아챔, 생성 후에는 작동 안함
    @GetMapping("/login")
    public String login() {
        return "loginForm";
    }

    @GetMapping("/join")
    @ResponseBody
    public String join() {
        return "join";
    }
}
