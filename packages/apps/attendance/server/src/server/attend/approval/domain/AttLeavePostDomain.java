package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.domain;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.dto.AttLeavePostFromDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttApproveLogPO;
import com.guhecloud.platform.base.util.JsonUtils;

import java.util.Objects;

/**
 * 离岗
 */
public class AttLeavePostDomain {

    public static void leavePostToApproveLog(AttApproveLogPO logPO, String leavePost) {
        AttLeavePostFromDTO leavePostFromDTO = JsonUtils.deserialize(leavePost, AttLeavePostFromDTO.class);
        logPO.setId(IdWorker.getId());
        if (Objects.isNull(leavePostFromDTO)) {
            return;
        }
        logPO.setStartTime(leavePostFromDTO.getPostStartTime());
        logPO.setEndTime(leavePostFromDTO.getPostEndTime());
        logPO.setUserId(leavePostFromDTO.getApplicantId());
        logPO.setActualDuration(String.valueOf(leavePostFromDTO.getPostNumber()));
    }
}
