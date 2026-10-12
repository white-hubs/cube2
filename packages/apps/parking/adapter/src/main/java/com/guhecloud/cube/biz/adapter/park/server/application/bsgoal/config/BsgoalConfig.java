package com.guhecloud.cube.biz.adapter.park.server.application.bsgoal.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * {
 * "tenant":"58197260",
 * "serviceAddr":"",
 * "servicePort":"",
 * "serviceAddrSuffix":"",
 * "subsystemCode":"parkingServer",
 * "manufacturer":"bsgoal",
 * "manufacturerJson":{
 * "url":"http://172.30.10.35",
 * "parkingId":"UT",
 * "companyID":"71cfa1c59773ddfa289994e6d505bba3",
 * "accessCode":"",
 * "secretkey":"e7e7e7e7e7e7e7e7e7e7e7e7e7e7e7e7"
 * }
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BsgoalConfig {
    /**
     * url
     */
    private String url;
    /**
     * 停车场id 逗号分隔
     */
    private String parkingIds;
    /**
     * 公司id
     */
    private String companyId;
    /**
     * 接入code
     */
    private String accessCode;
    /**
     * 签名密钥
     */
    private String secretkey;
}
