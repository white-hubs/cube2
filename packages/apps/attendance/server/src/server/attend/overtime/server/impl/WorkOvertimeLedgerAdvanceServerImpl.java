package com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.server.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttApproveLogPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.dao.WorkOvertimeLedgerAdvanceMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.entity.WorkOvertimeLedger;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.entity.WorkOvertimeLedgerAdvance;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.param.WorkOvertimeSituationParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.result.WorkOvertimeLedgerPageResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.server.WorkOvertimeLedgerAdvanceServer;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.server.WorkOvertimeLedgerServer;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.WorkOvertimeRuleDetailPO;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Author: lijie
 * @CreateTime: 2024-03-12  10:47
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkOvertimeLedgerAdvanceServerImpl extends ServiceImpl<WorkOvertimeLedgerAdvanceMapper, WorkOvertimeLedgerAdvance> implements WorkOvertimeLedgerAdvanceServer {
    private final WorkOvertimeLedgerServer workOvertimeLedgerServer;

    @Override
    public Boolean updateWorkOvertime(String processInstanceId, String status) {
        try {
            return this.update().set("status", status).eq("process_instance_id", processInstanceId).update();
        } catch (Exception e) {
            log.error("更新加班预统计失败");
        }
        return false;
    }

    @Override
    public Boolean addWorkOvertime(WorkOvertimeSituationParam command, List<AttApproveLogPO> attApproveLog) {
        List<WorkOvertimeLedgerAdvance> ledgerList = new ArrayList<>();
        attApproveLog.forEach(ex -> {
            command.getOvertimeDetails().forEach(detailsParam -> {
                if (ObjectUtils.isEmpty(ex.getSubDataId()) || ex.getSubDataId().equals(detailsParam.getSubDataId())) {
                    long num = DateUtils.between(ex.getStartTime(), ex.getEndTime(), ChronoUnit.DAYS, true);
                    for (int i = 0; i <= num; i++) {
                        LocalDateTime startTime =
                                i > 0 ? DateUtils.beginOfDay(DateUtils.offsetDay(ex.getStartTime(), i)) : ex.getStartTime();
                        LocalDateTime endTime =
                                DateUtils.isSameDay(startTime, ex.getEndTime()) ? ex.getEndTime() : DateUtils.endOfDay(startTime);
                        String compensate = ObjectUtils.isNotEmpty(detailsParam.getCompensate()) ? detailsParam.getCompensate()
                                                                                                 : this.getWorkOvertimeRule(ex)
                                                    .getCompensate();
                        WorkOvertimeLedger workOvertimeLedger = workOvertimeLedgerServer.getWorkOvertimeLedger(startTime, endTime, command, ex, compensate);
                        WorkOvertimeLedgerAdvance workOvertimeLedgerAdvance = ObjectUtils.copying(workOvertimeLedger, WorkOvertimeLedgerAdvance.class);
                        workOvertimeLedgerAdvance.setProcessInstanceId(ex.getProcessInstanceId());
                        workOvertimeLedgerAdvance.setStatus(FlowResult.PASS.name());
                        ledgerList.add(workOvertimeLedgerAdvance);
                    }
                }
            });
        });
        return this.saveBatch(ledgerList);
    }

    @Override
    public List<WorkOvertimeLedgerPageResult> getWorkOvertimeLedgerByUserIds(Set<Long> userIds, Period period) {
        return this.baseMapper.getWorkOvertimeLedgerByUserIds(userIds, period);
    }

    /**
     * 通过解析获得加班规则
     */
    private WorkOvertimeRuleDetailPO getWorkOvertimeRule(AttApproveLogPO approveLog) {
        WorkOvertimeRuleDetailPO ruleDetail = new WorkOvertimeRuleDetailPO();
        try {
            ruleDetail = JsonUtils.deserialize(approveLog.getProportion(), WorkOvertimeRuleDetailPO.class);
        } catch (Exception e) {
            log.error("解析加班规则报错：", e);
        }
        return ruleDetail;
    }
}
