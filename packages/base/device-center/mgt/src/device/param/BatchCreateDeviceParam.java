package com.guhecloud.cube.iot.mgt.device.param;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BatchCreateDeviceParam {
    private MultipartFile excel;
    private boolean isSubDevice;

    private Long parentDeviceId;
    private String productId;
}
