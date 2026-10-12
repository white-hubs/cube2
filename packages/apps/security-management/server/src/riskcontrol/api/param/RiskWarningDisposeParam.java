package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param;

import lombok.Data;

@Data
public class RiskWarningDisposeParam {
    /**
     * 预警处置说明。
     */
    private String disposeDesc;

    /**
     * 预警处置结果。
     */
    private String disposeResult;
}
