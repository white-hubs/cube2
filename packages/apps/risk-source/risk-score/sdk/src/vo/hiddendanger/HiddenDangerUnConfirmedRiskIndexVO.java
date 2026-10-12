package com.guhecloud.cube.srv.gen.riskscore.sdk.vo.hiddendanger;

import com.guhecloud.cube.srv.gen.riskscore.sdk.vo.RiskIndexVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 隐患未响应情况指标
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
public class HiddenDangerUnConfirmedRiskIndexVO extends RiskIndexVO {

    /**
     * 隐患梳理
     */
    private long totalNum;

}
