package com.guhecloud.cube.srv.gen.property.management.server.consumable.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Appet
 * @Description 耗材入库
 * @date 2023-03-16 10:08:36
 */
@Data
public class ConsumableAddRecordVO {

    //
    private Long id;
    //仓库ID
    private Long storeId;
    //入库日期
    private LocalDateTime addDate;
    //供应商
    private String supplier;
    //处理人
    private String handler;
    //备注
    private String remark;

}