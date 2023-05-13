package com.cos.security1.controller;

import com.cos.security1.auth.PrincipalDetails;
import com.cos.security1.domain.User;
import com.cos.security1.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
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

    //OAuth/일반 로그인 둘다 여기서 핸들 가능
    @ResponseBody
    @GetMapping("/user")
    public String user(@AuthenticationPrincipal PrincipalDetails principalDetails){
      log.info("principal = {}",principalDetails.getUser());
        return "user";
    }
    @ResponseBody
    @GetMapping("/admin")
    public String admin(){
        return "admin";
    }
   @ResponseBody
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
        userService.save(user);
        return "redirect:/loginForm";
    }

    @ResponseBody
    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public String info(){
        return "Private Info";
    }

    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @GetMapping("/data")
    public String data(){
        return "Data";
    }

    //To get Authentication.principal after login
    @ResponseBody
    @GetMapping("/test/login")
    public String testLogin(Authentication authentication, @AuthenticationPrincipal UserDetails userDetails,@AuthenticationPrincipal PrincipalDetails pd){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal(); //userDetails를 구현한 오브젝트를 리턴
        log.info("authentication user : {}",principalDetails.getUser()); //유저 정보 반환

        log.info("@AuthenticationPrincipal = {}",userDetails.getUsername());
        log.info("@AuthenticationPrincipal type: PrincipalDetails = {}",pd.getUser());
        return "Check Session Info";
    }

    //To get Authentication.principal after google login
    @ResponseBody
    @GetMapping("/test/oauth/login")
    public String testOauthLogin(Authentication authentication,@AuthenticationPrincipal OAuth2User oauth){
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();//oauth2User 가 UserDetails 역용
        log.info("authentication user : {}",oAuth2User.getAttributes()); //유저 정보 반환

        log.info("@AuthenticationPrincipal = {}",oauth.getAttributes());
        return "Check Session Info";
    }
}
