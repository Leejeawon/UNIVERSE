package com.shop.controller;

import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import com.shop.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

@SpringBootTest
@AutoConfigureMockMvc // MVC테스트 프레임워크
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class MemberControllerTest {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    PasswordEncoder passwordEncoder;

    // 테스트에서 사용할 회원을 생성
    public Member createMember(String email, String password){
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail(email);
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울시 마포구 합정동");
        memberFormDto.setPassword(password);
        Member member = Member.createMember(memberFormDto, passwordEncoder);
        return memberService.saveMember(member);
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    public void loginSuccessTest() throws Exception {
        String email = "test@email.com";
        String password = "1234";
        this.createMember(email, password);

        // SpringMVC 테스트 프레임워크에서 제공하는 메서드
        // mockMvc.perForm() : Httprequest를 모방하여 컨트롤러의 동작을 테스트하는데 사용됨
        mockMvc.perform(formLogin().userParameter("email")
                .loginProcessingUrl("/members/login") // 로그인 처리 url 설정
                .user(email) // 로그인에 사용될 사용자의 이메일
                .password(password)) // 로그인에 사용될 사용자의 비밀번호
                .andExpect(SecurityMockMvcResultMatchers.authenticated()); // 테스트를 검증
    }

}
