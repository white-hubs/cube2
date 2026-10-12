package com.guhecloud.cube.biz.ouaa.server.dto.login;

import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-06  10:42
 * @Description:
 * @Version: 1.0
 */
@Data
public class MSTokenDTO {
    private String state;
    private String token;
    private String message;
    private String usercode;
}
