package com.guhecloud.cube.srv.gen.park.management.server.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 房间
 * </p>
 *
 * @author zwb
 * @since 2022-09-15
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("room")
public class RoomPO extends BasicEnhancedTenantEntity<Long> {
    /**
     * ID
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 房间类型ID
     */
    private Long roomTypeId;

    /**
     * 区域ID
     */
    private Long areaId;

    /**
     * 区域序列号
     */
    private String areaSequence;

    /**
     * 状态
     */
    private String status;

    /**
     * 建筑面积
     */
    private double floorage;

    /**
     * 计费面积
     */
    private double floorageBilling;

    /**
     * 企业ID
     */
    private Long enterpriseId;

}
