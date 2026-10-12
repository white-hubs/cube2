package com.guhecloud.cube.srv.gen.safety.management.riskindex.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RiskIndexDTO implements Serializable {

    /**
     * 风险指数得分
     */
    private int riskIndex;
}
