package com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.enumeration;

/**
 * @Author: lijie
 * @CreateTime: 2024-10-23  11:01
 * @Description:
 * @Version: 1.0
 */
public enum RuleNormStatusEnum {
    NORMAL("正常"),
    LOWER_LIMIT ("低于下限" ),
    UPPER_LIMIT ("超过上限" )

    ;
    private String remarks;

    RuleNormStatusEnum(String remarks) {
        this.remarks = remarks;
    }
}
