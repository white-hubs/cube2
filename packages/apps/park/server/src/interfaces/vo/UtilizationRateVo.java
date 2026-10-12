package com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UtilizationRateVo {
    //区域id
    private String areaId;
    //区域
    private String areaName;
    //使用面积
    private BigDecimal useArea;
    //使用面积占比
    private BigDecimal useAreaRate;
    //停用面积
    private BigDecimal disabledArea;
    //停用面积占比
    private BigDecimal disabledAreaRate;
    //总计面积
    private BigDecimal total;
    //名称
    private String name;
}
