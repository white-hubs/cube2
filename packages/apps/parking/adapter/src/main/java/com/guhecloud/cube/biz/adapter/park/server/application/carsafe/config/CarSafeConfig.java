package com.guhecloud.cube.biz.adapter.park.server.application.carsafe.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * {
 * "tenant":"58197260",
 * "serviceAddr":"http://172.30.10.35",
 * "servicePort":"9022",
 * "serviceAddrSuffix":"",
 * "subsystemCode":"parkingServer",
 * "manufacturer":"carsafe",
 * "manufacturerJson":{
 * "baseUrl":"http://172.30.10.35",
 * "account":"UT",
 * "privateKey":"71cfa1c59773ddfa289994e6d505bba3"
 * }
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarSafeConfig {
    /**
     * 服务地址
     */
    private String baseUrl;

    /**
     * 账号
     */
    private String account;

    /**
     * 私钥
     */
    private String privateKey;

}
