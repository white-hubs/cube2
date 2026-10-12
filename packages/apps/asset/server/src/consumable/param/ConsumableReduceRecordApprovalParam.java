package com.guhecloud.cube.srv.gen.property.management.server.consumable.param;

import annotation.FormField;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ConsumableReduceRecordApprovalParam {
    @FormField(label = "出库仓库")
    private Long storeId;
    @FormField(label = "领用部门")
    private Long deptId;
    @FormField(label = "领用人")
    private String claimant;
    @FormField(label = "领用日期")
    private LocalDateTime reduceDate;
    @FormField(label = "处理人")
    private String handler;
    @FormField(label = "备注")
    private String remark;
    @FormField(label = "附件")
    private List<Long> files;
    @FormField(label = "耗材数据")
    private String data;
}
