package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

/**
 * 已绑粮仓简要项（下拉里常用）。
 */
@Data
public class BoundWarehouseSimpleVO {
    /** 智慧仓 id。 */
    private Long warehouseId;
    /** 仓名称。 */
    private String name;
    /** 仓类别（智慧仓 categoryName）。 */
    private String categoryName;
}
