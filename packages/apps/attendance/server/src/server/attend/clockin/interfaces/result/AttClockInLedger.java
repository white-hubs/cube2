package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2023-10-07  09:51
 * @Description:
 * @Version: 1.0
 */
@Data
public class AttClockInLedger {
    /**
     * 打卡状态
     */
    private String state;
    /**
     * 上班时间
     */
    private String startWorkTime;
    /**
     * 下班时间
     */
    private String endWorkTime;
    /**
     * 上班异常信息
     */
    private String startWorkDetails;
    private String startWorkState;
    /**
     * 上班是否有补卡
     */
    private Boolean startWorkOmit;

    /**
     * 下班异常信息
     */
    private String endWorkDetails;
    private String endWorkState;
    /**
     * 下班是否有补卡
     */
    private Boolean endWorkOmit;
    /**
     * 状态详情
     */
    private List<KeyValueResult> stateDetails;
    private String clockInUuid;
    private Boolean startWorkOut;
    private Boolean endWorkOut;
    private LocalDateTime startClockInTime;
    private LocalDateTime endClockInTime;
    private Long userId;

}
