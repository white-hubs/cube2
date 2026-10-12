package com.guhecloud.cube.base.device.center.metadata.std.dto;

import com.guhecloud.cube.base.device.center.metadata.dto.AreaDeviceDTO;
import lombok.Data;

import java.util.List;

@Data
public class DeviceLntAndLatCountDTO {
    private String lntAndLat;
    private Integer count;
    private List<AreaDeviceDTO> devices;
}