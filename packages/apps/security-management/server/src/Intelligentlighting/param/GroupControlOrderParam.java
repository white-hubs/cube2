package com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.param;

import lombok.Data;

@Data
public class GroupControlOrderParam {
    private String applicationCode;
    //群控id
    private Long groupControlId;
    //开关值
    private String openGate;
}
