package com.guhecloud.cube.biz.mdata.server.scene.param;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

import java.util.List;

@Data
public class AreaScenePanelConfigBatchModifyParam {

    /**
     * 区域主键
     */
    @NotNull(message = "区域主键不能小于1")
    private Long areaId;

    /**
     * 场景主键
     */
    @NotNull(message = "场景主键不能小于1")
    private Long sceneType;

    /**
     * 面板配置
     */
    private List<AreaScenePanelConfigModifyParam> panelConfigModifyParams;
}
