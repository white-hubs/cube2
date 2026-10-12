package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.application.event;

import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.enumeration.WorkOvertimeResultEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository.AttApproveLogRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttApproveLogPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.application.commandgateway.AttHolidayBalanceCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.param.WorkOvertimeDetailsParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.param.WorkOvertimeSituationParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.server.WorkOvertimeLedgerAdvanceServer;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.server.WorkOvertimeLedgerServer;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.ApprovalBizEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.WorkOvertimeCompensateEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.WorkOvertimeRuleDetailPO;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import instance.event.ProcessCompletedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 加班审批通过后，消费加班信息
 */

@Slf4j
@Component("Overtime_Approval_Over")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = ProcessCompletedMessage.TOPIC + "_OVERTIME", topic = ProcessCompletedMessage.TOPIC,
                         selectorExpression = "OVERTIME"
)
public class WorkOvertimeApprovalOverConsumer implements RocketMQListener<ProcessCompletedMessage> {
    private final AttApproveLogRepository approveLogRepository;
    private final WorkOvertimeLedgerServer workOvertimeLedgerServer;
    private final AttHolidayBalanceCommandService attHolidayBalanceCommandService;
    private final WorkOvertimeLedgerAdvanceServer workOvertimeLedgerAdvanceServer;

    @Override
    public void onMessage(ProcessCompletedMessage message) {
        log.info("加班审批通过：{}", JSONObject.toJSONString(message));
        List<AttApproveLogPO> attApproveLog = approveLogRepository.getApproveLog(message.getProcessInstanceId());
        if (ObjectUtils.isEmpty(attApproveLog)) {
            log.info("未找到审批信息：{}", message.getProcessInstanceId());
            return;
        }
        if (ObjectUtils.isNotEmpty(attApproveLog) && attApproveLog.getFirst().getStatus().equals(FlowResult.FINISHED.name())) {
            log.info("重复消费，无需处理：{}", JSONObject.toJSONString(message));
            return;
        }
        String status = FlowResult.PASS.name().equals(message.getResult().name()) ? FlowResult.FINISHED.name() : message.getResult().name();
        String remarks = "";
        if (FlowResult.PASS.name().equals(message.getResult().name())) {
            try {
                Map<String, WorkOvertimeRuleDetailPO> workOvertimeRuleMap = new HashMap<>();
                this.getWorkOvertimeRule(attApproveLog, workOvertimeRuleMap);
                WorkOvertimeSituationParam command = WorkOvertimeSituationParam.getWorkOvertimeSituationParam(message.getData());
                command.setApplyTime(message.getStartTime());
                command.setApplyDoneTime(message.getCompletedTime());
                remarks = this.workOverIntoHolidayBalance(command.getOvertimeDetails(), workOvertimeRuleMap, command.getApplicantId(), message.getProcessInstanceId());
                Boolean bool = workOvertimeLedgerServer.addWorkOvertime(command, attApproveLog);
                log.info("写入台账结果：{}", bool);
            } catch (Exception e) {
                log.error("加班审批通过后,消费报错：", e);
                remarks = "加班审批通过后,消费报错" + e.getMessage() +
                          (e.getMessage().length() > 100 ? e.getMessage().substring(0, 100) : e.getMessage());
            }
        }
        Boolean bool = approveLogRepository.updateApproveLog(status, remarks, message.getProcessInstanceId());
        log.info("修改审批记录结果：{}", bool);
        workOvertimeLedgerAdvanceServer.updateWorkOvertime(message.getProcessInstanceId(), status);
    }

    /**
     * 通过解析获得加班规则
     */
    private String getWorkOvertimeRule(List<AttApproveLogPO> approveLogs, Map<String, WorkOvertimeRuleDetailPO> workOvertimeRuleMap) {
        AtomicReference<String> remarks = new AtomicReference<>(WorkOvertimeResultEnum.OK.getRemarks());
        try {
            approveLogs.forEach(ex -> {
                WorkOvertimeRuleDetailPO ruleDetail = JsonUtils.deserialize(ex.getProportion(), WorkOvertimeRuleDetailPO.class);
                if (Objects.nonNull(ruleDetail)) {
                    workOvertimeRuleMap.put(ruleDetail.getType(), ruleDetail);
                }
            });
        } catch (Exception e) {
            log.error("解析加班规则报错：", e);
            remarks.set(WorkOvertimeResultEnum.WORK_OVERTIME_ANALYSIS.getRemarks() +
                        (e.getMessage().length() > 100 ? e.getMessage().substring(0, 100) : e.getMessage()));
        }
        return remarks.get();
    }

    /**
     * 加班计入调休
     */
    private String workOverIntoHolidayBalance(List<WorkOvertimeDetailsParam> overtimeDetails,
                                              Map<String, WorkOvertimeRuleDetailPO> workOvertimeRuleMap, Long applicantId,
                                              String processInstanceId
                                             ) {
        AtomicReference<String> remarks = new AtomicReference<>("");
        overtimeDetails.forEach(ex -> {
            WorkOvertimeRuleDetailPO ruleDetail = new WorkOvertimeRuleDetailPO();
            try {
                //如果没有设置补偿方式，则取规则的补偿方式
                if (ObjectUtils.isEmpty(ex.getCompensate())) {
                    ruleDetail = workOvertimeRuleMap.get(ex.getWorkType());
                    if (ObjectUtils.isNotEmpty(ruleDetail)) {
                        ex.setCompensate(ruleDetail.getCompensate());
                    }
                }
                boolean bool = WorkOvertimeCompensateEnum.LEAVE.name().equals(ex.getCompensate());
                remarks.set(ruleDetail.getCompensate());
                if (bool) {
                    //如果没有设置比例，默认是1：1
                    BigDecimal leaveDuration = ex.getWorkDuration()
                            .multiply(new BigDecimal(Optional.ofNullable(ruleDetail.getProportion()).orElse("1")));
                    //加班时间计入假期余额
                    remarks.set(attHolidayBalanceCommandService.workOverIntoHolidayBalance(applicantId, leaveDuration.toString(), processInstanceId, ApprovalBizEnum.OVERTIME.name()));
                }
            } catch (Exception e) {
                log.error("更改调休时间报错：", e);
                remarks.set(WorkOvertimeResultEnum.LEAVE_EXCEPTION.getRemarks() +
                            (e.getMessage().length() > 100 ? e.getMessage().substring(0, 100) : e.getMessage()));
            }
        });
        return remarks.get();
    }
}