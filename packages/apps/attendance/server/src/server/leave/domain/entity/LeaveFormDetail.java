package com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.entity;

import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author pengjian
 * @date 2022/11/18 18:41
 * @description 请假单明细
 */
@Getter
@Setter
@NoArgsConstructor
public class LeaveFormDetail {
    /**
     * 请假人
     */
    private Long userId;

    /**
     * 请假时间
     * <p>
     * 注意： 一般请假时长是开始时间-结束时间
     * 特殊情况下出现: 日期-上午，如2022-11-22 上午，表示请2022-11-22 上午的假
     * 这种类型因为是排班制的，所以上午时间没法计算开始时间和结束时间，
     */
    private LeaveTime leaveTime;

    /**
     * 请假的时长小时，默认是从请假起止时间计算的，但是会出现请假时长折算的情况
     */
    private Float leaveHour;

    /**
     * 备注
     */
    @Setter
    private String remark;

    /**
     * 请假类型
     */
    private Holiday.HolidayType leaveType;

    /**
     * 休假地点
     */
    @Setter
    private String leaveLocation;

    public LeaveFormDetail(Long userId,
                           LeaveTime leaveTime,
                           Holiday.HolidayType leaveType,
                           Float leaveHour
                          ) {
        this.userId = userId;
        this.leaveTime = leaveTime;
        this.leaveType = leaveType;
        this.leaveHour = leaveHour;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class LeaveTime {
        
        /**
         * 请假时间；如果格式是有明确时间范围的，那么就是2022-01-01 00:00:00,2022-02-02 00:00:00
         * 如果是2022-02-02 上午,那么time就是2022-02-02
         */
        private String originalTime;

        private Time startTime;
        private Time endTime;

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Time {
            private LeaveTimeUnit timeUnit;
            private String time;
        }

        public Period getPeriod() {
            LeaveTimeUnit startTimeUnit = startTime.timeUnit;
            LeaveTimeUnit endTimeUnit = endTime.timeUnit;
            switch (startTimeUnit) {
                case TIME_RANGE:
                    return Period.parse(StringUtils.joinWith(",", startTime.time, endTime.time));
                case DAY:
                    return Period.parse(StringUtils.joinWith(",", startTime.time + " 00:00:00", endTime.time + " 23:59:59"));
            }
            if (startTimeUnit == LeaveTimeUnit.AM && endTimeUnit == LeaveTimeUnit.PM) {
                return Period.parse(StringUtils.joinWith(",", startTime.time + " 00:00:00", endTime.time + " 23:59:59"));
            } else if (startTimeUnit == LeaveTimeUnit.AM && endTimeUnit == LeaveTimeUnit.AM) {
                return Period.parse(StringUtils.joinWith(",", startTime.time + " 00:00:00", endTime.time + " 12:00:00"));
            } else if (startTimeUnit == LeaveTimeUnit.PM && endTimeUnit == LeaveTimeUnit.AM) {
                return Period.parse(StringUtils.joinWith(",", startTime.time + " 12:00:01", endTime.time + " 12:00:00"));
            } else if (startTimeUnit == LeaveTimeUnit.PM && endTimeUnit == LeaveTimeUnit.PM) {
                return Period.parse(StringUtils.joinWith(",", startTime.time + " 12:00:01", endTime.time + " 23:59:59"));
            }
            throw new ServiceException("请假单位有误");
        }
    }

    public enum LeaveTimeUnit {
        /**
         * 时间范围
         */
        TIME_RANGE,
        /**
         * 上午
         */
        AM,
        /**
         * 下午
         */
        PM,
        /**
         * 整天
         */
        DAY
    }
}
