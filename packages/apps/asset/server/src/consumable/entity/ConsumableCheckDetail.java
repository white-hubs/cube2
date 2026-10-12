package com.guhecloud.cube.srv.gen.property.management.server.consumable.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author Appet
 * @Description 耗材盘点明细
 * @date 2023-03-16 10:08:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ConsumableCheckDetail extends BasicEnhancedTenantEntity<Long> {

    //
    private Long id;
    //盘点单ID
    private Long checkId;
    //耗材ID
    private Long consumableId;
    //盘点数量
    private Integer checkNum = 0;
    //当前数量
    private Integer num = 0;
    //盘点人ID
    private Long checkerId;
    //盘点人姓名
    private String checkerName;
    //盘点时间
    private LocalDateTime checkTime;
    private String status;

}