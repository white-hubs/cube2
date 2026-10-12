package com.guhecloud.cube.property.management.client.property.vo.property;

import com.alibaba.excel.annotation.ExcelProperty;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DepositoryDetailedPageVO {
    /**
     * id
     */
    private Long id;
    /**
     * 入库单号
     */
    @ExcelProperty(value = { "入库单号" })
    private String depositoryNumbers;
    /**
     * 入库日期
     */
    @ExcelProperty(value = { "入库日期" })
    private LocalDateTime startTime;
    /**
     * 处理人
     */
    private String createBy;

    /**
     * 处理人id
     */
    private String creatorId;

    /**
     * 处理人类型
     */
    private ActorTypeEnum creatorType;

    @ExcelProperty(value = { "处理人" })
    private String createByName;
    /**
     * 处理时间日期
     */
    private LocalDateTime createTime;
    /**
     * 收货单号
     */
    @ExcelProperty(value = { "收货单号" })
    private String receiptNumbers;
    /**
     * 资产明细
     */
    private List<PropertyDetailedVO> propertyDetailed;
    /**
     * 状态
     */
    @ExcelProperty(value = { "状态" })
    private String status;
    /**
     * 来源
     */
    @ExcelProperty(value = { "来源" })
    private String sourceName;
    /**
     * 说明
     */
    private String remarks;
    /**
     * 入库附件
     */
    private List<FileVO> file;
    private String processInstanceId;
    private String currentTaskId;
}
