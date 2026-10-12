package com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.command;

import com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration.LogicalOpEnum;
import com.guhecloud.cube.biz.linkage.client.param.action.ActionParam;
import com.guhecloud.cube.biz.linkage.client.param.before.ActionBeforeParam;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 新增预案规则命令
 *
 * @author lanchun
 * @date 2022/9/3 14:23
 */
@Data
public class ChangeEmergencyRuleCommand {

    private Long id;

    @Size(max = 100, message = "预案规则名称不能大于100")
    private String name;

    /**
     * 预案级别主键
     */
    @NotNull(message = "预案级别主键不能为空")
    private Long alarmLevelId;

    /**
     * 事件分类编号
     */
    @NotEmpty(message = "事件分类不能为空")
    @Size(max = 50, message = "事件分类不能大于50")
    private String emergencyEventTypeCode;

    /**
     * 应用编号
     */
    @NotEmpty(message = "应用编号不能为空")
    private String applicationCode;

    /**
     * 是否启用
     */
    @NotNull(message = "是否启用不能为空")
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
     * 备注信息
     */
    @Size(max = 200, message = "备注信息不能大于200")
    private String remark;

    /**
     * 是否指定条件设备
     */
    @NotNull(message = "是否指定条件设备不能为空")
    private Boolean appointDevice;

    /**
     * 预案条件组
     */
    @Valid
    @NotNull(message = "触发条件不能为空")
    @Size(min = 1, message = "触发条件不能为空")
    private List<ChangeEmergencyRuleConditionGroupCommand> emergencyRuleConditionGroups;

    /**
     * 预案条件设备列表
     */
    private List<ChangeEmergencyRuleConditionDeviceCommand> emergencyRuleConditionDevices;

    /**
     * 预案动作列表
     */
    @NotNull(message = "预案动作列表不能为空")
    @Size(min = 1, message = "预案动作数量不能小于1")
    @Valid
    private List<ActionParam> emergencyActions;

    /**
     * 预案动作前置配置
     */
    @Valid
    private List<ActionBeforeParam> emergencyActionBeforeList;

    /**
     * 是否推送到调度中心
     */
    private Boolean notifyCommandDispatch;

    /**
     * 是否开启警告声音
     */
    private Boolean turnWarningSound;
}
