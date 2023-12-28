package com.shop.dto;

import com.shop.constant.OAuthType;
import com.shop.constant.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDetailsDto {
    private Long id;
    private String loginid;
    private String name;
    private String email;
    private String address;
    private String phonenumber;
    private Role role;
    private OAuthType oauth;

}

