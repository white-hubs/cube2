package com.guhecloud.cube.srv.gen.work.permit.server.param;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 作业类型配置保存参数。
 */
@Data
public class WorkPermitTypeConfigParam {
    /**
     * 作业类型名称。
     */
    @NotBlank(message = "作业类型名称不能为空")
    private String typeName;
    /**
     * 作业等级。
     */
    @NotBlank(message = "作业等级不能为空")
    private String workPermitLevel;
    /**
     * 等级定义。
     */
    @NotBlank(message = "等级定义不能为空")
    private String levelDefinition;
    /**
     * 作业票有效时长，单位小时。
     */
    @NotNull(message = "作业票有效时长不能为空")
    private Integer permitValidHours;
    /**
     * 作业风险检查项。
     */
    @Valid
    private List<WorkPermitAssociatedItemParam> riskCheckItems = new ArrayList<>();
    /**
     * 作业验收项。
     */
    @Valid
    private List<WorkPermitAssociatedItemParam> acceptanceItems = new ArrayList<>();
}
