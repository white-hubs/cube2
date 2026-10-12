package com.guhecloud.cube.srv.gen.safety.personnel.server.sync.consumer;

import com.guhecloud.cube.srv.gen.personnel.management.client.message.UpdateStaffMessage;
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
 * 人事员工更新消息消费者。
 */
@Slf4j
@Component("safetyPersonnelUpdateStaffConsumer")
@RequiredArgsConstructor
@RocketMQMessageListener(consumerGroup = UpdateStaffMessage.TOPIC + "_SAFETY_PERSONNEL", topic = UpdateStaffMessage.TOPIC)
public class SafetyPersonnelUpdateStaffConsumer implements RocketMQListener<UpdateStaffMessage> {

    private static final String LEAVE_OFFICE_STATUS = "LEAVE_OFFICE";

    private final SafetyPersonnelPersonnelFacadeService personnelFacade;
    private final SafetyPersonnelUserStatusSyncService userStatusSyncService;

    @Override
    public void onMessage(UpdateStaffMessage message) {
        if (message == null || !isLeaveOffice(message)) {
            return;
        }
        try {
            if (message.getTenant() != null) {
                SessionContextHolder.requestTenant(message.getTenant());
            }
            Long userId = resolveUserId(message.getUserId(), message.getStaffId());
            if (userId == null) {
                log.warn("安全人员消费人事离职消息时无法解析OUAA用户，message={}", JsonUtils.serialize(message));
                return;
            }
            userStatusSyncService.markLeft(userId);
        } catch (Exception e) {
            log.error("安全人员消费人事离职消息失败，message={}", JsonUtils.serialize(message), e);
        } finally {
            SessionContextHolder.clearSessionContext();
        }
    }

    private boolean isLeaveOffice(UpdateStaffMessage message) {
        return message.isLeaveOffice() || LEAVE_OFFICE_STATUS.equals(message.getStatus());
    }

    private Long resolveUserId(Long userId, Long staffId) {
        if (userId != null || staffId == null) {
            return userId;
        }
        return personnelFacade.resolveUserIdByStaffId(staffId);
    }
}
