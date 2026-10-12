package com.guhecloud.cube.srv.gen.attendance.management.server.attend.machine.event;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.application.commandgateway.AttStaffClockApplication;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param.ClockInMachineParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.machine.infrastructure.mapper.AttDumuMessageMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.machine.infrastructure.po.AttDumuMessagePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.machine.message.DuMuMachineClockInMessage;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: lijie
 * @CreateTime: 2023-11-24  17:26
 * @Description: 度目考勤机数据消费
 * @Version: 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = DuMuMachineClockInMessage.TOPIC + "_CLOCK",
                         topic = DuMuMachineClockInMessage.TOPIC, selectorType = SelectorType.TAG
)
public class DuMuMachineClockInEvent implements RocketMQListener<DuMuMachineClockInMessage> {
    private final AttStaffClockApplication attStaffClockService;
    private final AttDumuMessageMapper dumuMessageMapper;
    private final UserClient userClient;

    @Override
    public void onMessage(DuMuMachineClockInMessage message) {
        log.info("考勤机打卡开始消费：{}", JsonUtils.serialize(message));
        UserDTO user = userClient.getUser(Long.parseLong(message.getClockInfo().getUser_id()));
        if (ObjectUtils.isEmpty(user.getTenants())) {
            log.error("未获取到租户信息");
        }
        SessionContextHolder.setRequester(Requester.builder().tenant(user.getTenants().getFirst().getTenant()).build());
        SecurityContextHolder.setPrincipal(Principal.builder()
                                                   .id(user.getId())
                                                   .name(user.getName())
                                                   .type(Principal.Type.USER)
                                                   .build());
        AttDumuMessagePO dumuMessage = dumuMessageMapper.selectOne(new LambdaQueryWrapper<AttDumuMessagePO>()
                                                                           .eq(AttDumuMessagePO::getMsgId, message.getClockInfo()
                                                                                   .getMsg_id())
                                                                           .last("limit 1"));
        if (ObjectUtils.isEmpty(dumuMessage)) {
            ClockInMachineParam machineParam = new ClockInMachineParam();
            machineParam.setUserId(user.getId());
            machineParam.setClockInTime(DateUtils.parse(Long.parseLong(message.getClockInfo().getTimestamp())));
            Boolean bool = attStaffClockService.machineClockIn(machineParam);
            log.info("打卡结果：{}", bool);
            if (bool) {
                bool = dumuMessageMapper.insert(new AttDumuMessagePO(message.getClockInfo().getMsg_id())) > 0;
                log.info("{}:消费结果：{}", message.getClockInfo().getMsg_id(), bool);
            }
        }
        log.info("考勤机打卡消费结束");
    }
}
