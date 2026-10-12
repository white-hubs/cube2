package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.domain;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.dto.AttAwayBusinessFromDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttApproveLogPO;
import com.guhecloud.platform.base.util.JsonUtils;

import java.util.Objects;

/**
 * 出差
 */
public class AttAwayBusinessDomain {

    public static void awayBusinessToApproveLog(AttApproveLogPO logPO, String awayBusiness) {
        AttAwayBusinessFromDTO awayBusinessFromDTO = JsonUtils.deserialize(awayBusiness, AttAwayBusinessFromDTO.class);
        logPO.setId(IdWorker.getId());
        if (Objects.nonNull(awayBusinessFromDTO)) {
            logPO.setStartTime(awayBusinessFromDTO.getAwayStartTime());
            logPO.setEndTime(awayBusinessFromDTO.getAwayEndTime());
            logPO.setUserId(awayBusinessFromDTO.getApplicantId());
            logPO.setActualDuration(String.valueOf(awayBusinessFromDTO.getAwayNumber()));
        }
    }
}
