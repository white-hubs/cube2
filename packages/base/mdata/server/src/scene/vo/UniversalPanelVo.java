package com.guhecloud.cube.biz.mdata.server.scene.vo;

import lombok.Data;

@Data
public class UniversalPanelVo {
    private Long id;
    /**
     * 所属项目id
     */
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
