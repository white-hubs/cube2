package com.guhecloud.cube.biz.traffic.detection.server.detection.application.commandgateway.command;

import lombok.Data;

import java.util.Set;

@Data
public class IncreaseTrafficDetectionAreaCommand {

    /**
     * 名称
     */
    private String name;

    /**
     * 区域模型
     */
    private String modelArea;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 图片
     */
    private Set<Long> files;

    /**
     * 可视化配置
     */
    private TrafficDetectionAreaVisualCommand visual;
}
