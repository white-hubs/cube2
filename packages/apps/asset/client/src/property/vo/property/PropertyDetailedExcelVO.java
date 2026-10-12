package com.guhecloud.cube.property.management.client.property.vo.property;

import com.alibaba.excel.annotation.ExcelProperty;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PropertyDetailedExcelVO {
    @ExcelProperty(value = { "资产编码" })
    private String serialNumber;
    @ExcelProperty(value = { "资产名称" })
    private String name;
    @ExcelProperty(value = { "资产类别" })
    private String typeName;
    @ExcelProperty(value = { "计量单位" })
    private String unit;
    @ExcelProperty(value = { "规格型号" })
    private String norms;
    @ExcelProperty(value = { "原值" })
    private BigDecimal price;
    @ExcelProperty(value = { "使用部门" })
    private String receiveDepName;
    @ExcelProperty(value = { "使用人" })
    private String receiveUserName;
    @ExcelProperty(value = { "入库日期" })
    private LocalDateTime startTime;
    private String startTimeStr;

    @ExcelProperty(value = { "预计使用期限（月）" })
    private Long usageTime;
    @ExcelProperty(value = { "逾龄资产" })
    private String usage;
    private String creatorId;
    private ActorTypeEnum creatorType;
    @ExcelProperty(value = { "盘点操作人" })
    private String createByName;
    @ExcelProperty(value = { "盘点操作时间" })
    private LocalDateTime checkTime;
    private String checkTimeStr;

    @ExcelProperty(value = { "盘点状态" })
    private String checkStatus;
    @ExcelProperty(value = { "异常类型" })
    private String abnormalType;
    @ExcelProperty(value = { "说明" })
    private String abnormalRemarks;
    /**
     * 资产逾龄时间
     */
    private LocalDateTime usageDate;
}
