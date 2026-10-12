package com.guhecloud.cube.customization.xialing.server.liangcang.dto;

import lombok.Data;

/** 智慧仓库登录（租户字典 variable）。 */
@Data
public class WarehouseLoginCredentialDTO {
    private String baseUrl;
    private String host;
    private String account;
    private String username;
    private String password;
}
