package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 门禁气体联动配置详情。
 *
 * @param configId 配置 ID，未保存配置时为空
 * @param doorDeviceId 门禁设备中心本地 ID
 * @param door 门禁设备中心基础信息
 * @param gasLinkageEnabled 是否开启气体联动
 * @param dataValidSeconds 气体数据有效时长，单位秒
 * @param gasConfigs 每种气体一条配置
 * @param updateTime 最后更新时间
 */
public record DoorGasLinkageConfigDetailResponse(
        Long configId,
        Long doorDeviceId,
        DoorDeviceResponse door,
        Boolean gasLinkageEnabled,
        Integer dataValidSeconds,
        List<DoorGasConfigResponse> gasConfigs,
        LocalDateTime updateTime
) {
}
