package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.param;

import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-06-21  14:58
 * @Description:
 * @Version: 1.0
 */
@Data
public class RuleDataOrrectionParam {
    private Long id;
    /**
     * 设备id
     */
    private String deviceIds;
    /**
     * 指标编码
     */
    private String ruleNormCode;
    /**
     * 值
     */
    private String value;
    /**
     * 状态
     */
    private String status;
}
