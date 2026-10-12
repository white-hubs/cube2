package com.guhecloud.cube.biz.emergency.server.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Appet
 * @Description 应急物资管理
 * @date 2024-01-016 10:08:36
 */
@Data
public class EmergencySuppliesVO {

    //
    private Long id;
    //物资名称
    private String name;
    //物资类型
    private Long type;
    //物资类型名称
    private String typeName;
    //库存数量
    private BigDecimal storeNum;
    //报损数量
    private BigDecimal lossNum;
    //备注
    private String remark;

}