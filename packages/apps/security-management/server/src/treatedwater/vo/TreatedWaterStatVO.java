package com.guhecloud.cube.srv.gen.securitymanagement.server.treatedwater.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Appet
 * @Description 净化水统计
 * @date 2023-10-30 15:08:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreatedWaterStatVO {
    //类型编号
    private String waterType;
    //类型名称
    private String waterTypeName;
    //用量
    private BigDecimal totalUsage;
    //时间
    private String dateHour;
    //区域名称
    private String areaName;

    public TreatedWaterStatVO(String dateHour, BigDecimal totalUsage) {
        this.totalUsage = totalUsage;
        this.dateHour = dateHour;
    }
}