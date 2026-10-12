package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.application.event;

import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.application.message.AttendanceClockInMessage;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository.AttClockInRecordsRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockInRecordsPO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: lijie
 * @CreateTime: 2023-10-09  15:41
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = AttendanceClockInMessage.TOPIC + "_CLOCK_IN_LOG",
                         topic = AttendanceClockInMessage.TOPIC
)
public class ClockInRecordsEvent implements RocketMQListener<AttendanceClockInMessage> {
    private final AttClockInRecordsRepository clockInRecordsRepository;

    @Override
    public void onMessage(AttendanceClockInMessage message) {
        log.info("打卡日志开始消费：{}", message.getClockInfo());
        AttClockInRecordsPO attClockInRecordsPO = message.getClockInfo();
        if (ObjectUtils.isEmpty(attClockInRecordsPO.getRequireClockInTime())) {
            attClockInRecordsPO.setRequireClockInTime(attClockInRecordsPO.getActualClockInTime());
        }
        Boolean bool = clockInRecordsRepository.addClockLog(attClockInRecordsPO);
        log.info("新增打卡日志结果:{}", bool);
    }
}
