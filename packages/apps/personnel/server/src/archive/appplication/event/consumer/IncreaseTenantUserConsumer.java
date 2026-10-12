package com.guhecloud.cube.srv.gen.personnel.management.server.archive.appplication.event.consumer;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.ouaa.client.message.IncreaseTenantUserMessage;
import com.guhecloud.cube.srv.gen.personnel.management.server.commandgateway.StaffCommandService;
import com.guhecloud.cube.srv.gen.personnel.management.server.commandgateway.command.SaveToBePerfectCommand;
import com.guhecloud.cube.srv.gen.personnel.management.server.po.StaffPO;
import com.guhecloud.cube.srv.gen.personnel.management.server.querygateway.StaffQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = IncreaseTenantUserMessage.TOPIC + "_PERSONNEL",
        topic = IncreaseTenantUserMessage.TOPIC
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IncreaseTenantUserConsumer implements RocketMQListener<IncreaseTenantUserMessage> {

    private final StaffCommandService staffCommandService;
    private final StaffQueryService staffQueryService;

    @Override
    public void onMessage(IncreaseTenantUserMessage increaseTenantUserMessage) {
        log.info("收到新增租户用户消息,消息体:{}", increaseTenantUserMessage);
        // 调用新增员工
        SaveToBePerfectCommand saveToBePerfectCommand = SaveToBePerfectCommand.builder()
                .gender(increaseTenantUserMessage.getGender().name())
                .name(increaseTenantUserMessage.getName())
                .organizationId(increaseTenantUserMessage.getOrganizationId())
                .mobile(increaseTenantUserMessage.getMobile())
                .userId(increaseTenantUserMessage.getId())
                .postIds(increaseTenantUserMessage.getPositionIds())
                .appointmentIds(increaseTenantUserMessage.getAppointmentIds())
                .idCard(increaseTenantUserMessage.getIdCard())
                .build();

        long count = staffQueryService.count(
                Wrappers.<StaffPO>lambdaQuery()
                        .eq(StaffPO::getMobile, increaseTenantUserMessage.getMobile())
                                            );
        if (count <= 0) {
            staffCommandService.saveToBePerfect(saveToBePerfectCommand);
        }
    }
}
