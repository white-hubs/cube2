package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.consumer;

import com.guhecloud.cube.biz.ouaa.client.message.IncreaseTenantUserMessage;
import com.guhecloud.cube.biz.ouaa.client.message.UserUpdateMessage;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.machine.application.dumu.UserManageApplication;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.commandgateway.AttSchedulingStaffCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.param.AttRenewUserInfoParam;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 监听ouaa添加人员消息/监听ouaa新增人员消息
 */
@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = IncreaseTenantUserMessage.TOPIC + "_att-userinfo",
        topic = IncreaseTenantUserMessage.TOPIC,
        selectorType = SelectorType.TAG
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttUserAddConsumer implements RocketMQListener<IncreaseTenantUserMessage> {

    private final AttSchedulingStaffCommandService attSchedulingStaffCommandService;
    private final UserManageApplication userManageApplication;

    @Override
    public void onMessage(IncreaseTenantUserMessage message) {
        log.info("收到ouaa添加人员消息：{}", JsonUtils.serialize(message));
        SessionContextHolder.requestTenant(message.getTenant());
        try {
            userManageApplication.addUser(ObjectUtils.copying(message, UserUpdateMessage.class));
        } catch (Exception e) {
            log.error("考勤机同步人员异常：", e);
        }
        final AttRenewUserInfoParam param = AttRenewUserInfoParam.builder()
                .id(message.getId())
                .name(message.getName())
                .photographKey(message.getPhotographKey())
                .tenant(message.getTenant())
                .build();
        // 更新考勤照片
        if (message.getPhotographKey() != null) {
            try {
                attSchedulingStaffCommandService.renewStaffPhoto(param);
            } catch (Exception e) {
                log.error("消费ouaa添加人员，更新考勤照片出错：", e);
            }
        }
        // 更新月排班人员
        try {
            attSchedulingStaffCommandService.renewSchedulingStaff(param);
        } catch (Exception e) {
            log.error("消费ouaa添加人员，更新月排班人员出错：", e);
        }

    }
}
