package com.guhecloud.subsystem.requester.sdk.param.accesscontrol;

import lombok.Data;

import java.util.List;

@Data
public class DoorControlParam {
    //门禁点集合
    private List<String> doorIndexCodes;
    //控制
    private Integer controlType;
    //控制人
    private String operator;
}
