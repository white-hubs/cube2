package com.guhecloud.cube.deliver.project.merchant.commons.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 后端校验Excel数据错误信息类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ErrorExcelDTO {
    /**
     * 行号
     */
    private Integer row;

    /**
     * 字段名
     */
    private String fieldName;

    /**
     * 错误信息
     */
    private String errorMsg;
}
