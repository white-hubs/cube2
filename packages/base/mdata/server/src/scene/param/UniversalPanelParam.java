package com.guhecloud.cube.biz.mdata.server.scene.param;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
public class UniversalPanelParam {

    /**
     * 所属项目id
     */
    @NotNull(message = "所属项目不能为空")
    private Long projectId;
    private String tableOneName;
    private String tableOne;
    private String tableTwoName;
    private String tableTwo;
    private String tableThreeName;
    private String tableThree;
    private String items;
    private Long businessId;
}
