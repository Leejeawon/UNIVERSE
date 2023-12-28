package com.shop.service;

import com.shop.constant.OAuthType;
import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
// @Transactional : 로직을 처리하다가 에러 발생시 이전 상태로 롤백
// 자동으로 트랜잭션 처리
@Transactional
// @RequiredArgsConstructor : 의존성 주입을 할 때 자동으로 생성자를 생성함
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    // 회원가입을 처리하기 위한 메서드
    public Member saveMember(Member member){
        // 중복된 회원이 있는지 검사하는 메서드 호출
        validateDuplicateMember(member);

        if (member.getOauth() == null){
            member.setOauth(OAuthType.SHOP);
        }

        return memberRepository.save(member);
    }
    private void validateDuplicateMember(Member member){
        Member findMember = memberRepository.findByLoginid(member.getLoginid());
        if (findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Member member = memberRepository.findByLoginid(login);

        if(member == null){
            throw new UsernameNotFoundException(login);
        }

        return User.builder()
                .username(member.getLoginid()) // 아이디
                .password(member.getPassword()) // 사용자의 비밀번호 설정
                .roles(member.getRole().toString()) // 사용자의 권한
                .build();
    }

    public Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + id));
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
}
