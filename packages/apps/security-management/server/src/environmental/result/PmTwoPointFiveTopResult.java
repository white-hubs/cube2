package com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.result;

import lombok.Data;

/**
 * pm2.5top返回对象
 */
@Data
public class PmTwoPointFiveTopResult {
    private Long deviceId;
    private String deviceName;
    private String value;
}
