package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.model;

import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.enums.RiskLevelEnum;

import java.util.Collection;
import java.util.Objects;

public final class RiskLevelRank {
    private RiskLevelRank() {
    }

    public static String highest(Collection<String> levels) {
        if (levels == null) {
            return null;
        }
        return RiskLevelEnum.highest(levels.stream().filter(Objects::nonNull).toList());
    }
}
