package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.facade.iot.dto.device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceInfoDTO {

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
     * 区域主键
     */
    private String areaId;

    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 产品品类编号
     */
    private String prodCategoryCode;

    /**
     * 产品品类名称
     */
    private String prodCategoryName;

    /**
     * 产品主键
     */
    private Long prodId;

    /**
     * 产品名称
     */
    private String prodName;

    /**
     * 部门主键
     */
    private Long departmentId;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 经纬度
     */
    private String lntAndLat;

    /**
     * 位置描述
     */
    private String locationDes;
}
