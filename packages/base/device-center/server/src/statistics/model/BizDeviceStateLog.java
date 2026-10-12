package com.guhecloud.cube.base.device.center.statistics.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * @author wql
 * @describe 设备状态变更记录
 * @date 2024/1/22 16:49
 */
@Data
@TableName("center_biz_device_state_log")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BizDeviceStateLog {
    /**
     * 物联网设备ID
     */
    private Long deviceId;
    /**
     * 设备状态
     */
    private int deviceState;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
