package com.guhecloud.cube.customization.xialing.server.communal.service;

public interface XiaNingVisualizationService {
    /**
     * 获取无人机可视化免密跳转地址。
     *
     * @return 无人机可视化免密跳转地址
     */
    String droneSsoUrl();

    /**
     * 获取指挥调度入口跳转无人机平台的免密跳转地址。
     *
     * @return 指挥调度入口无人机平台免密跳转地址
     */
    String commandDispatchSsoUrl();
}
