//package com.shop.service;
//
//import com.shop.constant.OAuthType;
//import com.shop.constant.Role;
//import com.shop.dto.PrincipalDetails;
//import com.shop.entity.Member;
//import com.shop.repository.MemberRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@Transactional
//@RequiredArgsConstructor
//public class GoogleLoginService extends DefaultOAuth2UserService {
//
//    private final MemberRepository memberRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @Value("${google.default.password}")
//    private String googlePassword;
//
//    // 1. 사용자가 구글에 로그인을 성공하면 구글은 인증 클라이언트에 CODE정보를 전달함
//    // 2. OAuth2 클라이언트는 code를 기반으로 액세스 토큰을 요청하고 액세스 토큰이 저장된 OAuth2UserRequest를 받음
//    // 3. OAuth2 클라이언트가 OAuth2UserRequest를 매개변수로 넘기면서 loadUser 메서드를 호출함
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        // 액세스 토큰이 저장된 userRequest를 이용하여
//        // 구글로부터 회원 정보를 받아옴
//        OAuth2User oAuth2User = super.loadUser(userRequest);
//        // 구글이 전달해준 정보를 바탕으로 회원 정보를 구성
//        String userName = oAuth2User.getAttribute("name");
//        String userEmail = oAuth2User.getAttribute("email");
//        String password = googlePassword;
//
//        Member findUser;
//
//        try{
//            // 회원가입이 되어있는 사용자인지 확인
//            findUser = memberRepository.findByLoginid(userEmail);
//        } catch (NullPointerException e){
//            // 회원가입이 되어있지 않으면 member 객체를 생성
//            findUser = new Member();
//        }
//        // 회원가입이 되어있지 않으면 member객체에 데이터를 설정
//        if (findUser == null){
//            Member member = new Member();
//            member.setName(userName);
//            member.setEmail(userEmail);
//            member.setPassword(passwordEncoder.encode(password));
//            member.setRole(Role.USER);
//            member.setOauth(OAuthType.GOOGLE);
//
//            findUser = member;
//            memberRepository.save(member);
//        }
//        // OAuth2 클라이언트가 PrincipalDetails에 설정된 정보를 이용하여
//        // authencation객체를 SecurithContext에 자동으로 등록함.
//        return new PrincipalDetails(findUser, oAuth2User.getAttributes());
//    }
//}
