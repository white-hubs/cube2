package com.guhecloud.cube.biz.mdata.server.supplier.param;

import com.guhecloud.cube.biz.mdata.server.supplier.enumeration.RiskLevelEnum;
import lombok.Data;

/**
 * 风险级别新增
 */
@Data
public class RiskLevelCreateParam {

    /**
     * 风险级别类别
     */
    private RiskLevelEnum type;
    /**
     * 风险级别数值
     */
    private String value;
}
