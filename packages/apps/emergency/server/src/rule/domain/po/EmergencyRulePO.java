package com.guhecloud.cube.biz.emergency.server.rule.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration.LogicalOpEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 预案规则实体对象
 *
 * @author lanchun
 * @date 2022/9/3 10:47
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("emergency_rule")
public class EmergencyRulePO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    private String name;

    /**
     * 预案级别主键
     */
    private Long alarmLevelId;

    /**
     * 预案事件分类编号
     */
    private String emergencyEventTypeCode;

    /**
     * 预案类型主键
     */
    private Long emergencyTypeId;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 去重时间
     */
    private Integer repeatTime;

    /**
     * 去重时间单位
     */
    private String repeatTimeUnit;

    /**
     * 预计处理时间
     */
    private Integer estimatedHandlerTime;

    /**
     * 预计处理时间单位
     */
    private String estimatedHandlerUnit;

    /**
     * 逻辑运算符(or and)
     */
    private LogicalOpEnum logicalOp;

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 是否指定条件设备
     */
    private Boolean appointDevice;

    /**
     * 是否推送到调度中心
     */
    private Boolean notifyCommandDispatch;

    /**
     * 是否开启警告声音
     */
    private Boolean turnWarningSound;
}
