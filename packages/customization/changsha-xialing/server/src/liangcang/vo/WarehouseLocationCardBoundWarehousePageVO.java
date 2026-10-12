package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

/** 货位卡已绑定粮仓分页。 */
@Data
public class WarehouseLocationCardBoundWarehousePageVO {
    /** 粮仓 id。 */
    private Long warehouseId;
    /** 粮仓名称。 */
    private String warehouseName;
    /** 类别。 */
    private String categoryName;
    /** 等级。 */
    private String qualityGrade;
    /** 最大高度。 */
    private Double maxHeight;
    /** 仓内容积。 */
    private Double warehouseVolume;
    /** 保管人。 */
    private String keeper;
}
