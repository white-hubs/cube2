package com.guhecloud.cube.base.visualization.server.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/7 17:35
 * @Description:
 */
@Data
public class ModelSceneRelateVO {

    @ExcelProperty(value = "*区域名称")
    private String areaName;

    @ExcelProperty(value = "*模型结构")
    private String modelStructure;

}
