package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-26  13:36
 * @Description: 打卡台账详情
 * @Version: 1.0
 */
@Data
@TableName("att_clock_in_ledger_details")
public class AttClockInLedgerDetailsPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long userId;
    private String userName;
    /**
     * 台账id
     */
    private Long iedgerId;
    /**
     * 台账日期
     */
    private LocalDateTime ledgerBelongDate;
    /**
     * 周几
     */
    private String week;
    /**
     * 几号
     */
    private Integer today;
    /**
     * 打卡状态
     */
    private String state;
    /**
     * 上班时间
     */
    private LocalDateTime startClockInTime;
    /**
     * 下班时间
     */
    private LocalDateTime endClockInTime;
    /**
     * 上班打卡时间时间
     */
    private LocalDateTime actualStartClockInTime;
    private String startWorkTime;
    /**
     * 下班打卡时间
     */
    private LocalDateTime actualEndClockInTime;
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
    private Long classesId;
    private Integer classesVersion;
    private String classesDetails;
    private String clockInUuid;
    private Boolean startWorkOut;
    private Boolean endWorkOut;
}
