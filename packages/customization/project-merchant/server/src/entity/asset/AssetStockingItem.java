package com.guhecloud.cube.deliver.project.merchant.entity.asset;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 盘点资产明细
 * </p>
 *
 * @author bq
 * @since 2022-01-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("asset_stocking_item")
public class AssetStockingItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 盘点任务ID
     */
    private String stockingId;

    /**
     * 资产ID
     */
    private String assetId;

    /**
     * 盘点结果（未盘、盘亏、正常）
     */
    private String result;

    /**
     * 盘亏原因备注
     */
    private String remark;

    /**
     * 盘点时间
     */
    private LocalDateTime time;

}
