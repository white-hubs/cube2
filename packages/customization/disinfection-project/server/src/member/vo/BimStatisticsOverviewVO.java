package com.guhecloud.cube.srv.gen.disinfection.server.member.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * BIM 看板统计总览
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BimStatisticsOverviewVO {
    /**
     * 设备总数
     */
    private Long totalDeviceCount;
    /**
     * 在线设备个数
     */
    private Long onlineDeviceCount;
    /**
     * 今日新增设备数
     */
    private Long todayNewDeviceCount;
    /**
     * 今日新增用户数
     */
    private Long todayNewUserCount;
}
