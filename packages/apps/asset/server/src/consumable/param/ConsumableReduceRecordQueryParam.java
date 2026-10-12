package com.guhecloud.cube.srv.gen.property.management.server.consumable.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Appet
 * @Description 耗材出库记录
 * @date 2023-03-16 10:08:36
 */
@Data
public class ConsumableReduceRecordQueryParam extends Paging {

    //仓库ID
    private Long storeId;
    //领用部门
    private Long deptId;
    //领用人
    private String claimant;
    //领用日期
    private LocalDateTime claimantDate;
    //处理人
    private String handler;
    //备注
    private String remark;
    private String status;
    private String keyword;

}