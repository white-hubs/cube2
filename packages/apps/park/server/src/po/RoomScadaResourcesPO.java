package com.guhecloud.cube.srv.gen.park.management.server.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 房间组态图
 * </p>
 *
 * @author zwb
 * @since 2022-09-15
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("room_scada_resources")
public class RoomScadaResourcesPO extends BasicEnhancedTenantEntity<Long> {
    /**
     * ID
     */
    private Long id;

    /**
     * 区域ID
     */
    private Long areaId;

    /**
     * 区域序列号
     */
    private String areaSequence;

    /**
     * 二维组态地址
     */
    private String url;

}
