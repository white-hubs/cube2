package com.guhecloud.cube.srv.gen.attendance.management.server.clock.interfaces.vo;

import lombok.Data;

/**
 * @author bq
 * @date 2023/03/28
 * @description 打卡
 */
@Data
public class ThirdClockResultVO {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 部门名称
     */
    private String DeptName;

    /**
     * 班次名称
     */
    private String ScheduName;

    /**
     * 打卡状态
     */
    private String startEmployType;

    /**
     * 打开时间
     */
    private String StarTime;

}