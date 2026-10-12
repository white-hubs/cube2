package com.guhecloud.subsystem.requester.sdk.param.drone;

import lombok.Data;

@Data
public class DroneFlyActionParam {
    /**
     * 机库编号（机巢SN）。
     */
    private String nestSn;
    /**
     * 飞行操控类型：0前进，1后退，2左移，3右移，4左旋，5右旋，6上升，7下降。
     */
    private Integer type;
}
