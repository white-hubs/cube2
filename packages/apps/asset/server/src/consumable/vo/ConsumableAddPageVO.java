package com.guhecloud.cube.srv.gen.property.management.server.consumable.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ConsumableAddPageVO {
    private Long id;
    /**
     * 入库单号
     */
    private String depositoryNumbers;
    /**
     * 仓库
     */
    private Long storeId;
    /**
     * 入库日期
     */
    private LocalDateTime addDate;
    private String storeName;
    /**
     * 数量
     */
    private Integer num;
    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 处理人
     */
    private Long handler;
    private String handlerName;
    /**
     * 状态
     */
    private String status;
    /**
     * 供应商
     */
    private String supplier;
    private String processInstanceId;
    private BigDecimal amount;

}
