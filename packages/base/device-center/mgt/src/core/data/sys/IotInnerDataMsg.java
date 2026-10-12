package com.guhecloud.cube.iot.mgt.core.data.sys;

import lombok.Data;

@Data
public class IotInnerDataMsg {
    private String type;
    private Object dataMsg;
}
