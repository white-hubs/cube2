package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.param;

import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 班次入参对象
 */
@Data
public class AttClassesParam {

    private Long id;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空")
    private String name;

    /**
     * 别称
     */
    @NotBlank(message = "简称不能为空")
    private String nickName;

    /**
     * 上班时间
     */
    @NotBlank(message = "上班时间不能为空")
    private String startWorkTime;

    /**
     * 上班打卡开始时间
     */
    @NotBlank(message = "上班打卡开始时间不能为空")
    private String startClockBeforeTime;

    /**
     * 上班打卡结束时间
     */
    @NotBlank(message = "上班打卡结束时间不能为空")
    private String startClockAfterTime;

    /**
     * 下班时间
     */
    @NotBlank(message = "下班时间不能为空")
    private String endWorkTime;

    /**
     * 下班打卡开始时间
     */
    @NotBlank(message = "下班开始打卡时间不能为空")
    private String endClockBeforeTime;

    /**
     * 下班打卡结束时间
     */
    @NotBlank(message = "下班结束打卡时间不能为空")
    private String endClockAfterTime;

    /**
     * 休息开始时间
     */
    private String restStartTime;

    /**
     * 休息结束时间
     */
    private String restEndTime;

    /**
     * 班次类型
     * 枚举，（如果有需求可拓展成班次类型模块）
     */
    @NotBlank(message = "班次类型不能为空")
    private String classesType;

    /**
     * 下班时间是否是次日
     */
    private Boolean endTimeIsMorrow;

    /**
     * 建议应用范围(部门id)
     */
    private String deptIds;

    public String getDeptIds() {
        if (StringUtils.isNotBlank(this.deptIds)) {
            return this.deptIds;
        }
        return "0";
    }

    /**
     * 校验班次时间规范
     */
    public void validBusiness() {
        // 上班时间
        LocalDateTime startWorkTimeDate = null;
        // 上班打卡开始时间
        LocalDateTime startClickBeforeTimeDate = null;
        // 上班打卡结束时间
        LocalDateTime startClickAfterTimeDate = null;
        // 下班时间
        LocalDateTime endWorkTimeDate = null;
        // 下班打卡开始时间
        LocalDateTime endClickBeforeTimeDate = null;
        // 下班打卡结束时间
        LocalDateTime endClickAfterTimeDate = null;

        // 休息开始时间
        LocalDateTime restStartTimeDate = null;
        // 休息结束时间
        LocalDateTime restEndTimeDate = null;

        // 是否有休息时间
        boolean restDateBool = false;
        if ((StringUtils.isBlank(this.restStartTime) && StringUtils.isNotBlank(this.restEndTime)) ||
            (StringUtils.isNotBlank(this.restStartTime) && StringUtils.isBlank(this.restEndTime))) {
            throw new ServiceException("休息时间不能只有一个");
        }
        try {
            startWorkTimeDate = DateUtils.parse(this.startWorkTime, "HH:mm");
            startClickBeforeTimeDate = DateUtils.parse(this.startClockBeforeTime, "HH:mm");
            startClickAfterTimeDate = DateUtils.parse(this.startClockAfterTime, "HH:mm");
            endWorkTimeDate = DateUtils.parse(this.endWorkTime, "HH:mm");
            endClickBeforeTimeDate = DateUtils.parse(this.endClockBeforeTime, "HH:mm");
            endClickAfterTimeDate = DateUtils.parse(this.endClockAfterTime, "HH:mm");
            if (StringUtils.isNotBlank(this.restStartTime) && StringUtils.isNotBlank(this.restEndTime)) {
                restStartTimeDate = DateUtils.parse(this.restStartTime, "HH:mm");
                restEndTimeDate = DateUtils.parse(this.restEndTime, "HH:mm");
                restDateBool = true;
            }
        } catch (Exception ignored) {
            throw new ServiceException("时间格式有误！");
        }
        // 如果开始时间不在结束时间之前，说明结束时间为第二天
        if (startWorkTimeDate.isAfter(endWorkTimeDate)) {
            this.endTimeIsMorrow = true;
            endWorkTimeDate = DateUtils.offsetDay(endWorkTimeDate, 1);
            endClickAfterTimeDate = DateUtils.offsetDay(endClickAfterTimeDate, 1);
            endClickBeforeTimeDate = DateUtils.offsetDay(endClickBeforeTimeDate, 1);

            if (restDateBool) {
                restStartTimeDate = DateUtils.offsetDay(restStartTimeDate, 1);
                restEndTimeDate = DateUtils.offsetDay(restEndTimeDate, 1);
            }
        }

        if (startClickBeforeTimeDate.isAfter(startWorkTimeDate)) {
            startClickBeforeTimeDate = DateUtils.offsetDay(startClickBeforeTimeDate, -1);
        }
        if (startClickAfterTimeDate.isBefore(startWorkTimeDate)) {
            startClickAfterTimeDate = DateUtils.offsetDay(startClickAfterTimeDate, 1);
        }

        if (endClickBeforeTimeDate.isAfter(endWorkTimeDate)) {
            endClickBeforeTimeDate = DateUtils.offsetDay(endClickBeforeTimeDate, -1);
        }
        if (endClickAfterTimeDate.isBefore(endWorkTimeDate)) {
            endClickAfterTimeDate = DateUtils.offsetDay(endClickAfterTimeDate, 1);
        }

        // 校验时间是否符合业务条件
        if (startWorkTimeDate.isBefore(startClickBeforeTimeDate)) {
            throw new ServiceException("上班开始打卡时间不能晚于上班时间");
        }
        if (startWorkTimeDate.isAfter(startClickAfterTimeDate)) {
            throw new ServiceException("上班结束打卡时间不能早于上班时间");
        }
        if (endWorkTimeDate.isBefore(endClickBeforeTimeDate)) {
            throw new ServiceException("下班开始打卡时间应该在下班时间之前");
        }
        if (endWorkTimeDate.isAfter(endClickAfterTimeDate)) {
            throw new ServiceException("下班打卡结束时间应该在下班时间之后");
        }
        if (!startClickAfterTimeDate.isBefore(endClickBeforeTimeDate)) {
            throw new ServiceException("上班结束打卡时间应该在下班开始打卡时间之前");
        }

        if (restDateBool) {
            if (!restStartTimeDate.isAfter(startWorkTimeDate) || !restEndTimeDate.isBefore(endWorkTimeDate)) {
                throw new ServiceException("休息时间应该在上下班时间之间");
            }
            if (!restStartTimeDate.isBefore(restEndTimeDate)) {
                throw new ServiceException("休息开始时间应该在休息结束时间之前");
            }
        }

    }

}
