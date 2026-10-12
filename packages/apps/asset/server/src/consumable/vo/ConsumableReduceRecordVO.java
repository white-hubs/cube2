package com.guhecloud.cube.srv.gen.property.management.server.consumable.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Appet
 * @Description 耗材出库记录
 * @date 2023-03-16 10:08:36
 */
@Data
public class ConsumableReduceRecordVO {

    //
    private Long id;
    //仓库ID
    private Long storeId;
    private String storeName;
    //领用部门
    private Long deptId;
    private String deptName;
    //领用人
    private String claimant;
    private String claimantName;
    //领用日期
    private LocalDateTime reduceDate;
    //处理人
    private String handler;
    private String handlerName;
    //备注
    private String remark;
    /**
     * 出库单号
     */
    private String depositoryNumbers;
    /**
     * 状态
     */
    private String status;
    private String processInstanceId;
    private LocalDateTime createTime;

}