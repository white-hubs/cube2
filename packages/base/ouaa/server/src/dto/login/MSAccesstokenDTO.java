package com.guhecloud.cube.biz.ouaa.server.dto.login;

import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-05  10:30
 * @Description:
 * @Version: 1.0
 */
@Data
public class MSAccesstokenDTO {
    private String access_token;
    private String refresh_token;
    private String token_type;
    private int expires_in;
}
