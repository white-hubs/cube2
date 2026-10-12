package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.enumeration.ClockInEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.filter.AttendanceCurrentUserDeptFieldFilter;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.annotation.DataPermission;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-22  11:14
 * @Description:
 * @Version: 1.0
 */
@Data
@TableName("att_clock_in_records")
@DataPermission({
        @DataPermissionField(permission = "CURRENT_USER", field = "user_id"),
        @DataPermissionField(permission = "CURRENT_USER_DEPT", field = "org_id", fieldFilter = AttendanceCurrentUserDeptFieldFilter.class),
        @DataPermissionField(permission = "CUSTOM_DEPT", field = "org_id")
}
)
public class AttClockInRecordsPO extends BasicEnhancedTenantEntity<Long> {
    /**
     * 打卡记录id
     */
    private Long id;
    private Long userId;
    private String userName;
    private Long orgId;
    /**
     * 班次id
     */
    private Long classesId;
    /**
     * 班次版本
     */
    private Integer classesVersion;
    /**
     * 打卡类型（上班：start、下班：end，更新打卡：revise）
     */
    private String clockType;
    /**
     * 打卡记录标识
     */
    private String uuid;

    /**
     * 来源
     */
    private String ways;

    /**
     * 打卡时间
     */
    private LocalDateTime requireClockInTime;
    private String clockInTime;
    /**
     * 实际打卡时间
     */
    private LocalDateTime actualClockInTime;
    /**
     * 打卡状态（未打卡、正常、早退、迟到，缺卡、无效）
     */
    private String state;
    /**
     * 迟到、早退时间（分钟）
     */
    private Long deletionDuration = 0L;
    /**
     * 加班时长
     */
    private Integer overtimeDuration = 0;
    /**
     * 班次时间（年月日）
     */
    private LocalDateTime clockInDate;
    /**
     * 打卡权限
     */
    private String clockInAuthority;
    /**
     * 打卡地点
     */
    private String name;
    /**
     * 最早打卡时间
     */
    private LocalDateTime clockBeforeTime;
    /**
     * 最晚打卡时间
     */
    private LocalDateTime clockAfterTime;
    /**
     * 打卡照片
     */
    private Long photoFileId;
    private String photoFileUrl;
    /**
     * 同步台账时间
     */
    private LocalDateTime syncTime;
    /**
     * 人脸识别失败结果
     */
    private String faceRecognitionResult;
    private String pastDataType;
    /**
     * 是否为外勤
     */
    private Boolean outClock = Boolean.FALSE;
    private Boolean workOmit = Boolean.FALSE;
    private String mark;
    /**
     * 打卡失败原因
     */
    private String failReason;

    public AttClockInRecordsPO convertAttClockInRecordsPO() {
        AttClockInRecordsPO records = new AttClockInRecordsPO();
        records.setClockInTime(this.getClockInTime());
        records.setUuid(this.getUuid());
        records.setUserId(this.getUserId());
        records.setUserName(this.getUserName());
        records.setOrgId(this.getOrgId());
        records.setClassesId(this.getClassesId());
        records.setClassesVersion(this.getClassesVersion());
        records.setClockType(this.getClockType());
        records.setClockAfterTime(this.getClockAfterTime());
        records.setRequireClockInTime(this.getRequireClockInTime());
        records.setClockInDate(this.getClockInDate());
        records.setOutClock(this.outClock);
        records.setClockBeforeTime(this.getClockBeforeTime());
        return records;
    }

    /**
     * 上班打卡状态
     */
    private void getStartClockState() {
        if (ObjectUtils.isNotEmpty(this.getActualClockInTime())) {
            this.setState(DateUtils.compare(this.getActualClockInTime(), DateUtils.offsetMinute(this.getRequireClockInTime(), 1)) < 0
                          ? ClockInEnum.NORMAL.name() : ClockInEnum.LATE_CHECKED.name());
            if (ClockInEnum.LATE_CHECKED.name().equals(this.getState())) {
                String requireClockInTime = DateUtils.format(this.getRequireClockInTime());
                requireClockInTime = requireClockInTime.substring(0, requireClockInTime.length() - 2) + "00";
                Long minute = DateUtils.between(DateUtils.parse(requireClockInTime), this.getActualClockInTime(), ChronoUnit.MINUTES);
                this.setDeletionDuration(minute);
            }
        }
    }

    /**
     * 下班打卡状态
     */
    private void getEndClockState() {
        if (ObjectUtils.isNotEmpty(this.getActualClockInTime())) {
            this.setState(
                    DateUtils.compare(this.getRequireClockInTime(), this.getActualClockInTime()) > 0 ? ClockInEnum.EARLY_CHECKED.name()
                                                                                                     : ClockInEnum.NORMAL.name());
            if (ClockInEnum.EARLY_CHECKED.name().equals(state)) {
                String requireClockInTime = DateUtils.format(this.getRequireClockInTime());
                requireClockInTime = requireClockInTime.substring(0, requireClockInTime.length() - 2) + "59";
                long minute = DateUtils.between(this.getActualClockInTime(), DateUtils.parse(requireClockInTime), ChronoUnit.MINUTES, true);
                this.setDeletionDuration(minute);
            }
        }
    }

    /**
     * 打卡记录修正考勤结果
     */
    public void getAttClockInRecordsPO(AttClockInRecordsPO clockLog) {
        this.setActualClockInTime(clockLog.getActualClockInTime());
        this.setWays(clockLog.getWays());
        this.setName(clockLog.getName());
        this.setClockInAuthority(ClockInEnum.MY_CLOCK.name());
        this.setPhotoFileId(clockLog.getPhotoFileId());
        this.setPhotoFileUrl(clockLog.getPhotoFileUrl());
        this.setFaceRecognitionResult(clockLog.getFaceRecognitionResult());
        this.setSyncTime(null);
        if (ClockInEnum.START.name().equals(this.getClockType())) {
            this.getStartClockState();
        } else if (ClockInEnum.END.name().equals(this.getClockType())) {
            this.getEndClockState();
        }
    }
}
