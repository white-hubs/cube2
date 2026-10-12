package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.application.event;

import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.domain.AttAskLeaveDomain;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.domain.AttAwayBusinessDomain;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.domain.AttChangeClassDomain;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.domain.AttLeavePostDomain;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.application.commandgateway.AttOverTimeApplication;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.application.commandgateway.AttWorkOmitApplication;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.application.constant.AttVersionConstant;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository.AttApproveLogRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttApproveLogPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.ClassesStatusTriggerEnum;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import instance.event.ProcessStartedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2023-11-08  15:41
 * @Description: 流程发起审批消费
 * @Version: 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = ProcessStartedMessage.TOPIC + "_ATT_STARTED", topic = ProcessStartedMessage.TOPIC)
public class FlowStartedEvent implements RocketMQListener<ProcessStartedMessage> {
    private final AttApproveLogRepository approveLogRepository;
    private final AttWorkOmitApplication workOmitApplication;
    private final AttOverTimeApplication overTimeApplication;
    private final AttAskLeaveDomain attAskLeaveDomain;

    private static String STATUS = "PROGRESS";

    @Override
    public void onMessage(ProcessStartedMessage message) {
        log.info("流程发起审批消费：" + JSONObject.toJSONString(message));
        try {
            List<AttApproveLogPO> logPO = approveLogRepository.getApproveLog(message.getProcessInstanceId());
            if (ObjectUtils.isNotEmpty(logPO)) {
                log.info("重复消费：{}", message.getProcessInstanceId());
                return;
            }
            for (String ex : ClassesStatusTriggerEnum.getAllName()) {
                if (ex.equals(message.getBusinessType())) {
                    List<AttApproveLogPO> approveLogList = new ArrayList<>();
                    AttApproveLogPO approveLog = new AttApproveLogPO();
                    approveLog.setStatus(STATUS);
                    approveLog.setProcessInstanceId(message.getProcessInstanceId());
                    approveLog.setVersion(AttVersionConstant.VERSION);
                    if (ex.equals(ClassesStatusTriggerEnum.REPAIR_CLOCK.name())) {
                        workOmitApplication.getApproveLog(approveLog, message.getData());
                    } else if (ex.equals(ClassesStatusTriggerEnum.OVERTIME.name())) {
                        overTimeApplication.getApproveLog(approveLogList, approveLog, message.getData());
                        approveLog = null;
                    } else if (ex.equals(ClassesStatusTriggerEnum.LEAVE.name())) {
                        // 请假
                        approveLog.setBusinessType(ClassesStatusTriggerEnum.LEAVE.name());
                        attAskLeaveDomain.askLeaveToApproveLog(approveLog, message.getData());
                    } else if (ex.equals(ClassesStatusTriggerEnum.LEAVE_THE_POST.name())) {
                        // 离岗
                        approveLog.setSceneCode(ClassesStatusTriggerEnum.LEAVE_THE_POST.name());
                        approveLog.setSceneName(ClassesStatusTriggerEnum.LEAVE_THE_POST.getDesc());
                        approveLog.setBusinessType(ClassesStatusTriggerEnum.LEAVE_THE_POST.name());
                        AttLeavePostDomain.leavePostToApproveLog(approveLog, message.getData());
                    } else if (ex.equals(ClassesStatusTriggerEnum.CHANGE_CLASSES.name())) {
                        // 换班
                        approveLog.setSceneCode(ClassesStatusTriggerEnum.CHANGE_CLASSES.name());
                        approveLog.setSceneName(ClassesStatusTriggerEnum.CHANGE_CLASSES.getDesc());
                        approveLog.setBusinessType(ClassesStatusTriggerEnum.CHANGE_CLASSES.name());
                        AttChangeClassDomain.changeClassToApproveLog(approveLog, message.getData());
                    } else if (ex.equals(ClassesStatusTriggerEnum.AWAY_BUSINESS.name())) {
                        // 出差
                        approveLog.setBusinessType(ClassesStatusTriggerEnum.AWAY_BUSINESS.name());
                        approveLog.setSceneCode(ClassesStatusTriggerEnum.AWAY_BUSINESS.name());
                        approveLog.setSceneName(ClassesStatusTriggerEnum.AWAY_BUSINESS.getDesc());
                        AttAwayBusinessDomain.awayBusinessToApproveLog(approveLog, message.getData());
                    }
                    if (ObjectUtils.isNotEmpty(approveLog)) {
                        approveLogList.add(approveLog);
                    }
                    Boolean bool = approveLogRepository.addApproveLog(approveLogList);
                    log.info("新增审批记录结果：{}", bool);
                }
            }
        } catch (Exception e) {
            log.error("流程发起审批消费报错：" + JsonUtils.serialize(message), e);
        }
    }
}
