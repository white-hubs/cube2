package com.guhecloud.cube.srv.gen.attendance.management.server.rule.rocketmq.comsumer;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.common.commandgateway.WorkCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.machine.application.dumu.UserManageApplication;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.AttendanceGroupStaffMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.AttendanceGroupTypeEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.AttendanceGroupStaffPO;
import com.guhecloud.cube.srv.gen.personnel.management.client.message.DelStaffMessage;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 删除员工
 */

@Slf4j
@Component("attendance_del_staff")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = DelStaffMessage.TOPIC + "_" + "ATTENDANCEGROUP", topic = DelStaffMessage.TOPIC,
                         selectorType = SelectorType.TAG
)
public class DelStaffConsumer implements RocketMQListener<DelStaffMessage> {

    private final WorkCommandService workCommandService;
    private final AttendanceGroupStaffMapper attendanceGroupStaffMapper;
    private final UserManageApplication userManageApplication;

    @Override
    public void onMessage(DelStaffMessage message) {
        log.info("收到删除员工消息：{}", JSONObject.toJSONString(message));
        // 删除员工时，如果员工存在考勤组，则删除
        try {
            List<Long> userIds = new ArrayList<>();
            userIds.add(message.getStaffId());
            attendanceGroupStaffMapper.delete(Wrappers.<AttendanceGroupStaffPO>lambdaQuery()
                                                      .eq(AttendanceGroupStaffPO::getStaffId, message.getStaffId()));
            workCommandService.removeWorkData(userIds, AttendanceGroupTypeEnum.ARRANGE.name());
            workCommandService.removeWorkData(userIds, AttendanceGroupTypeEnum.DUTY.name());
        } catch (Exception e) {
            log.error("收到删除员工消息后，消费报错：{}", JsonUtils.serialize(message), e);
        }
        userManageApplication.deleteUser(message);
    }

}
