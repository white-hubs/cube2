package com.guhecloud.cube.srv.gen.personnel.management.server.archive.appplication.event.consumer;

import com.guhecloud.cube.biz.ouaa.client.message.UserUpdateMessage;
import com.guhecloud.cube.srv.gen.personnel.management.server.commandgateway.StaffCommandService;
import com.guhecloud.cube.srv.gen.personnel.management.server.commandgateway.command.ChangeStaffCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = UserUpdateMessage.TOPIC + "_PERSONNEL",
        topic = UserUpdateMessage.TOPIC
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ChangeTenantUserConsumer implements RocketMQListener<UserUpdateMessage> {

    private final StaffCommandService staffCommandService;

    @Override
    public void onMessage(UserUpdateMessage userUpdateMessage) {
        log.info("收到更新租户用户消息,消息体:{}", userUpdateMessage);
        // 如果花名册通过离职更新的ouaa,现在收到ouaa的更新消息时，我们不做操作
        if (Objects.nonNull(userUpdateMessage.getEnabled()) && !userUpdateMessage.getEnabled()) {
            return;
        }
        ChangeStaffCommand changeStaffCommand = ChangeStaffCommand.builder()
                .gender(userUpdateMessage.getGender().name())
                .name(userUpdateMessage.getName())
                .organizationId(userUpdateMessage.getOrganizationId())
                .mobile(userUpdateMessage.getMobile())
                .userId(userUpdateMessage.getId())
                .postIds(userUpdateMessage.getPositionIds())
                .appointmentIds(userUpdateMessage.getAppointmentIds())
                .idCard(userUpdateMessage.getIdCard())
                .build();
        staffCommandService.changeStaffByUser(changeStaffCommand);
    }
}
