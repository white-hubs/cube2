package com.guhecloud.cube.biz.emergency.server.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Appet
 * @Description 应急物资出入库记录
 * @date 2024-01-016 10:08:36
 */
@Data
public class EmergencySuppliesInoutRecordVO {

    //
    private Long id;
    //应急物资ID
    private Long suppliesId;
    //出入库记录号
    private String code;
    //出入库时间
    private LocalDateTime inoutTime;
    //出入库类型
    private String inoutType;
    //出入库标签
    private String inoutTag;
    //数量
    private BigDecimal inoutNum;
    //单位
    private String inoutUnit;
    //备注
    private String remark;

}