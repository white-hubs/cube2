package com.guhecloud.platform.cloud.logger;

import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.cloud.logger.message.BizLogRecordDTO;
import com.guhecloud.platform.cloud.logger.message.BizLogRecordMessage;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import com.mzt.logapi.beans.LogRecord;
import com.mzt.logapi.service.ILogRecordService;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BizLogCollectorServiceImpl implements ILogRecordService {
    @Override
    public void record(LogRecord logRecord) {
        log.info("业务日志：{}", JsonUtils.serialize(logRecord));
        try {
            BizLogRecordDTO log = ObjectUtils.copying(logRecord, BizLogRecordDTO.class);
            if (StringUtils.isNotEmpty(logRecord.getOperator())) {
                String[] userInfo = logRecord.getOperator().split("_");
                log.setAccount(userInfo[0]);
                log.setUserName(userInfo[1]);
            }
            RocketMQContextHolder.send(BizLogRecordMessage.TOPIC + ":" + logRecord.getType(), new BizLogRecordMessage(log));
        } catch (Exception e) {
            log.error("记录业务日志异常，数据为：{},异常为：{}", logRecord, e);
        }
    }

    @Override
    public List<LogRecord> queryLog(String bizNo, String type) {
        return null;
    }

    @Override
    public List<LogRecord> queryLogByBizNo(String bizNo, String type, String subType) {
        return null;
    }
}
