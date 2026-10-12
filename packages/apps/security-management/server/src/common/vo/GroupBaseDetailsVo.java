package com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo;

import lombok.Data;

@Data
public class GroupBaseDetailsVo {
    private Long id;
    private Long deviceId;
    private String deviceName;
    private String areaName;
    private Long groupControlId;
}
