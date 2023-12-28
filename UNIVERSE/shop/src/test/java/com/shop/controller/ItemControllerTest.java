package com.shop.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class ItemControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("상품 등록 페이지 권한 테스트")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void itemFormTest() throws Exception{
        // /admin/item/new 경로로 get방식으로 요청을 수행하도록 함
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/item/new"))
                .andDo(print()) // 테스트에 대한 요청과 응답을 콘솔에 출력
                .andExpect(status().isOk()); // 응답상태가 ok(200)인지 확인
    }

    @Test
    @DisplayName("상품 등록 페이지 일반회원 권한 테스트")
    @WithMockUser(username = "user", roles = "USER")
    public void itemFormNotAdminTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/item/new"))
                .andDo(print())
                .andExpect(status().isForbidden()); // 응답상태가 실패인지 확인
                // Forbidden : 서버가 허용하지 않는 웹페이지를 사용자가 요청할 때 발생
    }

}
