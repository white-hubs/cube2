package com.guhecloud.cube.biz.linkage.client.param;

import com.guhecloud.apps.linkage.metadata.enumeration.BusinessTypeEnum;
import com.guhecloud.apps.linkage.metadata.enumeration.DateUnitEnum;
import com.guhecloud.apps.linkage.metadata.enumeration.RuleTypeEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 规则基础信息保存参数
 *
 * @author lanchun
 * @date 2022/6/29 15:40
 */
@Data
public class RuleBasicModifyParam {

    /**
     * 规则编号
     */
    @Size(max = 50, message = "规则编号不能大于50")
    private String code;

    /**
     * 规则名称
     */
    @NotEmpty(message = "规则名称不能为空")
    @Size(max = 50, message = "规则名称不能大于50")
    private String name;

    /**
     * 应用编号
     */
    @NotEmpty(message = "应用编号不能为空")
    @Size(max = 50, message = "应用编号不能大于50")
    private String applicationCode;

    /**
     * 应用名称
     */
    @NotEmpty(message = "应用名称不能为空")
    @Size(max = 50, message = "应用名称不能大于50")
    private String applicationName;

    /**
     * 规则类型
     */
    @NotNull(message = "规则类型不能为空")
    private RuleTypeEnum type;

    /**
     * 业务类型
     */
    @NotNull(message = "业务类型不能为空")
    private BusinessTypeEnum businessType;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 是否去重
     */
    private Boolean reiterated;

    /**
     * 去重时间
     */
    private Integer repeatTime;

    /**
     * 去重时间单位
     */
    private DateUnitEnum repeatTimeUnit;

    /**
     * 触发级别主键
     */
    private Long triggerLevelId;

    /**
     * 触发级别名称
     */
    private String triggerLevelName;

    /**
     * 触发类型主键
     */
    private Long triggerTypeId;

    /**
     * 触发类型名称
     */
    private String triggerTypeName;
}
