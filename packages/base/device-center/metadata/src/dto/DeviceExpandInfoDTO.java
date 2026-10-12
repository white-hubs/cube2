package com.guhecloud.cube.base.device.center.metadata.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author pengjian
 * @date 2024/1/10 10:32
 * @description 设备扩展信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceExpandInfoDTO {
    /**
     * 设备id
     */
    private Long id;
    /**
     * 经纬度
     */
    private String lntAndLat;

    /**
     * 位置描述信息
     */
    private String locationDes;

    /**
     * 空间坐标信息
     */
    private String spatialCoordinates;
    /**
     * 采购信息
     */
    private PurchaseInfo purchaseInfo;
    /**
     * 出厂信息
     */
    private FactoryInfo factoryInfo;

    @Data
    public static class PurchaseInfo {
        /**
         * 采购单价
         */
        private String unitPrice;
        /**
         * 采购日期
         */
        private LocalDateTime purchaseDate;
    }

    @Data
    public static class FactoryInfo {
        /**
         * 生产日期
         */
        private LocalDateTime productionDate;
        /**
         * 使用寿命
         */
        private Integer usefulLife;
    }
}
