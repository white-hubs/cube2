package com.guhecloud.cube.srv.gen.riskscore.sdk.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RiskIndexVO implements Serializable {

    /**
     * 风险指数得分
     */
    private int riskIndex;

    /**
     * 租户编号
     */
    private Integer tenant;
}
