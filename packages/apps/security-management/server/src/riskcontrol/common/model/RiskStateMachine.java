package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.model;

import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.enums.RiskObjectStatusEnum;
import com.guhecloud.platform.base.standard.exception.Assert;

public final class RiskStateMachine {
    private RiskStateMachine() {
    }

    public static void require(RiskObjectStatusEnum actual, RiskObjectStatusEnum expected, String message) {
        Assert.isNotAcceptable(actual != expected, message);
    }
}
