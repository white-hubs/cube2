package com.guhecloud.cube.srv.gen.property.management.server.common.param;

import annotation.FormField;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 资产入库动态表单
 */
@Data
public class DepositoryDetailedApprovalParam {
    @FormField(label = "入库日期")
    private LocalDateTime startTime;
    @FormField(label = "来源")
    private String sourceId;
    @FormField(label = "收货单号")
    private String receiptNumbers;
    @FormField(label = "备注")
    private String remarks;
    @FormField(label = "附件")
    private List<Long> files;
    @FormField(label = "资产数据")
    private String data;

}