package com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LoginResponse {

    /**
     * token
     */
    private String access_token;

    /**
     * 用于刷新的token
     */
    private String refresh_token;

    /**
     * 登陆名称
     */
    private String loginId;

    /**
     * token有效期
     */
    private Long expires_in;
}
