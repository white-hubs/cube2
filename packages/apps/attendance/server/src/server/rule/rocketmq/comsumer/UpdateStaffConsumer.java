package com.guhecloud.cube.srv.gen.attendance.management.server.rule.rocketmq.comsumer;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.commandgateway.ArrangeWorkCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.commandgateway.DutyWorkCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.common.commandgateway.WorkCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po.ArrangeWorkPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po.DutyWorkPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.commandgateway.EmployeeHolidayCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.AttendanceGroupStaffMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.AttendanceGroupTypeEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.AttendanceGroupStaffPO;
import com.guhecloud.cube.srv.gen.personnel.management.client.message.UpdateStaffMessage;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;

/**
 * 修改员工
 */

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = UpdateStaffMessage.TOPIC + "_" + "ATTENDANCE_GROUP", topic = UpdateStaffMessage.TOPIC,
                         selectorType = SelectorType.TAG
)
public class UpdateStaffConsumer implements RocketMQListener<UpdateStaffMessage> {

    private final ArrangeWorkCommandService arrangeWorkCommandService;
    private final DutyWorkCommandService dutyWorkCommandService;
    private final AttendanceGroupStaffMapper attendanceGroupStaffMapper;
    private final WorkCommandService workCommandService;
    private final EmployeeHolidayCommandService employeeHolidayCommandService;

    @Override
    public void onMessage(UpdateStaffMessage message) {
        log.info("收到修改员工消息：{}", JSONObject.toJSONString(message));
        try {
            // 是离职还是更新信息
            if (message.isLeaveOffice()) {
                // 如果是离职，删除所在考勤组，删除后续排班
                attendanceGroupStaffMapper.delete(Wrappers.<AttendanceGroupStaffPO>lambdaQuery()
                                                          .eq(AttendanceGroupStaffPO::getStaffId, message.getStaffId()));
                workCommandService.removeWorkData(Collections.singletonList(message.getStaffId()), AttendanceGroupTypeEnum.ARRANGE.name());
                workCommandService.removeWorkData(Collections.singletonList(message.getStaffId()), AttendanceGroupTypeEnum.DUTY.name());
            } else {
                // 更新假期余额中的员工信息
                employeeHolidayCommandService.updateHolidayBalanceUserInfo(message.getStaffId());

                // 更新考勤组员工信息
                new LambdaUpdateChainWrapper<>(attendanceGroupStaffMapper)
                        .eq(AttendanceGroupStaffPO::getStaffId, message.getStaffId())
                        .set(Objects.nonNull(message.getOrganizationId()), AttendanceGroupStaffPO::getStaffOrganizationId, message.getOrganizationId())
                        .set(StringUtils.isNotEmpty(message.getName()), AttendanceGroupStaffPO::getStaffName, message.getName())
                        .set(StringUtils.isNotEmpty(message.getMobile()), AttendanceGroupStaffPO::getStaffMobile, message.getMobile())
                        .update();
                // 更新今日之后的排班和值班数据中员工信息
                arrangeWorkCommandService.lambdaUpdate().eq(ArrangeWorkPO::getStaffId, message.getStaffId())
                        .gt(ArrangeWorkPO::getDate, DateUtils.parseDate(DateUtils.formatDate(LocalDateTime.now())))
                        .set(Objects.nonNull(message.getOrganizationId()), ArrangeWorkPO::getStaffOrganizationId, message.getOrganizationId())
                        .set(StringUtils.isNotEmpty(message.getName()), ArrangeWorkPO::getStaffName, message.getName())
                        .set(StringUtils.isNotEmpty(message.getMobile()), ArrangeWorkPO::getStaffMobile, message.getMobile())
                        .update();
                dutyWorkCommandService.lambdaUpdate().eq(DutyWorkPO::getStaffId, message.getStaffId())
                        .gt(DutyWorkPO::getDate, DateUtils.parseDate(DateUtils.formatDate(LocalDateTime.now())))
                        .set(StringUtils.isNotEmpty(message.getName()), DutyWorkPO::getStaffName, message.getName())
                        .set(Objects.nonNull(message.getOrganizationId()), DutyWorkPO::getStaffOrganizationId, message.getOrganizationId())
                        .set(StringUtils.isNotEmpty(message.getMobile()), DutyWorkPO::getStaffMobile, message.getMobile())
                        .update();
            }
        } catch (Exception e) {
            log.error("收到修改员工消息，消费报错：{}", JsonUtils.serialize(message), e);
        }
    }

}
