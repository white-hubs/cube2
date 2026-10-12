package com.guhecloud.cube.srv.gen.visitor.management.server.vo;

import lombok.Data;

@Data
public class ThirdTokenVO {
    private String userId;
    private String username;
    private String password;
    private String clientId;
    private String clientSecret;
    private String accessToken;
    /**
     * 有效时长
     */
    private Long expiresIn;
    private String refreshToken;
}
