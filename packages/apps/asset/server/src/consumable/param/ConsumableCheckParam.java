package com.guhecloud.cube.srv.gen.property.management.server.consumable.param;

import lombok.Data;

/**
 * @author Appet
 * @Description 耗材盘点
 * @date 2023-03-16 10:08:36
 */
@Data
public class ConsumableCheckParam {

    //盘点单名称
    private String name;
    //盘点仓库
    private Long storeId;
    //盘点负责人ID(用户ID)
    private Long checkerId;
    /**
     * 本次盘点时间
     */
    private String presentCheckDate;
    /**
     * 上次盘点时间
     */
    private String previousCheckDate;

}