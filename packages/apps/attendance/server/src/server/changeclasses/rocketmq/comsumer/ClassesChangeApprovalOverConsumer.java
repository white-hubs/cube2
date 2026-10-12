package com.guhecloud.cube.srv.gen.attendance.management.server.changeclasses.rocketmq.comsumer;

import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.commandgateway.AttClassExchangeCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.dto.AttExchangeCheckDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.querygateway.AttClassExchangeQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository.AttApproveLogRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttApproveLogPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.changeclasses.commandgateway.command.ClassesChangeCommand;
import com.guhecloud.platform.base.util.JsonUtils;
import instance.event.ProcessCompletedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 换班审批通过后，消费换班信息
 */

@Slf4j
@Component("ClassesChange_Approval_Over")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = ProcessCompletedMessage.TOPIC + "_CHANGE_CLASSES", topic = ProcessCompletedMessage.TOPIC,
                         selectorExpression = "CHANGE_CLASSES"
)
public class ClassesChangeApprovalOverConsumer implements RocketMQListener<ProcessCompletedMessage> {
    private final AttClassExchangeQueryService attClassExchangeQueryService;
    private final AttClassExchangeCommandService attClassExchangeCommandService;
    private final AttApproveLogRepository approveLogRepository;

    @Override
    public void onMessage(ProcessCompletedMessage message) {
        log.warn("换班审批通过日志：{}", JSONObject.toJSONString(message));

        AttApproveLogPO po = new AttApproveLogPO();
        po.setStatus(FlowResult.PASS.name().equals(message.getResult().name()) ? FlowResult.FINISHED.name() : message.getResult().name());
        po.setProcessInstanceId(message.getProcessInstanceId());
        log.warn("换班审批通过：修改记录状态{}", JSONObject.toJSONString(po));
        try {
            approveLogRepository.updateApproveLog(po);
        } catch (Exception e) {
            log.error("换班审批通过：修改记录失败：{}", String.valueOf(e));
        }
        log.warn("换班审批通过：记录修改成功{}", JSONObject.toJSONString(po));
        //参数校验和填充数据
        if (FlowResult.PASS.name().equals(message.getResult().name())) {
            ClassesChangeCommand command = JsonUtils.deserialize(message.getData(), ClassesChangeCommand.class);
            command.setApplyTime(message.getStartTime());
            command.setApplyDoneTime(message.getCompletedTime());
            command.setApplicantId(message.getStartUserId());
            AttExchangeCheckDTO param = JsonUtils.deserialize(message.getData(), AttExchangeCheckDTO.class);
            try {
                if (attClassExchangeQueryService.classesChangeCheck(param)) {
                    attClassExchangeCommandService.classExchange(command);
                    log.warn("换班审批通过：换班成功{}========={}", JSONObject.toJSONString(command), JSONObject.toJSONString(param));
                } else {
                    log.warn("换班审批校验不通过");
                }

            } catch (Exception e) {
                log.error("换班申请批准后，异常：", e);
            }
        } else {
            log.info("换班审批：不是通过{}", message.getResult().name());
        }
    }
}
