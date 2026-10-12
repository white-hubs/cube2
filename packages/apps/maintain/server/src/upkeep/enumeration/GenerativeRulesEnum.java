package com.guhecloud.cube.srv.gen.maintain.server.upkeep.enumeration;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-16  20:02
 * @Description:
 * @Version: 1.0
 */
public enum GenerativeRulesEnum {
    JOB("定时生成"),
    RULE("规则触发");
    private final String description;

    GenerativeRulesEnum(String description) {
        this.description = description;
    }
}
