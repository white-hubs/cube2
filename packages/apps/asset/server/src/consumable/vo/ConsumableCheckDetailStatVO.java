package com.guhecloud.cube.srv.gen.property.management.server.consumable.vo;

import lombok.Data;

/**
 * @author Appet
 * @Description 耗材入库
 * @date 2023-03-16 10:08:36
 */
@Data
public class ConsumableCheckDetailStatVO {
    //全部
    private Long consumableNum = 0L;
    //已盘数量
    private Long checkedNum = 0L;
    //盘盈数量
    private Long addNum = 0L;
    //盘亏数量
    private Long reduceNum = 0L;
    /**
     * 未盘点
     */
    private Long checkWaitNumber = 0L;
}