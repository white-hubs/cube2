package com.guhecloud.cube.base.alarm.server.flow.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 设备报警处理分析统计返回参数
 *
 * @author lanchun
 * @date 2022/11/10 11:11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlarmHandlerAnalysisStatisticsVO {

    /**
     * 已处理的报警数量
     */
    private Integer alarmHandlerTotalNum;

    /**
     * 未处理的报警数量
     */
    private Integer alarmUnresolvedHandlerTotalNum;

    /**
     * 报警处理前三的用户名称
     */
    private String alarmHandlerTopThreeUserNames;

    /**
     * 处理报警数排名top10
     */
    private List<AlarmHandlerCurrencyStatisticsVO> topTenUserHandlerStatisticsVOS;

    /**
     * 处理报警数排名倒数10
     */
    private List<AlarmHandlerCurrencyStatisticsVO> lastTopTenUserHandlerStatisticsVOS;
}
