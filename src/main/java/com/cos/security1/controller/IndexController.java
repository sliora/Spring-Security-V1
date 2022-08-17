package com.cos.security1.controller;

import com.cos.security1.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @GetMapping("/test/login")
    @ResponseBody
    public String testLogin(Authentication authentication, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        PrincipalDetails authenticationDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authenticationDetails = " + authenticationDetails.getUser());

        System.out.println("principalDetails = " + principalDetails.getUser());

        return "세션 정보 확인하기";
    }

    @GetMapping("/test/oauth/login")
    @ResponseBody
    public String testOauthLogin(Authentication authentication, @AuthenticationPrincipal OAuth2User oAuth2User) {
        OAuth2User authenticationOAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("oAuth2User = " + authenticationOAuth2User.getAttributes());

        System.out.println("oAuth2User = " + oAuth2User.getAttributes());

        return "OAuth 세션 정보 확인하기";
    }

    //OAuth 로그인을 해도 PrincipalDetails
    //일반 로그인을 해도 PrincipalDetails
    @GetMapping("/user")
    @ResponseBody
    public String User(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("principalDetails = " + principalDetails);
        return "user";
    }


    @GetMapping({"", "/"})
    public String index() {

        return "index";
    }

    @GetMapping("/manager")
    @ResponseBody
    public String manager() {
        return "manager";
    }

    @GetMapping("/admin")
    @ResponseBody
    public String admin() {
        return "admin";
    }

    //SecurityConfig 설정전에는 주소를 낚아챔, 생성 후에는 작동 안함
    @GetMapping("/loginForm")
    public String login() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }



    @PostMapping("/join")
    public String join(User user) {

        System.out.println(user);
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        userRepository.save(user); // 회원가입은 됨 비밀번호 : 1234 => 시큐리티로 로그인 할 수 없음. 이유는 패스워드가 암호화가 안되서
        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN")          //SecurityConfig에 설정 후 사용함, 1개에 대해서 권한을 줄 때
    @GetMapping("/info")
    @ResponseBody
    public String info() {
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") //여러 권한을 줄때 어노테이션
    @GetMapping("/data")
    @ResponseBody
    public String data() {
        return "데이터정보";
    }
}
