package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 报警处理分析统计
 *
 * @author lanchun
 * @date 2024/4/7 10:15
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlarmHandlerSituationVO {

    /**
     * 报警处理数
     */
    private Integer handlerNum;

    /**
     * 报警处理率
     */
    @Decimal
    private BigDecimal handlerRatio;

    /**
     * 报警处理环比率
     */
    @Decimal
    private BigDecimal handlerChainRatio;

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
     * 报警响应环比率
     */
    @Decimal
    private BigDecimal confirmedChainRatio;

    /**
     * 报警未响应数
     */
    private Integer unConfirmedNum;

    /**
     * 报警未响应率
     */
    @Decimal
    private BigDecimal unConfirmedRatio;

    /**
     * 报警未响应环比率
     */
    @Decimal
    private BigDecimal unConfirmedChainRatio;

    /**
     * 报警超时数量
     */
    private Integer timeoutNum;

    /**
     * 报警超时率
     */
    @Decimal
    private BigDecimal timeoutRatio;

    /**
     * 报警超时环比率
     */
    private BigDecimal timeoutChainRatio;

    /**
     * 报警超时未处理数量
     */
    private Integer timeoutUnHandlerNum;

    /**
     * 报警超时率
     */
    @Decimal
    private BigDecimal timeoutUnHandlerRatio;

    /**
     * 报警超时环比率
     */
    private BigDecimal timeoutUnHandlerChainRatio;

    /**
     * 报警超时处理数量
     */
    private Integer timeoutHandlerNum;

    /**
     * 报警超时处理率
     */
    @Decimal
    private BigDecimal timeoutHandlerRatio;

    /**
     * 报警超时处理环比率
     */
    private BigDecimal timeoutHandlerChainRatio;
}
