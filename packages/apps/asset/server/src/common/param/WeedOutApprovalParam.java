package com.guhecloud.cube.srv.gen.property.management.server.common.param;

import annotation.FormField;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 清理报废动态表单
 */
@Data
public class WeedOutApprovalParam {
    @FormField(label = "处理人")
    private Long weedOutUserId;
    @FormField(label = "清理日期")
    private LocalDateTime manageTime;
    @FormField(label = "清理原因")
    private Long causeId;
    @FormField(label = "是否逾期")
    private Boolean overdue;
    @FormField(label = "备注说明")
    private String remarks;
    @FormField(label = "附件")
    private List<Long> files;
    @FormField(label = "资产id集合")
    private List<Long> propertyIds;
}
