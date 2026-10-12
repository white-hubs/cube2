package com.guhecloud.cube.srv.gen.safety.management.riskindex.dto.hiddendanger;

import com.guhecloud.cube.srv.gen.safety.management.riskindex.dto.RiskIndexDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 隐患情况指标
 *
 * @author lanchun
 * @date 2023/11/6 15:42
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class HiddenDangerRiskIndexDTO extends RiskIndexDTO {

    /**
     * 隐患梳理
     */
    private int totalNum;

}
