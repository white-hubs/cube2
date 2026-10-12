package com.guhecloud.cube.biz.mdata.server.area.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/11/19 11:10
 * @Description: 区域导入实体
 */

@Data
@EqualsAndHashCode
public class AreaImportDTO {

    /**
     * 区域全路径（区域1/区域1-1/区域1-1-1）
     */
    @ExcelProperty(value = "*区域名称", index = 0)
    private String areaName;
    @ExcelProperty(value = "模型区域", index = 1)
    private String aliasName;
    @ExcelProperty(value = "区域类型", index = 2)
    private String areaType;
    @ExcelProperty(value = "区域介绍", index = 3)
    private String introduce;
    @ExcelProperty(value = "序号", index = 4)
    private String sort;
}
