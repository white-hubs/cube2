package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.event.consumer;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Sets;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.message.UserUpdateMessage;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttGroupStaffMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupStaffPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.machine.application.dumu.UserManageApplication;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.commandgateway.AttSchedulingStaffCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.param.AttRenewUserInfoParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.mapper.HolidayBalanceListMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.po.HolidayBalanceListPO;
import com.guhecloud.cube.srv.gen.personnel.management.client.StaffClient;
import com.guhecloud.cube.srv.gen.personnel.management.client.dto.StaffDetailDTO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 监听ouaa修改人员消息
 */
@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = "userUpdateConsumer",
        topic = UserUpdateMessage.TOPIC,
        selectorType = SelectorType.TAG
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserUpdateConsumer implements RocketMQListener<UserUpdateMessage> {
    private final StaffClient staffClient;
    private final OrganizationClient organizationClient;
    private final HolidayBalanceListMapper balanceListMapper;
    private final UserManageApplication userManageApplication;
    private final AttGroupStaffMapper attGroupStaffMapper;
    private final AttSchedulingStaffCommandService attSchedulingStaffCommandService;

    @Override
    public void onMessage(UserUpdateMessage userUpdateMessage) {
        log.info("收到ouaa修改人员消息：{}", JsonUtils.serialize(userUpdateMessage));
        userManageApplication.addUser(userUpdateMessage);
        String staffDeptName = null;
        try {
            OrganizationDTO organizationDTO = organizationClient.getOrganizationsById(userUpdateMessage.getOrganizationId());
            staffDeptName = organizationDTO.getName();
        } catch (Exception e) {
            log.error("消费ouaa修改人员,获取部门失败：", e);
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
                log.error("更新考勤照片出错：", e);
            }
        }
        // 更新考勤组人员
        try {
            attGroupStaffMapper.update(null, Wrappers.<AttGroupStaffPO>lambdaUpdate()
                                               .eq(AttGroupStaffPO::getStaffId, userUpdateMessage.getId())
                                               .set(AttGroupStaffPO::getName, userUpdateMessage.getName())
                                               .set(AttGroupStaffPO::getDeptId, userUpdateMessage.getOrganizationId())
                                               .set(StringUtils.isNotBlank(staffDeptName), AttGroupStaffPO::getDeptName, staffDeptName)
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
            log.error("消费ouaa修改人员,添加排班人员失败：", e);
        }

        StaffDetailDTO staffDetailDTO = staffClient.getByUserId(userUpdateMessage.getId());
        if (Objects.nonNull(staffDetailDTO)) {
            HolidayBalanceListPO holidayBalanceListPO = new HolidayBalanceListPO();
            holidayBalanceListPO.setUserId(staffDetailDTO.getId());
            holidayBalanceListPO.setEmployeeName(userUpdateMessage.getName());
            holidayBalanceListPO.setEmployeeOrganization(userUpdateMessage.getOrganizationId());
            balanceListMapper.updateBatchIn(Sets.newHashSet(staffDetailDTO.getId()), holidayBalanceListPO);
        }
    }
}
