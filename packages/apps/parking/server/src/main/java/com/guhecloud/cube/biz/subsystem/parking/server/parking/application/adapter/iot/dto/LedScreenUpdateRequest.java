package com.guhecloud.cube.biz.subsystem.parking.server.parking.application.adapter.iot.dto;

import lombok.Data;
import java.util.List;

/**
 * LED屏幕更新请求
 */
@Data
public class LedScreenUpdateRequest {
    /**
     * 控制器URL
     */
    private String controllerUrl;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 区域列表
     */
    private List<AreaInfo> areas;

    /**
     * 展示模式
     */
    private String displayMode;

    /**
     * 是否清屏幕
     */
    private Boolean clearScreen;

    /**
     * 区域信息
     */
    @Data
    public static class AreaInfo {
        /**
         * 区域ID
         */
        private Integer areaId;

        /**
         * 内容
         */
        private String content;
    }
}
