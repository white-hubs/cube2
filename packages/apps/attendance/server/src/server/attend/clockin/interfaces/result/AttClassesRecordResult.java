package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttClassesRecordPO;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-22  09:45
 * @Description:
 * @Version: 1.0
 */
@Data
public class AttClassesRecordResult {
    private Long id;
    /**
     * 班次id
     */
    private Long classesId;
    /**
     * 名称
     */
    private String name;
    /**
     * 别称
     */
    private String nickName;
    /**
     * 上班时间
     */
    private String startWorkTime;
    /**
     * 上班打卡开始时间
     */
    private LocalDateTime startClockBeforeTime;
    /**
     * 上班打卡结束时间
     */
    private LocalDateTime startClockAfterTime;
    /**
     * 下班时间
     */
    private String endWorkTime;
    /**
     * 下班打卡开始时间
     */
    private LocalDateTime endClockBeforeTime;
    /**
     * 下班打卡结束时间
     */
    private LocalDateTime endClockAfterTime;
    /**
     * 休息开始时间
     */
    private String restStartTime;
    /**
     * 休息结束时间
     */
    private String restEndTime;
    /**
     * 下班时间是否是次日
     */
    private Boolean endTimeIsMorrow;
    /**
     * 修改记录版本号
     */
    private Integer version = 1;
    /**
     * 打卡信息
     */
    private List<AttClockInRecordsResult> clockInfo;

    public static AttClassesRecordResult getAttClassesRecordResult(AttClassesRecordPO classesRecord, LocalDateTime classDate) {
        AttClassesRecordResult result = new AttClassesRecordResult();
        if (ObjectUtils.isNotEmpty(classesRecord)) {
            LocalDateTime startTime = DateUtils.parse(DateUtils.formatDate(classDate) + " " + classesRecord.getStartWorkTime() + ":00");
            LocalDateTime endTime = DateUtils.parse(DateUtils.formatDate(classDate) + " " + classesRecord.getEndWorkTime() + ":00");
            // 下班是第二天需要偏移一天
            if (classesRecord.getEndTimeIsMorrow()) {
                endTime = DateUtils.offsetDay(endTime, 1);
            }
            result.setId(classesRecord.getId());
            result.setClassesId(classesRecord.getClassesId());
            result.setName(classesRecord.getName());
            result.setNickName(classesRecord.getNickName());
            result.setVersion(classesRecord.getVersion());
            result.setStartWorkTime(classesRecord.getStartWorkTime());
            result.setEndWorkTime(classesRecord.getEndWorkTime());
            result.setStartClockBeforeTime(DateUtils.parse(
                    DateUtils.formatDate(classDate) + " " + classesRecord.getStartClockBeforeTime() + ":00"));
            result.setStartClockAfterTime(DateUtils.parse(
                    DateUtils.formatDate(classDate) + " " + classesRecord.getStartClockAfterTime() + ":59"));
            if (DateUtils.compare(result.getStartClockBeforeTime(), result.getStartClockAfterTime()) > 0) {
                result.setStartClockBeforeTime(DateUtils.offsetDay(result.getStartClockBeforeTime(), -1));
            }
            result.setEndClockBeforeTime(DateUtils.parse(
                    DateUtils.formatDate(endTime) + " " + classesRecord.getEndClockBeforeTime() + ":00"));
            result.setEndClockAfterTime(DateUtils.parse(
                    DateUtils.formatDate(endTime) + " " + classesRecord.getEndClockAfterTime() + ":59"));
            // 上班打卡时间>上班打卡最早时间=前一天
            if (DateUtils.compare(startTime, result.getStartClockBeforeTime()) < 0) {
                result.setStartClockBeforeTime(DateUtils.offsetDay(result.getStartClockBeforeTime(), -1));
            }
            // 上班结束时间>上班打卡时间=后一天
            if (DateUtils.compare(result.getStartClockAfterTime(), startTime) < 0) {
                result.setStartClockAfterTime(DateUtils.offsetDay(result.getStartClockAfterTime(), 1));
            }
            // 下班结束时间>下班打卡时间=后一天
            if (DateUtils.compare(endTime, result.getEndClockBeforeTime()) < 0) {
                result.setEndClockBeforeTime(DateUtils.offsetDay(result.getEndClockBeforeTime(), -1));
            }
            // 下班结束时间>下班打卡时间=后一天
            if (DateUtils.compare(result.getEndClockAfterTime(), endTime) < 0) {
                result.setEndClockAfterTime(DateUtils.offsetDay(result.getEndClockAfterTime(), 1));
            }
        }
        return result;
    }
}
