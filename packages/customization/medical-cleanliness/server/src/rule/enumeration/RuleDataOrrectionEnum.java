package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.enumeration;

/**
 * @Author: lijie
 * @CreateTime: 2024-06-21  15:38
 * @Description:
 * @Version: 1.0
 */
public enum RuleDataOrrectionEnum {
    INVAILD("无效"),
    VALID("有效"),
    ;
    private String desc;

    RuleDataOrrectionEnum(String desc) {
        this.desc = desc;
    }
}
