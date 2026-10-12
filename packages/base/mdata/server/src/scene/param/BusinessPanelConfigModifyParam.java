package com.guhecloud.cube.biz.mdata.server.scene.param;

import com.guhecloud.cube.biz.mdata.server.scene.enumeration.BusinessPanelConfigTypeEnum;
import com.guhecloud.cube.biz.mdata.server.scene.enumeration.BusinessPanelPositionEnum;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class BusinessPanelConfigModifyParam {

    /**
     * 所属项目id
     */
    @NotNull(message = "所属项目不能为空")
    private Long projectId;

    /**
     * 业务类型
     */
    @NotNull(message = "业务类型不能为空")
    private BusinessPanelConfigTypeEnum businessType;

    /**
     * 业务主键
     */
    @NotNull(message = "业务主键不能为空")
    @Min(value = 1, message = "业务主键不能小于1")
    private Long businessId;

    /**
     * 面板定位
     */
    private BusinessPanelPositionEnum panelPosition = BusinessPanelPositionEnum.MIDDLE_PANEL;

    /**
     * 面板配置
     */
    @NotEmpty(message = "面板配置不能为空")
    @Size(max = 1000, message = "面板配置不能大于1000")
    private String panelConfig;

    private UniversalPanelParam universalPanelParam;
}
