package com.shop.entity;

import com.shop.constant.OAuthType;
import com.shop.constant.Role;
import com.shop.dto.MemberFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;


@Entity // 엔티티 클래스임을 지정
@Table(name = "member") // 테이블 이름 지정
@Getter
@Setter
@ToString
public class Member extends BaseEntity{
    @Id // 기본키
    @Column(name="member_id") // 테이블의 열과 매핑
    @GeneratedValue(strategy = GenerationType.AUTO) // 전략
    private Long id;

    @Column(unique = true) // 유일한 값으로 설정
    private String loginid;

    //    @Column(nullable = false) // password not null 설정
    private String password;

    private String name;

    private String email;

    private String address;

    private String phonenumber;

    @Enumerated(EnumType.STRING) // 멤버변수가 Enum(열거형) 타입일때 사용
    private Role role;

    @Enumerated(EnumType.STRING)
    private OAuthType oauth;
    // Member 엔티티를 생성하는 메서드
    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setLoginid(memberFormDto.getLoginid());
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        member.setPhonenumber(memberFormDto.getPhonenumber());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setRole(Role.USER);
//        member.setRole(Role.ADMIN);
        return member;
    }

}
