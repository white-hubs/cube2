package com.guhecloud.cube.srv.gen.park.management.server.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.Deletable;
import com.guhecloud.platform.database.entity.Isolatable;
import com.guhecloud.platform.database.interceptor.DeletedTypeHandler;
import lombok.Data;

/**
 * <p>
 * 房间租赁记录与房间关联表
 * </p>
 *
 * @author zwb
 * @since 2022-09-15
 */

@Data
@TableName("room_rent_record_room")
public class RoomRentRecordRoomPO implements Isolatable<Long>, Deletable<Long> {
    /**
     * ID
     */
    private Long id;

    /**
     * 企业ID
     */
    private Long recordId;

    /**
     * 房间ID
     */
    private Long roomId;

    /**
     * 房间名称
     */
    private String roomName;

    /**
     * 租户编号
     */
    private Integer tenant;

    /**
     * 是否已删除
     */
    @TableField(typeHandler = DeletedTypeHandler.class)
    private Boolean deleted;

}
