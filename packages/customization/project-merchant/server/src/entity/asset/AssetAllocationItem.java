package com.guhecloud.cube.deliver.project.merchant.entity.asset;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 资产调拨明细
 * </p>
 *
 * @author bq
 * @since 2022-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("asset_allocation_item")
public class AssetAllocationItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 调拨单ID
     */
    private String allocationId;

    /**
     * 资产ID
     */
    private String assetId;

    /**
     * 转出一级区域ID
     */
    private String areaId1;

    /**
     * 转出二级区域ID
     */
    private String areaId2;

    /**
     * 转出三级区域ID
     */
    private String areaId3;

    /**
     * 转出部门ID
     */
    private String useDeptId;

    /**
     * 转出部门名称
     */
    private String useDeptName;

    /**
     * 转出人ID
     */
    private String userId;

    /**
     * 转出人名称
     */
    private String username;

}
