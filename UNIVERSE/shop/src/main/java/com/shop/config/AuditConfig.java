package com.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
// jpaAuditing 기능을 활성화
// 엔티티의 생성일과 수정일을 자동으로 관리하기 위해서 사용
@EnableJpaAuditing
public class AuditConfig {

    @Bean
    public AuditorAware<String> auditorProvider(){
        return new AuditorAwarelmpl();
    }

}
