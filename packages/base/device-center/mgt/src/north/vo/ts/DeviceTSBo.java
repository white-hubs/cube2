package com.guhecloud.cube.iot.mgt.north.vo.ts;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author mioopp
 * @create 2020-12-14 13:56
 */
@Setter
@Getter
public class DeviceTSBo {

    private String deviceId;

    private String deviceName;

    private String deviceNumber;

    private List<PropertyTsBo> propertyTsBoList;
}
