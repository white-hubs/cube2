package com.guhecloud.cube.biz.emergency.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Appet
 * @Description 应急资金收支记录
 * @date 2024-01-22 10:08:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EmergencyFundInoutRecord extends BasicEnhancedTenantEntity<Long> {

    //
    private Long id;
    //记录编号
    private String code;
    //记录时间
    private LocalDateTime inoutTime;
    //类型
    private String inoutType;
    //金额
    private BigDecimal inoutAmount;
    //分类标签
    private String inoutTag;
    //备注
    private String remark;

}