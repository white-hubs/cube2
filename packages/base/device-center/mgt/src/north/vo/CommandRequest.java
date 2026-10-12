package com.guhecloud.cube.iot.mgt.north.vo;

import lombok.Data;

/**
 * @author mioopp
 * @create 2021-11-22 15:09
 */
@Data
public class CommandRequest {

    private String commandSeq;

    private String deviceId;

    private String propCode;

    private String propValue;

}
