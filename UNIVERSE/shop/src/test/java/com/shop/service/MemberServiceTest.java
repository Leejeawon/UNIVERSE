package com.shop.service;

import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class MemberServiceTest {
    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    // 테스트를 위한 멤버를 생성
    public Member createMember(){
        MemberFormDto memberFormDTO = new MemberFormDto();
        memberFormDTO.setEmail("test@email.com");
        memberFormDTO.setLoginid("abcd");
        memberFormDTO.setName("홍길동");
        memberFormDTO.setAddress("서울시 마포구 합정동");
        memberFormDTO.setPassword("1234");
        memberFormDTO.setPhonenumber("1111111111");
        return Member.createMember(memberFormDTO, passwordEncoder);
    }

    @Test
    @DisplayName("회원가입테스트")
    public void saveMemberTest(){
        Member member = createMember();
        Member savedMember = memberService.saveMember(member);

        // jUnitTest문법 (assertTrue, assertFalse, assertNull..)
        assertEquals(member.getEmail(), savedMember.getEmail());
        assertEquals(member.getLoginid(), savedMember.getLoginid());
        assertEquals(member.getName(), savedMember.getName());
        assertEquals(member.getAddress(), savedMember.getAddress());
        assertEquals(member.getPhonenumber(), savedMember.getPhonenumber());
        assertEquals(member.getPassword(), savedMember.getPassword());
        assertEquals(member.getRole(), savedMember.getRole());
    }

    @Test
    @DisplayName("중복 회원 가입 테스트")
    public void saveDuplicateMemberTest(){
        Member member1 = createMember();
        Member member2 = createMember();
        memberService.saveMember(member1);

        try {
            memberService.saveMember(member2);
        } catch (IllegalStateException e){
            assertTrue(e instanceof IllegalStateException);
            assertEquals("이미 가입된 회원입니다.", e.getMessage());
        }
    }

}
