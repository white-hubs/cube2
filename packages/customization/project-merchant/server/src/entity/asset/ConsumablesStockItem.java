package com.guhecloud.cube.deliver.project.merchant.entity.asset;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 易耗品出入库记录明细
 * </p>
 *
 * @author bq
 * @since 2022-01-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("consumables_stock_item")
public class ConsumablesStockItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 易耗品ID
     */
    private String consumablesId;

    /**
     * 出入库ID
     */
    private String stockId;

    /**
     * 出入库数量
     */
    private Integer num;

}
