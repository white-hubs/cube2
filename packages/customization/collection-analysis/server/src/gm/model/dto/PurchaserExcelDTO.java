package com.guhecloud.cube.biz.collection.analysis.gm.model.dto;

import lombok.Data;

@Data
public class PurchaserExcelDTO {
    private String name;
    /**
     * 性别(SexEnum)
     */
    private String sex;
    private Integer age;
    /**
     * 区域
     */
    private String area;
    /**
     * 经营类型(BusinessScopeEnum)
     */
    private String businessScope;
    /**
     * 采购车类型(VehicleTypeEnum)
     */
    private String vehicleType;
}
