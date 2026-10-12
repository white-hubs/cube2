package com.guhecloud.cube.srv.gen.work.permit.server.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 作业关联项类型。
 */
@Getter
@AllArgsConstructor
public enum WorkPermitAssociatedItemTypeEnum {
    /**
     * 作业风险检查项。
     */
    RISK_CHECK("RISK_CHECK", "作业风险检查项"),
    /**
     * 作业验收项。
     */
    ACCEPTANCE("ACCEPTANCE", "作业验收项");

    private final String code;
    private final String name;
}
