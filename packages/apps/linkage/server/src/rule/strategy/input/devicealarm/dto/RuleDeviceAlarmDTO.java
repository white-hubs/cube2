package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicealarm.dto;

import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicealarm.enumeration.RuleConfigTypeEnum;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicecondition.dto.RuleDeviceConditionDTO;
import lombok.Data;

import java.util.List;

/**
 * 设备报警规则配置参数
 *
 * @author lanchun
 * @date 2022/6/22 11:52
 */
@Data
public class RuleDeviceAlarmDTO {

    /**
     * 设备产品品类编号
     */
    private String deviceProdCategoryCode;

    /**
     *
     */
    private String deviceProdCategoryCodeConcat;

    /**
     * 设备产品品类名称
     */
    private String deviceProdCategoryName;

    /**
     * 设备产品主键
     */
    private Long deviceProdId;

    /**
     * 设备产品名称
     */
    private String deviceProdName;

    /**
     * 事件主键
     */
    private Long eventId;

    /**
     * 事件名称
     */
    private String eventName;

    /**
     * 规则类型
     */
    private RuleConfigTypeEnum type;

    /**
     * 属性编号
     */
    private String propCode;

    /**
     * 属性名称
     */
    private String propName;

    /**
     * 属性值
     */
    private String oprand;

    /**
     * 操作符
     */
    private String op;

    /**
     * 状态持续时延
     */
    private Integer delayTime;

    /**
     * 规则设备列表
     */
    private List<RuleDeviceConditionDTO> ruleDeviceAlarmConfigDTOS;
}
