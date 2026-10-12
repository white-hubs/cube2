package com.guhecloud.cube.iot.mgt.core.data.msg;

import lombok.Getter;

@Getter
public enum Qos {
    //At most once (QoS 0)
    //At least once (QoS 1)
    //Exactly once (QoS 2)
    QOS0(0),
    QOS1(1),
    QOS2(2);

    private final int qosNumber;

    Qos(int qos) {
        this.qosNumber = qos;
    }

}
