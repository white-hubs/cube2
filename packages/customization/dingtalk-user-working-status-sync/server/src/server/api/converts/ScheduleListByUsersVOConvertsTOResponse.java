package com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.api.converts;

import com.dingtalk.api.response.OapiAttendanceScheduleListbyusersResponse;
import com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.api.dto.response.DingTalkScheduleListByUsersResponse;
import com.guhecloud.platform.base.util.DateUtils;

import java.util.List;
import java.util.stream.Collectors;

public final class ScheduleListByUsersVOConvertsTOResponse {

    private ScheduleListByUsersVOConvertsTOResponse() {

    }

    public static DingTalkScheduleListByUsersResponse scheduleListByUsersVOConvertsTOResponse(
            OapiAttendanceScheduleListbyusersResponse.TopScheduleVo vo
                                                                                             ) {
        return DingTalkScheduleListByUsersResponse.builder()
                .checkType(vo.getCheckType())
                .planCheckTime(DateUtils.parse(vo.getPlanCheckTime()))
                .groupId(vo.getGroupId())
                .approveId(vo.getApproveId())
                .userid(vo.getUserid())
                .workDate(DateUtils.parse(vo.getWorkDate()))
                .id(vo.getId())
                .shiftId(vo.getShiftId())
                .shiftVersion(vo.getShiftVersion())
                .isRest(vo.getIsRest())
                .build();
    }

    public static List<DingTalkScheduleListByUsersResponse> scheduleListByUsersVOSConvertsTOResponses(
            List<OapiAttendanceScheduleListbyusersResponse.TopScheduleVo> vos
                                                                                                     ) {
        return vos.stream().map(ScheduleListByUsersVOConvertsTOResponse::scheduleListByUsersVOConvertsTOResponse)
                .collect(Collectors.toList());
    }
}
