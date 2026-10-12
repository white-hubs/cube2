package com.guhecloud.cube.biz.door.server.param;

import lombok.Data;

@Data
public class ControlParam {
    //门禁点
    private String doorIndexCodes;
    //控制
    private Integer controlType;
}
