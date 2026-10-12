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
 * 报警响应情况得分
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AlarmUnConfirmedRiskIndexDTO extends RiskIndexDTO {

    /**
     * 级别得分情况
     */
    private List<AlarmLevelRiskIndexDTO> alarmLevelIndexDetails;
}
