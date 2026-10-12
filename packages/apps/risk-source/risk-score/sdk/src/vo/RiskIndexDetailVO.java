package com.guhecloud.cube.srv.gen.riskscore.sdk.vo;

import com.guhecloud.cube.srv.gen.riskscore.sdk.vo.alarm.AlarmSituationRiskIndexVO;
import com.guhecloud.cube.srv.gen.riskscore.sdk.vo.alarm.AlarmUnConfirmedRiskIndexVO;
import com.guhecloud.cube.srv.gen.riskscore.sdk.vo.hiddendanger.HiddenDangerRiskIndexVO;
import com.guhecloud.cube.srv.gen.riskscore.sdk.vo.hiddendanger.HiddenDangerUnConfirmedRiskIndexVO;
import com.guhecloud.cube.srv.gen.riskscore.sdk.vo.propertyfiling.PropertyFilingRiskIndexVO;
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
    private AlarmSituationRiskIndexVO alarmSituationRiskIndex;

    /**
     * 报警未响应情况
     */
    private AlarmUnConfirmedRiskIndexVO alarmUnConfirmedRiskIndex;

    /**
     * 隐患情况
     */
    private HiddenDangerRiskIndexVO hiddenDangerRiskIndex;

    /**
     * 隐患未响应情况
     */
    private HiddenDangerUnConfirmedRiskIndexVO hiddenDangerUnConfirmedRiskIndex;

    /**
     * 风险源建档情况
     */
    private PropertyFilingRiskIndexVO propertyFilingRiskIndex;
}
