package com.guhecloud.cube.property.management.client.property.param.property;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 入库信息
 */
@Data
public class DepositoryDetailedParam {
    /**
     * 来源id
     */
    @NotNull(message = "来源id不能为空")
    private Long sourceId;
    /**
     * 来源名称
     */
    @NotEmpty(message = "来源名称不能为空")
    private String sourceName;
    /**
     * 收货单号
     */
    private String receiptNumbers;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 附件
     */
    private List<Long> files;
    /**
     * 入库日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime startTime;
    /**
     * 入库状态
     */
    private String status;
    /**
     * 资产明细
     */
    @NotEmpty(message = "资产明细不能为空")
    private List<PropertyDetailedWorkflowParam> propertyDetailed;
    private String processInstanceId;
}