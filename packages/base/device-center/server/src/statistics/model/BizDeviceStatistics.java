package com.guhecloud.cube.base.device.center.statistics.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@TableName("center_biz_device_statistics")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BizDeviceStatistics {
    /**
     * 物联网设备ID
     */
    private Long id;
    /**
     * 设备状态
     */
    private Integer deviceState;
    /**
     * 状态更新时间
     */
    private LocalDateTime reportTime;
    /**
     * 在线时长
     */
    private Long onlineTime;
    /**
     * 在线次数
     */
    private Integer onlineNum;
    /**
     * 离线时长
     */
    private Long offlineTime;
    /**
     * 离线次数
     */
    private Integer offlineNum;
}
