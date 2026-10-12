package com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-11-05  11:44
 * @Description:
 * @Version: 1.0
 */
@Data
public class DeviceVO {
    private Long productId;
    private Long productName;
   private List<DeviceListVO> deviceList;
}
