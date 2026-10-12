package com.guhecloud.cube.srv.gen.property.management.server.consumable.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Appet
 * @Description 耗材盘点明细
 * @date 2023-03-16 10:08:36
 */
@Data
public class ConsumableCheckDetailVO {

    //
    private Long id;
    //盘点单ID
    private Long checkId;
    //耗材ID
    private Long consumableId;
    //盘点数量
    //盘点人ID
    private Long checkerId;
    //盘点人姓名
    private String checkerName;
    //盘点时间
    private LocalDateTime checkTime;

}