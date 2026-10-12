package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: lijie
 * @CreateTime: 2024-06-21  14:58
 * @Description:
 * @Version: 1.0
 */
@Data
public class RuleDataOrrectionVO {
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
    private String createBy;
    private String createByName;
    private LocalDateTime createTime;
}
