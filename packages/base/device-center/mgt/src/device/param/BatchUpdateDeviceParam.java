package com.guhecloud.cube.iot.mgt.device.param;

import lombok.Data;

import java.util.List;

/**
 * @author wql
 * @describe 批量修改设备名称和编码
 * @date 2024/6/27 14:21
 */
@Data
public class BatchUpdateDeviceParam {
    private List<UpdateInfo> updateInfos;

    @Data
    public static class UpdateInfo{

        private long deviceId;
        /**
         * 设备编码
         */
        private String deviceNumber;
        /**
         * 设备名称
         */
        private String name;
    }
}
