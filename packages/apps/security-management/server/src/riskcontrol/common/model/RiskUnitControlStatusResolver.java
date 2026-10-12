package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.model;

import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.enums.RiskObjectStatusEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.identify.entity.RiskObjectEntity;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

public final class RiskUnitControlStatusResolver {
    private static final List<RiskObjectStatusEnum> PRIORITY = List.of(
            RiskObjectStatusEnum.PENDING_REVIEW,
            RiskObjectStatusEnum.DRAFT,
            RiskObjectStatusEnum.RE_EVAL,
            RiskObjectStatusEnum.INHERENT_EVAL,
            RiskObjectStatusEnum.CONTROL_EVAL,
            RiskObjectStatusEnum.CONTROL_SET,
            RiskObjectStatusEnum.EXECUTING,
            RiskObjectStatusEnum.ACTIVE
    );

    private RiskUnitControlStatusResolver() {
    }

    public static String resolve(List<RiskObjectEntity> objects) {
        if (CollectionUtils.isEmpty(objects)) {
            return null;
        }
        for (RiskObjectStatusEnum status : PRIORITY) {
            boolean matched = objects.stream().anyMatch(object -> Objects.equals(object.getStatus(), status.name()));
            if (matched) {
                return status.name();
            }
        }
        return objects.get(0).getStatus();
    }
}
