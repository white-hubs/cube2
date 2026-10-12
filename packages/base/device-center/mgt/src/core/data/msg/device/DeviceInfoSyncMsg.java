package com.guhecloud.cube.iot.mgt.core.data.msg.device;

import lombok.Data;

import java.util.List;

/**
 * @author wql
 * @describe 设备信息同步消息
 * @date 2024/6/27 15:21
 */
@Data
public class DeviceInfoSyncMsg {
    /**
     * 实例seq
     */
    private String cmdSeq;

    /**
     * 任务id
     */
    private String instanceId;

    /**
     * 租户号
     */
    private String tenantId;
    /**
     * 网关设备
     */
    private DeviceInfo syncSelfInfo;
    /**
     * 网关子设备
     */
    private List<DeviceInfo> syncChildInfo;

    @Data
    public static class DeviceInfo{
        /**
         * 设备编码
         */
        private String devNum;
        /**
         * 设备id
         */
        private String devId;
        /**
         * 边缘侧设备标识
         */
        private String edgeDevIdentify;
        /**
         * 更新内容
         */
        private DeviceConfig config;

    }

    @Data
    public static class DeviceConfig{
        /**
         * 设备编码
         */
        private String newDevNum;
        /**
         * 设备名称
         */
        private String newDevName;
    }
}
