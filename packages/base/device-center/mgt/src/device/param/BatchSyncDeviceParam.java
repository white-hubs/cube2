package com.guhecloud.cube.iot.mgt.device.param;

import lombok.Data;

import java.util.List;

/**
 * @author wql
 * @describe 从缓存批量同步设备参数
 * @date 2024/7/9 10:54
 */
@Data
public class BatchSyncDeviceParam {
    /**
     * 父设备id
     */
    private Long parentDeviceId;
    /**
     * 产品id
     */
    private Long productId;
    /**
     * 设备类型 1真实设备; 0虚拟设备
     */
    private int deviceType;
    /**
     * 导入设备信息
     */
    private List<SynDeviceInfo> synDevices;

    @Data
    public static class SynDeviceInfo {
        /**
         * 设备编号
         */
        private String deviceNumber;
        /**
         * 设备名称
         */
        private String deviceName;
    }
}
