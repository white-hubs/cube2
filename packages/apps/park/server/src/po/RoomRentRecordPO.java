package com.guhecloud.cube.srv.gen.park.management.server.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.park.management.server.enumeration.RoomRentRecordStatusEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * <p>
 * 房间租赁记录
 * </p>
 *
 * @author zwb
 * @since 2022-09-15
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("room_rent_record")
public class RoomRentRecordPO extends BasicEnhancedTenantEntity<Long> {
    /**
     * ID
     */
    private Long id;

    /**
     * 企业ID
     */
    private Long enterpriseId;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 房间ID
     */
    private String roomIds;

    /**
     * 房间名称
     */
    private String roomNames;

    /**
     * 签订时间
     */
    private LocalDateTime signTime;

    /**
     * 开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 状态
     */
    private RoomRentRecordStatusEnum status;

}
