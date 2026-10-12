package com.guhecloud.cube.biz.ouaa.server.param;

import lombok.Data;

@Data
public class OtherSysLoginParam {

    /**
     * 客户端id
     */
    private String client_id;

    /**
     * 客户端密钥
     */
    private String client_secret;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密钥id
     */
    private String secretId;
}
