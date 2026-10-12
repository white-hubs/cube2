package com.guhecloud.cube.biz.adapter.park.server.application.jscloud.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 捷顺云数据中心配置（图片查询等接口）
 * 对接文档: 数据中心平台登录、图片查询-返回URL
 * 从 manufacturerJson 解析，需包含 dataCenterPno、dataCenterSecret、dataCenterBaseUrl
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsCloudDataCenterConfig {

    /**
     * 平台编号，对接数据推送时提供
     */
    private String dataCenterPno;

    /**
     * 密钥，对接数据推送时提供
     */
    private String dataCenterSecret;

    /**
     * 数据中心 base 地址，默认 http://jhtdc.jslife.com.cn
     */
    private String dataCenterBaseUrl;
}
