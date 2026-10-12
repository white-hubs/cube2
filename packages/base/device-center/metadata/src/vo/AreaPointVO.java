package com.guhecloud.cube.base.device.center.metadata.vo;

import lombok.Data;

import java.util.List;

@Data
public class AreaPointVO {
    private String lntAndLat;
    private Integer count;
    private List<AreaDeviceVO> devices;
}
