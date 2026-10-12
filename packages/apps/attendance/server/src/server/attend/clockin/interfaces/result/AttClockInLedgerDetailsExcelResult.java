package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-26  13:52
 * @Description:
 * @Version: 1.0
 */
@Data
public class AttClockInLedgerDetailsExcelResult {
    private Long userId;
    private String userName;
    private Long orgId;
    private String orgName;
    private String attGroupName;
    private String ledgerDate;
    private String today;
    private LocalDateTime ledgerBelongDate;
    /**
     * 上班时间
     */
    private LocalDateTime startClockInTime;
    private String startClockInDate;
    /**
     * 下班时间
     */
    private LocalDateTime endClockInTime;
    private String endClockInDate;

    /**
     * 上班打卡时间
     */
    private String startWorkTime;
    /**
     * 下班打卡时间
     */
    private String endWorkTime;
    private String state;
    private LocalDateTime actualStartClockInTime;
    private LocalDateTime actualEndClockInTime;
    private String startWorkState;
    private String endWorkState;
    private String classesDetails;
    private String result;
    private Boolean startWorkOmit;
    private Boolean startWorkOut;
    private Boolean endWorkOmit;
    private Boolean endWorkOut;
}
