package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import java.util.List;

/**
 * 门禁气体联动配置保存参数。
 *
 * @param gasLinkageEnabled 是否开启气体联动
 * @param dataValidSeconds 气体数据有效时长，单位秒
 * @param gasConfigs 氧气、二氧化碳、磷化氢配置
 */
public record DoorGasLinkageConfigSaveRequest(
        Boolean gasLinkageEnabled,
        Integer dataValidSeconds,
        List<DoorGasConfigRequest> gasConfigs
) {
}
