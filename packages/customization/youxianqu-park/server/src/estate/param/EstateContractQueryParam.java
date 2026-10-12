package com.guhecloud.cube.customized.youxianqu.park.management.server.estate.param;

import com.guhecloud.cube.customized.youxianqu.park.management.server.common.vo.param.TrendQueryPageBaseParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 物业合同查询对象
 */
@Data

@EqualsAndHashCode(callSuper = true)
public class EstateContractQueryParam extends TrendQueryPageBaseParam {

    /**
     * 预警天数条件(ConditionTypeEnum)
     */
    private String warnCondition;
    /**
     * 合同预警天数
     */
    private Integer warnDay;

    /**
     * 合同逾期天数条件(ConditionTypeEnum)
     */
    private String overdueCondition;
    /**
     * 合同逾期天数(库中负数未预期)
     */
    private Integer overdueDay;

}
