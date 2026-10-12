package com.guhecloud.cube.biz.ouaa.server.dto.login;

import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-05  10:30
 * @Description:
 * @Version: 1.0
 */
@Data
public class MSLoginUserDTO {
    private String service;
    private String id;
    private String client_id;
}
