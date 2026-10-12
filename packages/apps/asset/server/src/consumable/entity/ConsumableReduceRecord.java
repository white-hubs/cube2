package com.guhecloud.cube.srv.gen.property.management.server.consumable.entity;

import annotation.FormField;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author Appet
 * @Description 耗材出库记录
 * @date 2023-03-16 10:08:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ConsumableReduceRecord extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    @FormField(label = "出库仓库")
    private Long storeId;
    @FormField(label = "领用部门")
    private Long deptId;
    @FormField(label = "领用人")
    private Long claimant;
    @FormField(label = "领用日期")
    private LocalDateTime reduceDate;
    @FormField(label = "处理人")
    private Long handler;
    @FormField(label = "备注")
    private String remark;
    //业务类型
    private String businessType;
    /**
     * 状态
     */
    private String status;
    /**
     * 入库单号
     */
    private String depositoryNumbers;
    /**
     * 流程id
     */
    private String processInstanceId;

}