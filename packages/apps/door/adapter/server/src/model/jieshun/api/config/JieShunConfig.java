package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.config;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

/**
 * author: ht
 * create: 2025-05-22 10:54
 */
@Data
public class JieShunConfig {
    /**
     * http+ip+端口
     * string(0,128)
     */
    @JSONField(name = "host")
    private String host;
    /**
     * 用户名
     * string(0,50)
     */
    @JSONField(name = "username")
    private String username;
    /**
     * 平台密码，需RSA加密密码传输,RSA公钥调获取公钥接口，加密参考：RSA加密demo
     * string(0,255)
     */
    @JSONField(name = "password")
    private String password;

    private Integer tenant;
}
