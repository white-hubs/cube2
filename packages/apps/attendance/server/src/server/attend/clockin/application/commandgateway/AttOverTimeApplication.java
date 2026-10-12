package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.application.commandgateway;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.application.constant.AttVersionConstant;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttApproveLogPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.repository.AttGroupRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.param.WorkOvertimeSituationParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.ClassesStatusTriggerEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.WorkOvertimeRuleDetailMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.WorkOvertimeRuleDetailPO;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * @Author: lijie
 * @CreateTime: 2023-11-13  15:15
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttOverTimeApplication {
    private final WorkOvertimeRuleDetailMapper workOvertimeRuleDetailMapper;
    private final AttGroupRepository groupRepository;

    /**
     * 加班审批数据处理
     */
    public void getApproveLog(List<AttApproveLogPO> attApproveLogList, AttApproveLogPO approveLog, String data) {
        WorkOvertimeSituationParam command = WorkOvertimeSituationParam.getWorkOvertimeSituationParam(data);
        command.getOvertimeDetails().forEach(ex -> {
            AttApproveLogPO log = new AttApproveLogPO();
            AttGroupPO group = groupRepository.getGroupByStaffId(command.getApplicantId());
            if (ObjectUtils.isNotEmpty(group)) {
                if (ObjectUtils.isNotEmpty(group.getOvertimeRuleId()) && ObjectUtils.isNotEmpty(ex.getWorkType())) {
                    WorkOvertimeRuleDetailPO workOvertimeRuleDetailPO = workOvertimeRuleDetailMapper.getProportionByUserId(group.getOvertimeRuleId(), ex.getWorkType());
                    if (ObjectUtils.isNotEmpty(workOvertimeRuleDetailPO)) {
                        log.setProportion(JsonUtils.serialize(workOvertimeRuleDetailPO));
                    }
                }
            }
            log.setStatus(approveLog.getStatus());
            log.setProcessInstanceId(approveLog.getProcessInstanceId());
            log.setVersion(AttVersionConstant.VERSION);
            log.setUserId(command.getApplicantId());
            log.setApplyDuration(Optional.ofNullable(ex.getWorkDuration()).orElse(BigDecimal.ZERO).toString());
            log.setActualDuration(Optional.ofNullable(ex.getWorkDuration()).orElse(BigDecimal.ZERO).toString());
            String[] time = ex.getWorkPeriod().substring(1, ex.getWorkPeriod().length() - 1).split(",");
            log.setStartTime(DateUtils.parse(time[0].substring(1, time[0].length() - 1)));
            log.setEndTime(DateUtils.parse(time[1].substring(1, time[1].length() - 1)));
            log.setBusinessType(ClassesStatusTriggerEnum.OVERTIME.name());
            log.setSceneCode(ex.getWorkType());
            log.setSubDataId(ex.getSubDataId());
            attApproveLogList.add(log);
        });
    }
}
