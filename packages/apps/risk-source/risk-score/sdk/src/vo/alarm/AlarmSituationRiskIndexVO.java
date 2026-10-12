package com.guhecloud.cube.srv.gen.riskscore.sdk.vo.alarm;

import com.guhecloud.cube.srv.gen.riskscore.sdk.vo.RiskIndexVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * 报警情况得分
 *
 * @author lanchun
 * @date 2023/11/6 15:36
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AlarmSituationRiskIndexVO extends RiskIndexVO {

    /**
     * 级别得分情况
     */
    private List<AlarmLevelRiskIndexVO> alarmLevelIndexDetails;

}
