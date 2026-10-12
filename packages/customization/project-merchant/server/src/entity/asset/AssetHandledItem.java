package com.guhecloud.cube.deliver.project.merchant.entity.asset;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 资产处置明细
 * </p>
 *
 * @author bq
 * @since 2022-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("asset_handled_item")
public class AssetHandledItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 处置ID
     */
    private String handledId;

    /**
     * 资产ID
     */
    private String assetId;


}
