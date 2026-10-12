package com.guhecloud.cube.biz.emergency.server.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Appet
 * @Description 应急资金收支记录
 * @date 2024-01-22 10:08:36
 */
@Data
public class EmergencyFundInoutRecordVO {

    //
    private Long id;
    //记录编号
    private String code;
    //记录时间
    private LocalDateTime inoutTime;
    //类型 IN入 OUT出
    private String inoutType;
    //金额
    private BigDecimal inoutAmount;
    //分类标签
    private String inoutTag;
    //备注
    private String remark;

}