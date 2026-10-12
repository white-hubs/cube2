package com.guhecloud.cube.iot.mgt.transfer.dto.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 方法Excel导入导出DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MethodExcelDto {

    @Excel(name = "方法编码", orderNum = "1", width = 20, isImportField = "true")
    private String methodCode;

    @Excel(name = "方法名称", orderNum = "2", width = 30, isImportField = "true")
    private String name;

    @Excel(name = "方法类型", orderNum = "3", width = 15, isImportField = "true", replace = {"同步_sync", "异步_async"})
    private String methodType;

    @Excel(name = "方法描述", orderNum = "4", width = 50, isImportField = "false")
    private String description;

    @Excel(name = "输入参数", orderNum = "5", width = 80, isImportField = "false")
    private String inputParams;

    @Excel(name = "输出参数", orderNum = "6", width = 80, isImportField = "false")
    private String outputParams;

    @Excel(name = "超时时间(毫秒)", orderNum = "7", width = 20, isImportField = "false")
    private Integer timeoutMs;

    @Excel(name = "支持重试", orderNum = "8", width = 15, isImportField = "false", replace = {"否_false", "是_true"})
    private Boolean retryable;

    @Excel(name = "最大重试次数", orderNum = "9", width = 20, isImportField = "false")
    private Integer maxRetries;

    @Excel(name = "状态", orderNum = "10", width = 15, isImportField = "false", replace = {"禁用_0", "启用_1"})
    private Integer status;

    @Excel(name = "备注", orderNum = "11", width = 50, isImportField = "false")
    private String remark;
}

