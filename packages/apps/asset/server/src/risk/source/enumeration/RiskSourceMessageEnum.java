package com.guhecloud.cube.srv.gen.property.management.server.risk.source.enumeration;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-02-01  10:20
 * @Description:
 * @Version: 1.0
 */
@Getter
public enum RiskSourceMessageEnum {
    RISK_SOURCE_OVERDUE_ABOUT("即将过期"),
    RISK_SOURCE_OVERDUE("已过期"),
    MANDATORY_TESTING_OVERDUE_ABOUT("强检到期"),
    ;
    private final String remarks;

    RiskSourceMessageEnum(String remarks) {
        this.remarks = remarks;
    }

    public static List<String> getNameList() {
        List<String> nameList = new ArrayList<>();
        nameList.add(RiskSourceMessageEnum.RISK_SOURCE_OVERDUE_ABOUT.name());
        nameList.add(RiskSourceMessageEnum.RISK_SOURCE_OVERDUE.name());
        nameList.add(RiskSourceMessageEnum.MANDATORY_TESTING_OVERDUE_ABOUT.name());
        return nameList;
    }
}
