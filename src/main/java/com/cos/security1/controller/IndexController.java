package com.cos.security1.controller;

import com.cos.security1.domain.User;
import com.cos.security1.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@Slf4j
@Controller
@RequiredArgsConstructor
public class IndexController {
    private final UserService userService;
    @GetMapping({"","/"})
    public String index(){
        return "index";
    }

    @GetMapping("/user")
    public String user(){
        return "user";
    }

    @GetMapping("/admin")
    public String admin(){
        return "admin";
    }

    @GetMapping("/manager")
    public String manager(){
        return "manager";
    }
    //security config 파일 작성 후 스프링 시큐리티가 정해 놓은 로그인 페이지로 안가고 내가 설정한 페이지로 가
    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm(){
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute User user){
        System.out.println(user);
        userService.save(user);
        return "redirect:/loginForm";
    }
}
