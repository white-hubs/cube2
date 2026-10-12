package com.guhecloud.cube.biz.mdata.server.job;

import com.guhecloud.cube.biz.mdata.server.bizlog.service.BizLogRecordService;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BizLogRedisClear {
    private final BizLogRecordService bizLogRecordService;

    /**
     * 每次服务重启的时候，都会向redis写入服务本身具有的业务日志信息；会造成重复建问题
     * 为了避免键值中的内容越来越大，每日进行定时清理
     */
    @JobAnnotation(name = "bizLogRedisClear", cron = "0 0 1 * * ?",
                   desc = "日志-业务日志-定时清理"
    )
    public void bizLogRedisClear() {
        bizLogRecordService.clearBizLogInRedis();
    }
}
