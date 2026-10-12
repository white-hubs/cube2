package com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.rocketmq;

import com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.commandgateway.AttendanceLogService;
import com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.enumeration.BusinessTypeEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.po.AttendanceOperationLog;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.cloud.logger.message.BizLogRecordDTO;
import com.guhecloud.platform.cloud.logger.message.BizLogRecordMessage;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wql
 * @describe 操作日志消息
 * @date 2023/8/1 11:44
 */
@Slf4j
@Component("attendance_operation_log")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = BizLogRecordMessage.TOPIC + "_" + "ATTENDANCE", topic = BizLogRecordMessage.TOPIC,
                         selectorExpression = "ATTENDANCE"
)
public class OperationLogMessageConsumer implements RocketMQListener<BizLogRecordMessage> {

    @Resource
    private AttendanceLogService attendanceLogService;

    @Override
    public void onMessage(BizLogRecordMessage processCompletedMessage) {
        BizLogRecordDTO logRecord = processCompletedMessage.getBizLogRecord();
        log.info("操作日志消息:{}", JsonUtils.serialize(logRecord));
        insertAttendanceLog(logRecord);
    }

    /**
     * 保存考勤操作日志
     *
     * @param logRecord 操作信息
     */
    private void insertAttendanceLog(BizLogRecordDTO logRecord) {
        String action = logRecord.getAction();
        String replace = action.replace("；", "<br/>");
        Long id = SecurityContextHolder.getPrincipal().getId();
        attendanceLogService.insertRecord(AttendanceOperationLog.builder()
                                                  .objId(Long.getLong(logRecord.getBizNo()))
                                                  .businessType(BusinessTypeEnum.ATTENDANCE_MANAGER.name())
                                                  .operationObj(logRecord.getSubType())
                                                  .userId(id)
                                                  .userName(logRecord.getUserName())
                                                  .operationType(logRecord.getOperationType())
                                                  .operationContent(replace)
                                                  .build());
    }
}
