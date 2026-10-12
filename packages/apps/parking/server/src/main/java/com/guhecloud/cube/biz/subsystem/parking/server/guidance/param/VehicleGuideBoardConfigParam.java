package com.guhecloud.cube.biz.subsystem.parking.server.guidance.param;

import lombok.Data;

import java.util.List;

/**
 * 车辆引导牌配置参数
 */
@Data
public class VehicleGuideBoardConfigParam {

    /**
     * 主键（更新时必传）
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 编码
     */
    private String code;

    /**
     * 现场图片文件id（多个，逗号分隔）
     */
    private String sceneImageIds;

    /**
     * 安装位置
     */
    private String installationLocation;

    /**
     * 关联车场id
     */
    private Long parkingLotId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 引导内容配置列表
     */
    private List<GuideContentConfigParam> contentConfigs;

    /**
     * 引导内容配置参数
     */
    @Data
    public static class GuideContentConfigParam {
        /**
         * 主键（更新时必传）
         */
        private Long id;

        /**
         * 内容名称
         */
        private String contentName;

        /**
         * 优先级
         */
        private Integer priority;

        /**
         * 绑定字段
         */
        private String boundField;

        /**
         * 字体大小
         */
        private Integer fontSize;

        /**
         * 字体颜色
         */
        private String fontColor;
    }
}

