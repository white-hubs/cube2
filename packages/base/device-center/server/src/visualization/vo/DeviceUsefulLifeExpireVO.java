package com.guhecloud.cube.base.device.center.visualization.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeviceUsefulLifeExpireVO {

    private Long id;

    /**
     * 设备编号
     */
    private String code;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 产品品类编号
     */
    private String prodCategoryCode;

    /**
     * 产品品类名称
     */
    private String prodCategoryName;

    /**
     * 使用寿命
     */
    private Integer usefulLife;

    /**
     * 使用寿命年限
     */
    private String usefulLifeYear;

    /**
     * 过期状态
     */
    private String expireStatus;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 采购时间
     */
    private LocalDateTime purchaseDate;

}
