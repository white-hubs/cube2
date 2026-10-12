package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.event.consumer;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.mapper.HolidayBalanceListMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.po.HolidayBalanceListPO;
import com.guhecloud.cube.srv.gen.personnel.management.client.message.DelStaffMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component("HolidayDelStaffConsumer")
@RocketMQMessageListener(
        consumerGroup = "HolidayDelStaffConsumer",
        topic = DelStaffMessage.TOPIC,
        selectorType = SelectorType.TAG
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DelStaffConsumer implements RocketMQListener<DelStaffMessage> {

    private final HolidayBalanceListMapper holidayBalanceListMapper;

    @Override
    public void onMessage(DelStaffMessage delStaffMessage) {
        log.info("假期模块收到删除员工【 {} 】消息", delStaffMessage.getStaffId());
        holidayBalanceListMapper.delete(
                Wrappers.<HolidayBalanceListPO>lambdaQuery()
                        .eq(HolidayBalanceListPO::getUserId, delStaffMessage.getStaffId())
                                       );
    }
}
