package com.guhecloud.cube.srv.gen.safety.management.dto.vo;

import com.guhecloud.cube.srv.gen.safety.management.riskindex.dto.alarm.AlarmSituationRiskIndexDTO;
import com.guhecloud.cube.srv.gen.safety.management.riskindex.dto.alarm.AlarmUnConfirmedRiskIndexDTO;
import com.guhecloud.cube.srv.gen.safety.management.riskindex.dto.hiddendanger.HiddenDangerRiskIndexDTO;
import com.guhecloud.cube.srv.gen.safety.management.riskindex.dto.hiddendanger.HiddenDangerUnConfirmedRiskIndexDTO;
import com.guhecloud.cube.srv.gen.safety.management.riskindex.dto.propertyfiling.PropertyFilingRiskIndexDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 指标详情
 *
 * @author lanchun
 * @date 2023/11/6 15:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RiskIndexDetailVO {

    /**
     * 风险指数得分
     */
    private int riskIndex;

    /**
     * 报警情况
     */
    private AlarmSituationRiskIndexDTO alarmSituationRiskIndex;

    /**
     * 报警未响应情况
     */
    private AlarmUnConfirmedRiskIndexDTO alarmUnConfirmedRiskIndex;

    /**
     * 隐患情况
     */
    private HiddenDangerRiskIndexDTO hiddenDangerRiskIndex;

    /**
     * 隐患未响应情况
     */
    private HiddenDangerUnConfirmedRiskIndexDTO hiddenDangerUnConfirmedRiskIndex;

    /**
     * 风险源建档情况
     */
    private PropertyFilingRiskIndexDTO propertyFilingRiskIndex;
}
