package com.guhecloud.cube.customization.xialing.server.liangcang.param;

import lombok.Data;

/**
 * 单仓查询
 * Query 入参，用于详情、货位卡等接口。
 */
@Data
public class RadarBindingDetailParam {
    /** 智慧仓库粮仓 id。 */
    private Long warehouseId;
}
