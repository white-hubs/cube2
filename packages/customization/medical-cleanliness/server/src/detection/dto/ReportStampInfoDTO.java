package com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.dto;

import lombok.Data;

import java.util.List;

/**
 * 报告采样信息
 */
@Data
public class ReportStampInfoDTO {
    /**
     * 设备id
     */
    private Long deviceId;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 设备校验是否合格
     */
    private Boolean qualifiedBool;
    /**
     * 每个属性校验数据
     */
    private List<PropCheck> propCheckList;

    @Data
    public static class PropCheck {
        /**
         * 属性code
         */
        private String propCode;
        /**
         * 属性名称
         */
        private String propName;
        /**
         * 单位名称
         */
        private String unitName;
        /**
         * 检测结果
         */
        private String value;
        /**
         * 技术要求
         */
        private String maxMin;
        private String max;
        private String min;
        /**
         * 单项结论
         */
        private String result;
    }
}
