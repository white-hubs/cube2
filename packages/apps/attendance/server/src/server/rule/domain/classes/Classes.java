package com.guhecloud.cube.srv.gen.attendance.management.server.rule.domain.classes;

import com.baomidou.mybatisplus.annotation.TableField;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.Data;
import com.guhecloud.platform.base.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author bq
 * @date 2022/11/23
 * @description 班次
 */
@Data
public class Classes {

    private Long id;

    private Long classesId;

    /**
     * 汇总名称
     */
    @TableField(exist = false)
    private String summaryName;

    /**
     * 名称
     */
    private String name;

    /**
     * 上班时间
     */
    private String startWorkTime;

    /**
     * 上班时间前
     */
    @TableField(exist = false)
    private String beforeStartWorkTime;

    /**
     * 上班时间后
     */
    @TableField(exist = false)
    private String afterStartWorkTime;

    /**
     * 下班时间
     */
    private String endWorkTime;

    /**
     * 下班时间前
     */
    @TableField(exist = false)
    private String beforeEndWorkTime;

    /**
     * 下班时间后
     */
    @TableField(exist = false)
    private String afterEndWorkTime;

    /**
     * 休息开始时间
     */
    @TableField(exist = false)
    private String restStartTime;

    /**
     * 休息结束时间
     */
    @TableField(exist = false)
    private String restEndTime;

    /**
     * 标志
     */
    private String mark;

    /**
     * 下班时间是否是次日
     */
    private Boolean endTimeIsMorrow;

    /**
     * 班次状态
     */
    @TableField(exist = false)
    private String classesStatus;

    /**
     * 触发动作：迫使班次状态改变的动作
     */
    @TableField(exist = false)
    private String trigger;

    public void setName(String name) {
        this.name = name;
    }

    public String getSummaryName() {
        return this.name + (StringUtils.isNotEmpty(this.startWorkTime) ? (":" + this.startWorkTime + "-" + this.endWorkTime) : "");
    }

    // 为什么设置成一样暂时不知道了
    public void setId(Long id) {
        this.id = id;
        // 这里主要是在排班和值班的班次中，不能把classesId和id设置成一样，这里的两个ID是有区别的，只有在用于班次信息本身时两个才能相等
        if (null != id && (this.classesId == null || Objects.equals(this.classesId, id))) {
            this.classesId = id;
        }
    }

    public void setEndWorkTime(String endWorkTime) {
        this.endWorkTime = endWorkTime;
        LocalDateTime startDate = this.getTimeStrToDate(this.startWorkTime);
        LocalDateTime endDate = this.getTimeStrToDate(this.endWorkTime);
        int compare = DateUtils.compare(startDate, endDate);
        // 如果上班时间比下班时间晚，那么上班时间就是次日的
        if (compare > 0) {
            this.endTimeIsMorrow = true;
        }
    }

    /**
     * 获取结束时间字符串的时间，下班时间和下班之后的打卡范围时间可能会存在跨天的
     *
     * @param timeStr 时间字符串
     *
     * @return 时间
     */
    public LocalDateTime getEndTimeStrToDate(String timeStr) {
        if (StringUtils.isNotEmpty(timeStr)) {
            LocalDateTime date = DateUtils.parse(timeStr, "HH:mm");
            if (this.endTimeIsMorrow != null && this.getEndTimeIsMorrow()) {
                return DateUtils.offsetDay(date, 1);
            }
            return date;
        }
        return null;
    }

    /**
     * 获取时间字符串的时间
     *
     * @param timeStr 时间字符串
     *
     * @return 时间
     */
    public LocalDateTime getTimeStrToDate(String timeStr) {
        if (StringUtils.isNotEmpty(timeStr)) {
            return DateUtils.parse(timeStr, "HH:mm");
        }
        return null;
    }

}