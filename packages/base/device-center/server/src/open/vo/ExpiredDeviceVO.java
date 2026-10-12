package com.guhecloud.cube.base.device.center.open.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author wql
 * @describe 设备使用期限信息
 * @date 2023/12/8 9:45
 */
@Data
public class ExpiredDeviceVO {
    /**
     * 设备id
     */
    private Long id;
    /**
     * 使用寿命
     */
    private Integer usefulLife;
    /**
     * 每日开机时长
     */
    private Integer dailyWorkTime;
    /**
     * 安装时间
     */
    private LocalDateTime installDate;
}
