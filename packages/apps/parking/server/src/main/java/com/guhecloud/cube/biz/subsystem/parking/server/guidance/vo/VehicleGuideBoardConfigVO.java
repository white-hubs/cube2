package com.guhecloud.cube.biz.subsystem.parking.server.guidance.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingInfoVO;
import lombok.Data;

import java.util.List;

/**
 * 车辆引导牌配置 VO
 */
@Data
public class VehicleGuideBoardConfigVO {

    /**
     * 主键
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
     * 车场信息
     */
    private ParkingInfoVO parkingInfo;
    /**
     * 备注
     */
    private String remark;

    /**
     * 现场图片文件列表（详情接口返回）
     */
    private List<FileVO> sceneImageFiles;

    /**
     * 引导内容配置列表（详情接口返回）
     */
    private List<GuideContentConfigVO> contentConfigs;

    /**
     * 引导内容配置 VO
     */
    @Data
    public static class GuideContentConfigVO {
        /**
         * 主键
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

