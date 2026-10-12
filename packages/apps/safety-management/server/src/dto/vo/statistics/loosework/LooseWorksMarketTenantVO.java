package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.loosework;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-24  16:58
 * @Description:
 * @Version: 1.0
 */
@Data
public class LooseWorksMarketTenantVO {
    /**
     * 市场租户编号
     */
    private Integer marketTenant;
    /**
     * 市场名称
     */
    private String marketName;
    /**
     * 占比
     */
    private BigDecimal percentage;
    private long value;
    /**
     * 小散状态数
     */
    private List<LooseWorksStatusVO> statusList;
}
