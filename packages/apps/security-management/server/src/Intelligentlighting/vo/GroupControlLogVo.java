package com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.vo;

import lombok.Data;

@Data
public class GroupControlLogVo {
    private Long id;
    private String deviceName;
    private Long deviceId;
    private String action;
    private String result;
}
