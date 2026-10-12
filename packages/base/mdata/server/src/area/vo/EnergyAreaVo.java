package com.guhecloud.cube.biz.mdata.server.area.vo;

import lombok.Data;

@Data
public class EnergyAreaVo {
    private String id;
    private String areaName;
    private String code;
    private String parentId;

    /**
     * 序号
     */
    private Integer sort;
}
