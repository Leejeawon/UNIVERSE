package com.shop.controller;

import com.shop.entity.Member;
import com.shop.service.KakaoLoginService;
import com.shop.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class KakaoLoginController {

    @Autowired
    private KakaoLoginService kakaoLoginService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MemberService memberService;

    @Value("${kakao.default.password}")
    private String kakaoPassword;

    @GetMapping("/members/kakao")
    public String kakaoCallback(String code){
        // 인증 서버로부터 받은 CODE를 이용하여 엑세스 토큰을 얻어옴
        String accessToken = kakaoLoginService.getAccessToken(code);
        // 응답을 콘솔과 브라우저에 출력함
//        System.out.println(accessToken);

        Member kakaoMember = kakaoLoginService.getMemberInfo(accessToken);

        // kakao 회원가입이 되어있으면 로그인 처리
        try {
            Member findMember = memberService.saveMember(kakaoMember);
        } catch (IllegalStateException e){
            memberService.loadUserByUsername(kakaoMember.getEmail());
        }

//        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(kakaoMember.getEmail(), kakaoPassword);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(kakaoMember.getEmail(), kakaoPassword);

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/";

    }
}
