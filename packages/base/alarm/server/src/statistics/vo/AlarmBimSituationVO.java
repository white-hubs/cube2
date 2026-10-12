package com.guhecloud.cube.base.alarm.server.statistics.vo;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlarmBimSituationVO {

    /**
     * 报警总数
     */
    private Integer totalNum;

    /**
     * 报警占比
     */
    @Decimal
    private BigDecimal ratio;

    /**
     * 报警已处理数量
     */
    private Integer handlerNum;

    /**
     * 报警处理率
     */
    @Decimal
    private BigDecimal handlerRatio;

    /**
     * 报警响应数(除待确认状态)
     */
    private Integer confirmedNum;

    /**
     * 报警响应率
     */
    @Decimal
    private BigDecimal confirmedRatio;

    /**
     * 报警正确数量
     */
    private Integer actualNum;

    /**
     * 报警正确率
     */
    @Decimal
    private BigDecimal actualRatio;

    /**
     * 报警超时数量
     */
    private Integer timeoutNum;

    /**
     * 报警超时率
     */
    @Decimal
    private BigDecimal timeoutRatio;
}
