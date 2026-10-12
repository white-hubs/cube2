package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.domain;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.dto.AttAskLeaveFromDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttApproveLogPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.mapper.AttHolidayRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.po.AttHolidayRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.ClassesStatusTriggerEnum;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 请假
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttAskLeaveDomain {

    private final AttHolidayRuleMapper attHolidayRuleMapper;

    public void askLeaveToApproveLog(AttApproveLogPO logPO, String askLeave) {
        AttAskLeaveFromDTO leaveFormDTO = JsonUtils.deserialize(askLeave, AttAskLeaveFromDTO.class);
        logPO.setId(IdWorker.getId());
        logPO.setSceneCode(ClassesStatusTriggerEnum.LEAVE.name());
        if (Objects.isNull(leaveFormDTO)) {
            return;
        }
        logPO.setStartTime(leaveFormDTO.getLeaveStartTime());
        logPO.setEndTime(leaveFormDTO.getLeaveEndTime());
        logPO.setUserId(leaveFormDTO.getApplicantId());
        logPO.setActualDuration(String.valueOf(leaveFormDTO.getLeaveNumber()));
        if (leaveFormDTO.getHolidayId() != null) {
            logPO.setSceneCode(String.valueOf(leaveFormDTO.getHolidayId()));
            AttHolidayRulePO rulePO = attHolidayRuleMapper.selectById(leaveFormDTO.getHolidayId());
            if (rulePO != null) {
                logPO.setSceneName(rulePO.getName());
            } else {
                log.error("请假申请未获取到假期规则:{}", askLeave);
            }
        }
    }
}
