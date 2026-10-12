package com.guhecloud.cube.biz.mdata.server.scene.param;

import com.guhecloud.cube.biz.mdata.server.scene.enumeration.BusinessPanelPositionEnum;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class AreaScenePanelConfigModifyParam {

    /**
     * 面板定位
     */
    private BusinessPanelPositionEnum panelPosition = BusinessPanelPositionEnum.MIDDLE_PANEL;

    /**
     * 所属项目id
     */
    @NotNull(message = "所属项目不能为空")
    private Long projectId;

    /**
     * 面板配置
     */
    @NotEmpty(message = "面板配置不能为空")
    @Size(max = 1000, message = "面板配置不能大于1000")
    private String panelConfig;

    private UniversalPanelParam universalPanelParam;
}
