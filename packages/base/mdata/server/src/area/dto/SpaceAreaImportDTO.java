package com.guhecloud.cube.biz.mdata.server.area.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 空间区域excel实体
 */
@Data
@Builder
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class SpaceAreaImportDTO {

    /**
     * 区域全路径（区域1/区域1-1/区域1-1-1）
     */
    @ExcelProperty(value = "*空间名称", index = 0)
    private String areaName;
    @ExcelProperty(value = "空间状态", index = 1)
    private String statusName;
    @ExcelProperty(value = "占地面积(平米)", index = 2)
    private String builtUpArea;
    @ExcelProperty(value = "区域介绍", index = 3)
    private String introduce;
}
