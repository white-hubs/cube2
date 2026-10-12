package com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.enumeration;

/**
 * @Author: lijie
 * @CreateTime: 2024-10-23  11:01
 * @Description:
 * @Version: 1.0
 */
public enum RuleNormTypeEnum {
    NORMAL("正常"),
    SPECIAL ("特殊指标" ),

    ;
    private String remarks;

    RuleNormTypeEnum(String remarks) {
        this.remarks = remarks;
    }
}
