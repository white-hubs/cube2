package com.guhecloud.cube.biz.vmonitor.server.common.infrastruceture.facade.securitymanage.response;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.std.vo.CustomerTagVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DevicePageInfoVO;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceMonitoringResponse {
    /**
     * 设备id
     */
    private Long deviceId;

    /**
     * 设备
     */
    private String deviceName;

    /**
     * 设备编码
     */
    private String code;

    /**
     * 区域
     */
    private String areaId;

    /**
     * 区域
     */
    private String areaName;

    /**
     * 状态
     */
    private Boolean deviceStatus;

    /**
     * 第三方设备标识
     */
    private String thirdPartyId;

    /**
     * 经纬度
     */
    private String lntAndLat;

    /**
     * 产品信息
     */
    private DeviceProductResponse product;

    /**
     * 标签信息
     */
    private List<DeviceTags> tags;

    @Data
    public static class DeviceProductResponse {
        /**
         * 设备产品主键
         */
        private Long id;

        /**
         * 设备产品名称
         */
        private String name;

        /**
         * 设备图片
         */
        private FileVO image;

        /**
         * 品牌名称
         */
        private String brandName;

        /**
         * 品类信息
         */
        private DeviceProdCategoryResponse prodCategory;

        public static DeviceProductResponse buildDeviceProductResponse(DevicePageInfoVO deviceMonitoringVo) {
            if (Objects.isNull(deviceMonitoringVo.getProductInfo())) {
                return null;
            }
            var deviceProductResponse = new DeviceProductResponse();
            deviceProductResponse.setId(deviceMonitoringVo.getProductInfo().getId());
            deviceProductResponse.setName(deviceMonitoringVo.getProductInfo().getName());
            deviceProductResponse.setBrandName(deviceMonitoringVo.getProductInfo().getBrandName());
            deviceProductResponse.setProdCategory(DeviceProdCategoryResponse.buildDeviceProdCategoryResponse(deviceMonitoringVo
                                                                                                                     .getProductInfo()
                                                                                                                     .getProductCategory()));
            return deviceProductResponse;
        }
    }

    /**
     * 设备产品品类信息
     */
    @Data
    public static class DeviceProdCategoryResponse {
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

        public static DeviceProdCategoryResponse buildDeviceProdCategoryResponse(DeviceProductInfoDTO.ProductCategory productCategory) {
            if (Objects.isNull(productCategory)) {
                return null;
            }
            DeviceProdCategoryResponse deviceProdCategoryResponse = new DeviceProdCategoryResponse();
            deviceProdCategoryResponse.setProdCategoryCode(productCategory.getCode());
            deviceProdCategoryResponse.setProdCategoryCodeConcat(productCategory.getPathCode());
            deviceProdCategoryResponse.setProdCategoryName(productCategory.getName());
            return deviceProdCategoryResponse;
        }
    }

    @Data
    public static class DeviceTags {

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

        public static List<DeviceTags> buildDeviceTags(List<CustomerTagVO> customerTagVOS) {
            return customerTagVOS.stream().map(customerTagVO -> {
                        DeviceTags deviceTags = new DeviceTags();
                        deviceTags.setId(customerTagVO.getId());
                        deviceTags.setTagName(customerTagVO.getTagName());
                        deviceTags.setTagCode(customerTagVO.getTagCode());
                        deviceTags.setDescription(customerTagVO.getDescription());
                        deviceTags.setUpdateTime(customerTagVO.getUpdateTime());
                        deviceTags.setEnableStatus(customerTagVO.getEnableStatus());
                        return deviceTags;
                    })
                    .collect(Collectors.toList());
        }
    }

    public static DeviceMonitoringResponse buildDeviceMonitoringResponse(DevicePageInfoVO deviceMonitoringVo) {
        return DeviceMonitoringResponse.builder()
                .deviceId(deviceMonitoringVo.getId())
                .deviceName(deviceMonitoringVo.getName())
                .code(deviceMonitoringVo.getCode())
                .areaId(Objects.isNull(deviceMonitoringVo.getArea()) ? null : deviceMonitoringVo.getArea().getPathId())
                .areaName(Objects.isNull(deviceMonitoringVo.getArea()) ? null : deviceMonitoringVo.getArea().getPathName())
                .product(DeviceProductResponse.buildDeviceProductResponse(deviceMonitoringVo))
                .deviceStatus(deviceMonitoringVo.getStatus().isOnlineState())
                .thirdPartyId(deviceMonitoringVo.getThirdPartyId())
                .lntAndLat(deviceMonitoringVo.getLntAndLat())
                .build();
    }
}
