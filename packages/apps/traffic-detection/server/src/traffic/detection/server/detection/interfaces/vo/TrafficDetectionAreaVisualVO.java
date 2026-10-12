package com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 人流区域视角配置view对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrafficDetectionAreaVisualVO {

    /**
     * 中心位置
     */
    private String centerAddress;

    /**
     * 视角参数
     */
    private String visualConfig;

    /**
     * 隐藏楼栋
     */
    private String hiddenBuilding;
}