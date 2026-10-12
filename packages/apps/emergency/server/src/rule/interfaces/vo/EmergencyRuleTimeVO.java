package com.guhecloud.cube.biz.emergency.server.rule.interfaces.vo;

import com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration.EmergencyConditionTimeTypeEnum;
import lombok.Data;

/**
 * 预案生效时段配置返回参数
 *
 * @author lanchun
 * @date 2023/6/6 14:19
 */
@Data
public class EmergencyRuleTimeVO {

    private Long id;

    /**
     * 条件组主键
     */
    private Long groupId;

    /**
     * 时间类型
     */
    private EmergencyConditionTimeTypeEnum timeType;

    /**
     * 时间条件
     */
    private String timeConditions;
}
