package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.enumeration;

import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-22  15:33
 * @Description:
 * @Version: 1.0
 */
@Getter
public enum ClockInEnum {
    //    打卡类型
    START("上班"),
    REVISE("更新打卡"),
    END("下班"),
    LOG("打卡记录"),
    // 打卡类型（pc可以进行操作权限）
    REVOKE_CLOCK("撤销"),
    PC_CLOCK("pc新增打卡"),
    DISCARD_CLOCK("作废"),
    MY_CLOCK("员工打卡"),
    // 班次状态
    NO_CLOCK("无数据"),
    NEGLECT_WORK("旷工"),
    ABNORMAL_WORK("异常"),
    REST("休息"),
    NORMAL("正常"),

    // 打卡状态
    NOT_CHECKED("未打卡"),
    EARLY_CHECKED("早退"),
    LATE_CHECKED("迟到"),
    LACK_CHECKED("缺卡"),
    LOSE_CHECKED("无效"),
    PROCESSED("已处理"),
    EXPIRED("已过期"),
    WORK_OUT("外勤"),

    ;
    private final String remarks;
    /**
     * 上班、下班状态
     */
    public static final List<String> clockType = new ArrayList<>();
    /**
     * 打卡状态
     */
    public static final List<String> clockState = new ArrayList<>();
    /**
     * 打卡异常状态
     */
    public static final List<String> clockAbnormalState = new ArrayList<>();

    ClockInEnum(String remarks) {
        this.remarks = remarks;
    }

    static {
        clockType.add(START.name());
        clockType.add(END.name());

        clockState.add(NOT_CHECKED.name());
        clockState.add(NORMAL.name());
        clockState.add(EARLY_CHECKED.name());
        clockState.add(LATE_CHECKED.name());
        clockState.add(LACK_CHECKED.name());
        clockState.add(LOSE_CHECKED.name());

        clockAbnormalState.add(EARLY_CHECKED.name());
        clockAbnormalState.add(LATE_CHECKED.name());
        clockAbnormalState.add(LACK_CHECKED.name());

    }

    public static List<String> getClockType() {
        return new ArrayList<>();
    }

    public static String getRemarksByName(String name) {
        String remarks = "";
        if (ObjectUtils.isEmpty(name)) {
            return remarks;
        }
        ClockInEnum[] clock = ClockInEnum.values();
        for (ClockInEnum ex : clock) {
            if (name.equals(ex.name())) {
                return ex.getRemarks();
            }
        }
        return remarks;
    }
}
