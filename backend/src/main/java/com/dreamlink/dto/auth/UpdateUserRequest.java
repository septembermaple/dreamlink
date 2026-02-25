package com.dreamlink.dto.auth;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String nickname;
    private String avatar;
    private String constellation;
    private String phone;
}
