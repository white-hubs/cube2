package com.guhecloud.cube.iot.north.bridge.client.pj.param;

import lombok.Data;

/**
 * @author mioopp
 * @create 2021-11-22 15:09
 */
@Data
public class CommandParam {

    private String commandSeq;

    private String deviceId;

    private String propCode;

    private String propValue;

}
