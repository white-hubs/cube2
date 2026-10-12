package com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.job;

import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.service.impl.MedicalOverrunRecordServiceImpl;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @Author: lijie
 * @CreateTime: 2024-09-09  18:51
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MedicalOverrunAlarmJob {
    private final MedicalOverrunRecordServiceImpl medicalOverrunAlarmService;

    /**
     * 历史数据采集
     */
    @JobAnnotation(name = "medicalCollectHistoricalData", cron = "0 0 0 * * ?", desc = "洁净医疗-历史数据采集")
    public void medicalCollectHistoricalData(String param) {
        SessionContextHolder.clearSessionContext();
        var date = DateUtils.offsetDay(LocalDateTime.now(), -1);
        if (ObjectUtils.isNotEmpty(param)) {
            date = DateUtils.parse(param, "yyyy-MM-dd HH:mm:ss");
        }
        medicalOverrunAlarmService.medicalOverrunAlarm(date);
        SessionContextHolder.clearSessionContext();
    }

    /**
     * 实时数据采集
     */
    @JobAnnotation(name = "medicalCollectRealTimeData", cron = "0 0/10 * * * ?", desc = "洁净医疗-实时数据采集")
    public void medicalCollectRealTimeData(String param) {
        SessionContextHolder.clearSessionContext();
        medicalOverrunAlarmService.medicalCollectRealTimeData();
        SessionContextHolder.clearSessionContext();
    }
}
