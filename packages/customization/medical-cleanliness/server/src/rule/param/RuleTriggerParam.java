package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleTriggerParam {

    /**
     * 规则主键
     */
    private Long ruleId;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * iot实例主键
     */
    private String iotInstanceId;

    /**
     * 产品id
     */
    private Long productId;

    /**
     * 触发设备id
     */
    private Long deviceId;

    /**
     * 触发设备名称
     */
    private String deviceName;

    /**
     * 触发部门主键
     */
    private Long departId;

    /**
     * 触发部门名称
     */
    private String departName;

    /**
     * 指标记录信息
     */
    private List<RuleTriggerNormParam> norms;
}
