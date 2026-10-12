package com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.infrastructure.repository.converters;

import com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.domain.entity.DingTalkSchedule;
import com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.domain.po.DingTalkSchedulePO;

import java.util.List;

public final class DingTalkScheduleConvertsPO {

    private DingTalkScheduleConvertsPO() {}

    public static DingTalkSchedulePO convertDingTalkScheduleToPO(DingTalkSchedule dingTalkSchedule) {
        return DingTalkSchedulePO.builder()
                .dingTalkScheduleId(dingTalkSchedule.getDingTalkScheduleId())
                .checkType(dingTalkSchedule.getCheckType())
                .planCheckTime(dingTalkSchedule.getPlanCheckTime())
                .groupId(dingTalkSchedule.getGroupId())
                .userid(dingTalkSchedule.getUserid())
                .approveId(dingTalkSchedule.getApproveId())
                .workDate(dingTalkSchedule.getWorkDate())
                .shiftVersion(dingTalkSchedule.getShiftVersion())
                .shiftId(dingTalkSchedule.getShiftId())
                .isRest(dingTalkSchedule.getIsRest())
                .build();
    }

    public static List<DingTalkSchedulePO> convertDingTalkScheduleToPOS(List<DingTalkSchedule> dingTalkSchedules) {
        return dingTalkSchedules.stream().map(DingTalkScheduleConvertsPO::convertDingTalkScheduleToPO).toList();
    }
}
