package com.guhecloud.cube.biz.daily.task.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyTargetDTO {
    private Long id;
    /**
     * 对象类型
     */
    private String targetCategory;
    /**
     * 对象编码
     */
    private String targetCode;
    /**
     * 对象名称
     */
    private String targetName;
    /**
     * 所属区域id
     */
    private String areaId;
    /**
     * 所属区域名称
     */
    private String areaName;
    /**
     * 详细地址
     */
    private String location;
    /**
     * 经纬度
     */
    private String lntAndLat;
}
