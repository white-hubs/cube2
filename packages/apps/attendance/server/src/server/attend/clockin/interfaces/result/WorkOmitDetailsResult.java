package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: lijie
 * @CreateTime: 2023-11-13  10:32
 * @Description:
 * @Version: 1.0
 */
@Data
public class WorkOmitDetailsResult {
    /**
     * 用户id
     */
    private Long userId;
    private String userName;
    /**
     * 补卡原因
     */
    private String sceneCode;
    /**
     * 补卡申请时间
     */
    private LocalDateTime createTime;
    /**
     * 补卡处理时间
     */
    private LocalDateTime updateTime;
    /**
     * 补卡日期
     */
    private LocalDateTime clockInDate;
    /**
     * 班次名称
     */
    private String classesName;
    /**
     * 上班时间
     */
    private String startWorkTime;
    /**
     * 下班时间
     */
    private String endWorkTime;
}
