package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-26  17:04
 * @Description:
 * @Version: 1.0
 */
@Data
public class ClockInLogResult {
    private Long id;
    /**
     * 日期
     */
    private LocalDateTime clockInDate;
    /**
     * 员工
     */
    private Long userId;
    private String userName;
    /**
     * 部门
     */
    private Long orgId;
    private String orgName;
    /**
     * 打卡时间
     */
    private LocalDateTime actualClockInTime;
    /**
     * 打卡方式
     */
    private String ways;
    /**
     * 打卡地点
     */
    private String name;
    /**
     * 图片
     */
    private FileVO img;
    private Long photoFileId;
    /**
     * 打卡权限
     */
    private String clockInAuthority;
    /**
     * 打卡状态（未打卡、正常、早退、迟到，缺卡、无效）
     */
    private String state;
    /**
     * 打卡类型（上班：start、下班：end，更新打卡：revise）
     */
    private String clockType;
    private LocalDateTime requireClockInTime;
    private String excelDate;
    private String excelDateTime;
}
