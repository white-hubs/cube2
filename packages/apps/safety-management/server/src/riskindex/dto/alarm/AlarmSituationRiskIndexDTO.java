package com.guhecloud.cube.srv.gen.safety.management.riskindex.dto.alarm;

import com.guhecloud.cube.srv.gen.safety.management.riskindex.dto.RiskIndexDTO;
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
public class AlarmSituationRiskIndexDTO extends RiskIndexDTO {

    /**
     * 级别得分情况
     */
    private List<AlarmLevelRiskIndexDTO> alarmLevelIndexDetails;
}
