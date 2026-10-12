package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.consumer;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.message.UserUpdateMessage;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository.AttClockInLedgerRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.infrastructure.mapper.AttDutyMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.infrastructure.mapper.AttDutyStaffMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.infrastructure.po.AttDutyPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.infrastructure.po.AttDutyStaffPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttGroupStaffMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupStaffPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.machine.application.dumu.UserManageApplication;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.commandgateway.AttSchedulingStaffCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.mapper.AttSchedulingMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po.AttSchedulingPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.param.AttRenewUserInfoParam;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 监听ouaa修改人员消息
 */
@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = UserUpdateMessage.TOPIC + "_att-userinfo",
        topic = UserUpdateMessage.TOPIC,
        selectorType = SelectorType.TAG
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttUserUpdateConsumer implements RocketMQListener<UserUpdateMessage> {
    private final AttSchedulingMapper attSchedulingMapper;
    private final AttDutyStaffMapper attDutyStaffMapper;
    private final UserManageApplication userManageApplication;
    private final AttSchedulingStaffCommandService attSchedulingStaffCommandService;
    private final AttGroupStaffMapper attGroupStaffMapper;
    private final AttDutyMapper attDutyMapper;
    private final UserClient userClient;
    private final AttClockInLedgerRepository attClockInLedgerRepository;

    @Override
    public void onMessage(UserUpdateMessage userUpdateMessage) {
        log.info("att:收到ouaa修改人员消息：{}", JsonUtils.serialize(userUpdateMessage));
        userManageApplication.addUser(userUpdateMessage);

        String staffDeptName = null;
        String organizations = null;
        try {
            UserDTO userDTO = userClient.getUser(userUpdateMessage.getId());
            UserDTO.Organization organizationDTO = userDTO.getOrganization();
            if (organizationDTO != null) {
                staffDeptName = organizationDTO.getName();
            }
            List<UserDTO.Organization> organizationList = userDTO.getOrganizations();
            if (organizationList != null && !organizationList.isEmpty()) {
                organizations = JsonUtils.serialize(organizationList);
            }
        } catch (Exception e) {
            log.error("消费ouaa修改人员,获取部门失败：", e);
        }

        // 更新考勤组人员
        try {
            attGroupStaffMapper.update(null, Wrappers.<AttGroupStaffPO>lambdaUpdate()
                                               .eq(AttGroupStaffPO::getStaffId, userUpdateMessage.getId())
                                               .set(AttGroupStaffPO::getName, userUpdateMessage.getName())
                                               .set(AttGroupStaffPO::getDeptId, userUpdateMessage.getOrganizationId())
                                               .set(StringUtils.isNotBlank(staffDeptName), AttGroupStaffPO::getDeptName, staffDeptName)
                                               .set(StringUtils.isNotBlank(organizations), AttGroupStaffPO::getOrganizations, organizations)
                                      );
        } catch (Exception e) {
            log.error("消费ouaa修改人员,更新考勤组人员失败：", e);
        }

        // 更新排班人员
        try {
            SessionContextHolder.requestTenant(userUpdateMessage.getTenant());
            AttRenewUserInfoParam param = AttRenewUserInfoParam.builder()
                    .id(userUpdateMessage.getId())
                    .name(userUpdateMessage.getName())
                    .tenant(userUpdateMessage.getTenant())
                    .build();
            attSchedulingStaffCommandService.renewSchedulingStaff(param);
        } catch (Exception e) {
            log.error("消费ouaa修改人员,更新排班人员失败：", e);
        }

        // 更新值班人员
        try {
            attDutyStaffMapper.update(null, Wrappers.<AttDutyStaffPO>lambdaUpdate()
                                              .eq(AttDutyStaffPO::getStaffId, userUpdateMessage.getId())
                                              .set(AttDutyStaffPO::getStaffName, userUpdateMessage.getName())
                                     );
        } catch (Exception e) {
            log.error("消费ouaa修改人员,更新值班人员失败：", e);
        }

        // 更新值班表中的信息
        try {
            attDutyMapper.update(null, Wrappers.<AttDutyPO>lambdaUpdate()
                                         .eq(AttDutyPO::getStaffId, userUpdateMessage.getId())
                                         .gt(AttDutyPO::getDutyDate, LocalDateTime.now())
                                         .set(AttDutyPO::getStaffName, userUpdateMessage.getName())
                                         .set(AttDutyPO::getStaffDeptId, userUpdateMessage.getOrganizationId())
                                         .set(staffDeptName != null, AttDutyPO::getStaffDeptName, staffDeptName)
                                );
        } catch (Exception e) {
            log.error("消费ouaa修改人员,更新值班表失败：", e);
        }

        // 更新排班表中的人员信息
        try {
            attSchedulingMapper.update(null, Wrappers.<AttSchedulingPO>lambdaUpdate()
                                               .eq(AttSchedulingPO::getStaffId, userUpdateMessage.getId())
                                               .gt(AttSchedulingPO::getSchedulingDate, LocalDateTime.now())
                                               .set(AttSchedulingPO::getStaffName, userUpdateMessage.getName())
                                               .set(AttSchedulingPO::getStaffDeptId, userUpdateMessage.getOrganizationId())
                                               .set(staffDeptName != null, AttSchedulingPO::getStaffDeptName, staffDeptName)
                                      );
        } catch (Exception e) {
            log.error("消费ouaa修改人员,修改排班人员信息失败：", e);
        }

        // 更新考勤照片
        if (userUpdateMessage.isPhotographEdit()) {
            AttRenewUserInfoParam param = AttRenewUserInfoParam.builder()
                    .id(userUpdateMessage.getId())
                    .name(userUpdateMessage.getName())
                    .photographKey(userUpdateMessage.getPhotographKey())
                    .tenant(userUpdateMessage.getTenant())
                    .build();
            try {
                attSchedulingStaffCommandService.renewStaffPhoto(param);
            } catch (Exception e) {
                log.error("消费ouaa修改人员,更新考勤照片出错：", e);
            }
        }
        if (ObjectUtils.isNotEmpty(userUpdateMessage.getOrganizationId())) {
            attClockInLedgerRepository.updateOrgByUserId(userUpdateMessage.getId(), userUpdateMessage.getOrganizationId());
        }
    }
}
