package com.guhecloud.cube.srv.gen.appraisals.server.consumer;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.appraisals.server.process.dao.FormAppraisalProcessMapper;
import com.guhecloud.cube.srv.gen.appraisals.server.process.entity.FormAppraisalProcess;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import instance.event.ProcessStartedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 表单考核流程发起监听事件
 */
@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = ProcessStartedMessage.TOPIC + "FORM_ASSESSMENT",
        topic = ProcessStartedMessage.TOPIC
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FormAppraisalProcessStartedConsumer implements RocketMQListener<ProcessStartedMessage> {
    private final FormAppraisalProcessMapper formAppraisalProcessMapper;
    private final UserClient userClient;

    @Override
    public void onMessage(ProcessStartedMessage message) {
        log.info("收到表单考核流程发起：{}", JsonUtils.serialize(message));
        if (message == null || StringUtils.isBlank(message.getProcessInstanceId()) || message.getStartUserId() == null ||
            !"FORM_ASSESSMENT".equals(message.getApplicationCode())) {
            return;
        }
        SecurityContextHolder.setPrincipal(Principal.builder().id(1L).type(Principal.Type.USER).name("system").account("system").build());
        SessionContextHolder.requestTenant(message.getTenant());
        var userDTO = userClient.getUser(message.getStartUserId());
        FormAppraisalProcess formAppraisalProcess = this.handlerFormAppraisalProcess(userDTO, message.getProcessInstanceId());
        if (formAppraisalProcess == null) {
            return;
        }
        formAppraisalProcessMapper.insert(formAppraisalProcess);
    }

    private FormAppraisalProcess handlerFormAppraisalProcess(UserDTO userDTO, String processInstanceId) {
        if (userDTO == null || StringUtils.isBlank(processInstanceId)) {
            return null;
        }
        Long organizationId = null;
        if (userDTO.getOrganization() != null) {
            organizationId = userDTO.getOrganization().getId();
        }
        String postIds = Optional.ofNullable(userDTO.getPositions())
                .orElse(CollectionUtils.emptyList())
                .stream()
                .map(position -> String.valueOf(position.getId()))
                .collect(Collectors.joining(","));
        String roleIds = Optional.ofNullable(userDTO.getRoles())
                .orElse(CollectionUtils.emptyList())
                .stream()
                .map(role -> String.valueOf(role.getId()))
                .collect(Collectors.joining(","));
        return FormAppraisalProcess.builder()
                .id(IdWorker.getId())
                .processInstanceId(processInstanceId)
                .userId(userDTO.getId())
                .organizationId(organizationId)
                .postIds(postIds)
                .roleIds(roleIds)
                .build();
    }
}
