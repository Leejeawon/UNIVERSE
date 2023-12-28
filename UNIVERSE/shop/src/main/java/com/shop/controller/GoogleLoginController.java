package com.shop.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;


@Controller
public class GoogleLoginController {

    // @AuthenticationPrincipal : 현재 인증된 사용자의 주체를 주입
    // 주로 인증된 사용자의 정보를 가져오고자 할 때 사용
    @GetMapping(value = "/oauth2/authorization/google")
    @ResponseBody
    public String oauthLoginInfo(Authentication authentication, @AuthenticationPrincipal OAuth2User oAuth2UserPrincipal){
        // 로그인 인증 정보를 가져옴
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        // oAuth2User객체를 이용하여 사용자의 정보를 가져옴
        Map<String, Object> attributes = oAuth2User.getAttributes();
        // 사용자 정보를 반환
        return attributes.toString();

    }
}
