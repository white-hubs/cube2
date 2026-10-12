package com.guhecloud.cube.base.device.center.visualization.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 设备使用寿命到期统计
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceUsefulLifeExpireChainVO {

    /**
     * 总数
     */
    private Integer totalNumber;

    /**
     * 环比率
     */
    private float chainRatio;

}
