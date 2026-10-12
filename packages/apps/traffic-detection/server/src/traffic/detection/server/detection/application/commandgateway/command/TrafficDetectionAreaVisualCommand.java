package com.guhecloud.cube.biz.traffic.detection.server.detection.application.commandgateway.command;

import lombok.Data;

@Data
public class TrafficDetectionAreaVisualCommand {

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
