package com.guhecloud.cube.customization.xialing.server.communal.controller;

import com.guhecloud.cube.customization.xialing.server.communal.service.XiaNingVisualizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 可视化控制层
 */
@RestController
@RequiredArgsConstructor
public class XiaNingVisualizationController {
    private final XiaNingVisualizationService xiaNingVisualizationService;

    /**
     * 获取无人机免密跳转地址
     *
     * @return 免密跳转地址
     */
    @GetMapping("/visualization/drone/sso-url")
    public String droneSsoUrl() {
        return xiaNingVisualizationService.droneSsoUrl();
    }

    /**
     * 获取指挥调度入口跳转无人机平台的免密跳转地址
     *
     * @return 指挥调度入口无人机平台免密跳转地址
     */
    @GetMapping("/visualization/command-dispatch/sso-url")
    public String commandDispatchSsoUrl() {
        return xiaNingVisualizationService.commandDispatchSsoUrl();
    }
}
