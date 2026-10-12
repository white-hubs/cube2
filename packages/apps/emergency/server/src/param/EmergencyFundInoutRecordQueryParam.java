package com.guhecloud.cube.biz.emergency.server.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Appet
 * @Description 应急资金收支记录
 * @date 2024-01-22 10:08:36
 */
@Data
public class EmergencyFundInoutRecordQueryParam extends Paging {

    //记录编号
    private String code;
    //记录时间
    private Period period;
    //类型
    private String inoutType;
    //金额
    private BigDecimal inoutAmount;
    //分类标签
    private String inoutTag;

}