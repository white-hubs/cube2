package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param;

import lombok.Data;

@Data
public class RiskEnabledParam {
    /**
     * 启用状态，true 表示启用，false 表示停用。
     */
    private Boolean enabled;
}
