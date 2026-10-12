package com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.infrastructure.repository.converters;

import com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.domain.entity.DingTalkSchedule;
import com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.domain.po.DingTalkSchedulePO;

import java.util.List;
import java.util.stream.Collectors;

public final class DingTalkSchedulePOConvertsEntity {

    private DingTalkSchedulePOConvertsEntity() {}

    public static DingTalkSchedule convertDingTalkSchedulePOToEntity(DingTalkSchedulePO dingTalkSchedulePO) {
        return DingTalkSchedule.builder()
                .id(dingTalkSchedulePO.getId())
                .dingTalkScheduleId(dingTalkSchedulePO.getDingTalkScheduleId())
                .checkType(dingTalkSchedulePO.getCheckType())
                .planCheckTime(dingTalkSchedulePO.getPlanCheckTime())
                .groupId(dingTalkSchedulePO.getGroupId())
                .userid(dingTalkSchedulePO.getUserid())
                .approveId(dingTalkSchedulePO.getApproveId())
                .workDate(dingTalkSchedulePO.getWorkDate())
                .shiftVersion(dingTalkSchedulePO.getShiftVersion())
                .shiftId(dingTalkSchedulePO.getShiftId())
                .isRest(dingTalkSchedulePO.getIsRest())
                .tenant(dingTalkSchedulePO.getTenant())
                .build();
    }

    public static List<DingTalkSchedule> convertDingTalkSchedulePOToEntities(List<DingTalkSchedulePO> dingTalkSchedulePOS) {
        return dingTalkSchedulePOS.stream().map(DingTalkSchedulePOConvertsEntity::convertDingTalkSchedulePOToEntity)
                .collect(Collectors.toList());
    }
}
