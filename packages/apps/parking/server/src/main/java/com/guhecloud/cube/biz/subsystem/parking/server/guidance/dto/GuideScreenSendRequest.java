package com.guhecloud.cube.biz.subsystem.parking.server.guidance.dto;

import lombok.Data;

/**
 * 引导屏发送请求DTO
 */
@Data
public class GuideScreenSendRequest {

    /**
     * 内容
     */
    private String content;

    /**
     * 区域参数
     */
    private AreaParams areaParams;

    /**
     * 区域参数内部类
     */
    @Data
    public static class AreaParams {
        /**
         * 动态区域位置
         */
        private Integer dynamicAreaLoc;

        /**
         * 字体大小
         */
        private Integer fontSize;

        /**
         * 颜色（RGB转整数，如16777215表示白色）
         */
        private Integer color;
    }
}
