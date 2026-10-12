package com.guhecloud.cube.srv.gen.property.management.server.consumable.entity;

import annotation.FormField;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author Appet
 * @Description 耗材入库
 * @date 2023-03-16 10:08:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ConsumableAddRecord extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    @FormField(label = "入库仓库")
    private Long storeId;
    @FormField(label = "入库日期")
    private LocalDateTime addDate;
    @FormField(label = "供应商")
    private String supplier;
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