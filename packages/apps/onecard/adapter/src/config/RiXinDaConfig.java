package com.guhecloud.cube.srv.gen.adapter.onecard.server.config;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/18 11:17
 * @Description:
 */
@Data
public class RiXinDaConfig {

    /**
     * 用户名
     * string(0,50)
     */
    @JSONField(name = "username")
    private String username;
    /**
     * 平
     * string(0,255)
     */
    @JSONField(name = "password")
    private String password;

    /**
     * publicKey
     */
    @JSONField(name = "publicKey")
    private String publicKey;
}
