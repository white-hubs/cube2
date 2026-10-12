package com.guhecloud.cube.biz.adapter.park.server.application.reformer.config;

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
 * "url":"http://127.0.0.1:8080/serviceName",
 * "aesKey":"cmVmb3JtZXJyZWZvcm1lcg==",
 * "stationNoList":"999997,999998"
 * }
 * }
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReformerConfig {

    private String url;

    private String aesKey = "cmVmb3JtZXJyZWZvcm1lcg==";

    /**
     * 终端号列表，逗号","分隔
     */
    private String stationNoList = "99997";
}
