package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.enumeration;

import lombok.Getter;

/**
 * 监测规则下发到iot的状态
 *
 * @author lanchun
 * @date 2024/9/14 15:15
 */
@Getter
public enum RuleIssuedIotStatusEnum {
    NOT_ISSUED("未下发"),
    ISSUED_SUCCESS("下发成功"),
    ISSUED_FAIL("下发失败");

    private final String description;

    RuleIssuedIotStatusEnum(String description) {
        this.description = description;
    }
}
