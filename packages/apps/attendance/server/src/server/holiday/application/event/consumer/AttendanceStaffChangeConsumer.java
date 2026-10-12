package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.event.consumer;

import com.google.common.collect.Sets;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.mapper.HolidayBalanceListMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.rocketmq.message.AttendanceGroupStaffMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = "attendanceStaffChangeConsumer",
        topic = AttendanceGroupStaffMessage.TOPIC,
        selectorType = SelectorType.TAG
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttendanceStaffChangeConsumer implements RocketMQListener<AttendanceGroupStaffMessage> {

    private final HolidayBalanceListMapper holidayBalanceListMapper;

    /**
     * @param attendanceGroupId 考勤组id
     * @param newlyUserIds      新加的人员
     * @param removedUserIds    移除的人员
     */
    public void changeAttendanceGroupId(Long attendanceGroupId, Set<Long> newlyUserIds, Set<Long> removedUserIds) {
        // 新加人员 如果之前存在就直接更新,不存在的不管
        if (CollectionUtils.isNotEmpty(newlyUserIds)) {
            holidayBalanceListMapper.updateAttendanceGroupId(newlyUserIds, attendanceGroupId);
        }

        // 移除的人员 如果之前存在就置为空，不存在不管
        if (CollectionUtils.isNotEmpty(removedUserIds)) {
            holidayBalanceListMapper.updateAttendanceGroupId(removedUserIds, null);
        }
    }

    @Override
    public void onMessage(AttendanceGroupStaffMessage attendanceGroupStaffMessage) {
        changeAttendanceGroupId(
                attendanceGroupStaffMessage.getGroupId(),
                Sets.newHashSet(attendanceGroupStaffMessage.getAddStaffIds()),
                Sets.newHashSet(attendanceGroupStaffMessage.getDelStaffIds()));
    }
}
