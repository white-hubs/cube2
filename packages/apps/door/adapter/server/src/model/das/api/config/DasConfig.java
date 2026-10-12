package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.das.api.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 大华的配置
 * {
 * "host":,
 * "username":,
 * "password":,
 * "clientId":,
 * "clientSecret":
 * }
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DasConfig {

    /**
     * 服务器地址
     */
    private String url;
    /**
     * 签名key
     */
    private String key;

    private Integer tenant;
}
