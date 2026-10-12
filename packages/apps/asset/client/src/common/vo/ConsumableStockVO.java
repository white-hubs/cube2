package com.guhecloud.cube.property.management.client.common.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-07-29  15:02
 * @Description:
 * @Version: 1.0
 */
@Data
public class ConsumableStockVO {
    /**
     * 耗材总数
     */
    private Integer consumableNumber = 0;
    /**
     * 超过库存上限
     */
    private Integer stockMax = 0;
    /**
     * 低于库存下限
     */
    private Integer stockMin = 0;

    private String status;
    private Integer count;

    public void setconsumableNumber(List<ConsumableStockVO> consumableStockList) {
        for (ConsumableStockVO ex : consumableStockList) {
            if (ex.getStatus().equals("stockMax")) {
                this.stockMax += ex.getCount();
            } else if (ex.getStatus().equals("stockMin")) {
                this.stockMin += ex.getCount();
            } else if (ex.getStatus().equals("consumableNumber")) {
                this.consumableNumber += ex.getCount();
            }
        }
        this.consumableNumber = this.consumableNumber + this.stockMax + this.stockMin;
    }
}
