package com.guhecloud.cube.srv.gen.attendance.management.server.clock.interfaces.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.cube.srv.gen.attendance.management.server.clock.enumeration.ClockStatusEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.ClassesStatusTriggerEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.CommonEnumVO;
import lombok.Data;
import com.guhecloud.platform.base.util.StringUtils;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author bq
 * @date 2022/12/09
 * @description 用户一天打卡记录
 */
@Data
public class UserClockDayVO {

    /**
     * 打卡日期
     */
    private String clockDate;

    /**
     * 班次名称
     */
    private String classesName;

    /**
     * 班次开始时间
     */
    private String startWorkTime;

    /**
     * 真实的班次开始时间
     */
    private LocalDateTime startRealTime;

    /**
     * 班次结束时间
     */
    private String endWorkTime;

    /**
     * 真实的班次结束时间
     */
    private LocalDateTime endRealTime;

    /**
     * 上班打卡时间
     */
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalDateTime startWorkClockTime;

    /**
     * 下班打卡时间
     */
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalDateTime endWorkClockTime;

    /**
     * 上班打卡状态
     */
    private ClockStatusEnum startWorkClockStatus;

    private CommonEnumVO startWorkClockStatusDetail;

    /**
     * 下班打卡状态
     */
    private ClockStatusEnum endWorkClockStatus;

    private CommonEnumVO endWorkClockStatusDetail;

    /**
     * 上班卡审批动作
     */
    private String startTrigger;

    private List<CommonEnumVO> startTriggerList;

    /**
     * 下班卡审批动作
     */
    private String endTrigger;

    private List<CommonEnumVO> endTriggerList;

    /**
     * 班次状态
     */
    private List<CommonEnumVO> classesStatus;

    public void setStartTrigger(String startTrigger) {
        this.startTrigger = startTrigger;
        if (StringUtils.isNotEmpty(startTrigger)) {
            List<CommonEnumVO> triggers = new ArrayList<>();
            String[] split = startTrigger.split(",");
            for (String trigger : split) {
                ClassesStatusTriggerEnum classesStatusTriggerEnum = ClassesStatusTriggerEnum.valueOf(trigger);
                triggers.add(CommonEnumVO.builder()
                                     .code(classesStatusTriggerEnum.name())
                                     .name(classesStatusTriggerEnum.getDesc())
                                     .build());
            }
            this.startTriggerList = triggers;
        }
    }

    public void setEndTrigger(String endTrigger) {
        this.endTrigger = endTrigger;
        List<CommonEnumVO> triggers = new ArrayList<>();
        if (StringUtils.isNotEmpty(endTrigger)) {
            String[] split = endTrigger.split(",");
            for (String trigger : split) {
                ClassesStatusTriggerEnum classesStatusTriggerEnum = ClassesStatusTriggerEnum.valueOf(trigger);
                triggers.add(CommonEnumVO.builder()
                                     .code(classesStatusTriggerEnum.name())
                                     .name(classesStatusTriggerEnum.getDesc())
                                     .build());
            }
            this.endTriggerList = triggers;
        }
    }

    public void setStartWorkClockStatus(ClockStatusEnum startWorkClockStatus) {
        this.startWorkClockStatus = startWorkClockStatus;
        if (startWorkClockStatus != null) {
            this.startWorkClockStatusDetail = CommonEnumVO.builder()
                    .code(startWorkClockStatus.name())
                    .name(startWorkClockStatus.getValue())
                    .build();
        }
    }

    public void setEndWorkClockStatus(ClockStatusEnum endWorkClockStatus) {
        this.endWorkClockStatus = endWorkClockStatus;
        if (endWorkClockStatus != null) {
            this.endWorkClockStatusDetail = CommonEnumVO.builder()
                    .code(endWorkClockStatus.name())
                    .name(endWorkClockStatus.getValue())
                    .build();
        }
    }
}