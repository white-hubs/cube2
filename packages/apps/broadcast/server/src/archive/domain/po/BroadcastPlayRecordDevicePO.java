package com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 广播记录设备数据模型
 *
 * @author lanchun
 * @date 2022/12/12 13:54
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("broadcast_play_record_device")
public class BroadcastPlayRecordDevicePO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 广播播放记录主键
     */
    private Long broadcastPlayRecordId;

    /**
     * 设备主键
     */
    private Long deviceId;

    /**
     * 设备编号
     */
    private String deviceCode;

    /**
     * 设备名称
     */
    private String deviceName;

}
