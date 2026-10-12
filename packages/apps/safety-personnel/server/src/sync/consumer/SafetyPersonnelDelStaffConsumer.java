package com.guhecloud.cube.srv.gen.safety.personnel.server.sync.consumer;

import com.guhecloud.cube.srv.gen.personnel.management.client.message.DelStaffMessage;
import com.guhecloud.cube.srv.gen.safety.personnel.server.common.facade.SafetyPersonnelPersonnelFacadeService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.sync.service.SafetyPersonnelUserStatusSyncService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * 人事员工删除消息消费者。
 */
@Slf4j
@Component("safetyPersonnelDelStaffConsumer")
@RequiredArgsConstructor
@RocketMQMessageListener(consumerGroup = DelStaffMessage.TOPIC + "_SAFETY_PERSONNEL", topic = DelStaffMessage.TOPIC)
public class SafetyPersonnelDelStaffConsumer implements RocketMQListener<DelStaffMessage> {

    private final SafetyPersonnelPersonnelFacadeService personnelFacade;
    private final SafetyPersonnelUserStatusSyncService userStatusSyncService;

    @Override
    public void onMessage(DelStaffMessage message) {
        if (message == null) {
            return;
        }
        try {
            if (message.getTenant() != null) {
                SessionContextHolder.requestTenant(message.getTenant());
            }
            Long userId = resolveUserId(message.getUserId(), message.getStaffId());
            if (userId == null) {
                log.warn("安全人员消费人事删除消息时无法解析OUAA用户，message={}", JsonUtils.serialize(message));
                return;
            }
            userStatusSyncService.markDeleted(userId);
        } catch (Exception e) {
            log.error("安全人员消费人事删除消息失败，message={}", JsonUtils.serialize(message), e);
        } finally {
            SessionContextHolder.clearSessionContext();
        }
    }

    private Long resolveUserId(Long userId, Long staffId) {
        if (userId != null || staffId == null) {
            return userId;
        }
        return personnelFacade.resolveUserIdByStaffId(staffId);
    }
}
