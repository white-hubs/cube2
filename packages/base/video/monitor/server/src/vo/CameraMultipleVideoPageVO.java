package com.guhecloud.cube.biz.vmonitor.server.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CameraMultipleVideoPageVO {

    private Long id;
    /**
     * 设备实例编码
     */
    private String code;

    /**
     * 设备实例名称
     */
    private String name;

    /**
     * 区域名称
     */
    private String areaName;

    private String areaId;

    /**
     * 在线状态
     */
    private Boolean deviceStatus;

    /**
     * 第三方设备标识
     */
    private String thirdPartyId;

    /**
     * 产品信息
     */
    private DeviceProductVO product;

    /**
     * 流媒体信息
     */
    private StreamMediaVO streamMedia;

    /**
     * 是否收藏
     */
    private Boolean collected;

    /**
     * 设备标签列表
     */
    private List<DeviceTagVO> tags;

    @Data
    public static class DeviceProductVO {
        /**
         * 设备产品主键
         */
        private Long id;

        /**
         * 设备产品名称
         */
        private String name;

        /**
         * 产品图片
         */
        private FileVO image;

        /**
         * 品牌名称
         */
        private String brandName;

        /**
         * 品类信息
         */
        private DeviceProdCategoryVO prodCategory;
    }

    /**
     * 设备产品品类信息
     */
    @Data
    public static class DeviceProdCategoryVO {
        /**
         * 产品品类编号
         */
        private String prodCategoryCode;

        /**
         * 产品品类编号序列号
         */
        private String prodCategoryCodeConcat;

        /**
         * 产品品类名称
         */
        private String prodCategoryName;
    }

    @Data
    public static class DeviceTagVO {

        private Long id;
        /**
         * 标签名称
         */
        private String tagName;
        /**
         * 标签code
         */
        private String tagCode;
        /**
         * 标签描述
         */
        private String description;
        /**
         * 编辑时间
         */
        private LocalDateTime updateTime;

        /**
         * 状态
         */
        private Boolean enableStatus;
    }
}
