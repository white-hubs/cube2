package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-06-21  14:58
 * @Description:
 * @Version: 1.0
 */
@Data
public class RuleDataOrrectionPageParam extends Paging {
    /**
     * 设备id
     */
    private String deviceId;
    /**
     * 指标编码
     */
    private String ruleNormCode;
    /**
     * 状态
     */
    private String status;
}
