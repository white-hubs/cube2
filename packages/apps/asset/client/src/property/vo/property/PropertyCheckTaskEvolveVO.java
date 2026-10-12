package com.guhecloud.cube.property.management.client.property.vo.property;

import lombok.Data;

@Data
public class PropertyCheckTaskEvolveVO {
    /**
     * 盘点总数
     */
    private Integer checkTotalNumber;
    /**
     * 待盘点
     */
    private Long checkUndiskNumber;
    /**
     * 已盘点
     */
    private Integer checkCompleteNumber;
    /**
     * 盘赢
     */
    private Integer checkWinNumber;
    /**
     * 盘亏
     */
    private Integer checkLossNumber;
    /**
     * 异常
     */
    private Long abnormalNumber = 0L;
}
