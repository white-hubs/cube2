package com.guhecloud.cube.srv.gen.property.management.server.consumable.param;

import annotation.FormField;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 耗材入库动态表单
 */
@Data
public class ConsumableAddRecordApprovalParam {
    @FormField(label = "入库仓库")
    private Long storeId;
    @FormField(label = "入库日期")
    private LocalDateTime addDate;
    @FormField(label = "供应商")
    private String supplier;
    @FormField(label = "处理人")
    private String handler;
    @FormField(label = "备注")
    private String remark;
    //业务类型
    private String businessType;
    @FormField(label = "附件")
    private List<Long> files;
    @FormField(label = "耗材数据")
    private String data;
}
