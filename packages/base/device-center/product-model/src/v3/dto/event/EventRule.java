package com.guhecloud.cube.base.iot.product.model.v3.dto.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.guhecloud.cube.base.iot.product.model.v3.enums.RuleOperatorEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 事件规则配置
 * <p>
 * 定义简单规则事件的触发条件
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class EventRule {

    /**
     * 属性编码
     * 指定要比较的属性
     */
    private String propCode;

    /**
     * 运算符
     * 比较运算的操作符,如: GT(大于)、EQ(等于)、IN(包含于)等
     */
    private RuleOperatorEnum op;

    /**
     * 操作数
     * 用于比较的值,支持多种类型:
     * - 单个值: 用于 GT、LT、EQ 等比较运算符
     * - 数组: 用于 IN、NOT_IN 等集合运算符,或 BETWEEN 区间运算符
     * 示例:
     * - GT 比较: oprand = 0
     * - IN 集合: oprand = [1, 2, 3]
     * - BETWEEN 区间: oprand = [10, 100]
     */
    private Object oprand;

    /**
     * 延迟时间(毫秒)
     * 规则满足后延迟多久触发事件,单位:毫秒
     * 可选字段,默认为0表示立即触发
     */
    private Integer delayTime;
}
