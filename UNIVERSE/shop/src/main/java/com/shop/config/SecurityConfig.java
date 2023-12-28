package com.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration // 스프링을 구성하기 위한 클래스
@EnableWebSecurity // 스프링 시큐리트를 활성화시키기 위한 어노테이션
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.formLogin()
                .loginPage("/members/login") // 로그인 페이지로 설정
                .defaultSuccessUrl("/") // 로그인에 성공하면 이동할 url
                .usernameParameter("loginid") // 로그인시 사용할 이름
                .failureUrl("/members/login/error") // 로그인에 실패했을 때 보여줄 url
                .and()
                .logout()// 로그아웃 처리
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))// 로그아웃 url 설정
                .logoutSuccessUrl("/"); // 로그아웃에 성공하면 이동할 url

        http.authorizeRequests()
                // 모든 사용자가 인증 없이 접근 가능
                .mvcMatchers("/css/**", "/js/**", "/img/**").permitAll()
                .mvcMatchers("/", "/members/**", "/item/**", "/board/**","/boardcontent/**","/boardmain/**","/posts/**","/noticeboard/**", "/shopping/**", "/images/**", "/extras/**").permitAll()
                // admin으로 시작하는 경로는 ADMIN만 가능
                .mvcMatchers("/admin/**").hasRole("ADMIN")
                // 나머지는 모두 인증을 요청
                .anyRequest().authenticated();

        // OAuth2기반의 로그인을 한 경우
        http.oauth2Login()
                // 인증이 필요한 URL에 접근하면 /loginForm으로 이동
                .loginPage("/member/login")
                // 로그인 성공 시 메인화면
                .defaultSuccessUrl("/")
                // 로그인 실패 시 /loginForm으로 이동
                .failureUrl("/member/login/error")
                // 로그인 성공 후 사용자 정보를 가져온다.
                .userInfoEndpoint();

        // 인증받지 않은 사용자가 접근하면 수행
        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());


        // HttpSecurity 객체를 매개변수로 받아서 해당 객체를 사용하여
        // 필터 체인을 구성하고 반환
        return  http.build();
    }
    // AuthenticationManager : 스프링 시큐리티에서 인증을 처리하는 인터페이스
    // AuthenticationConfiguration : 스프링 시큐리티에서 인증 구성을 담당하는 클래스
    //                               인증 관련된 설정을 가져옴
    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        // 비밀번호 암호화
        return new BCryptPasswordEncoder();
    }

}
