package com.guhecloud.platform.job.xxl.dto;

import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.job.jobtask.dto.JobAddInfo;
import lombok.Data;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
public class XxlJobInfo {
    private Integer id;
    private String jobDesc;
    private String author;
    private String alarmEmail;
    private String scheduleType = "CRON"; // 固定
    private String scheduleConf; // cron 表达式
    private String misfireStrategy = "DO_NOTHING";
    private String executorRouteStrategy = "FIRST";
    private String executorHandler; // 任务名(jobHandler)
    private String executorParam;
    private String executorBlockStrategy = "SERIAL_EXECUTION";
    private Integer executorTimeout = 0;
    private Integer executorFailRetryCount = 0;
    private String glueType = "BEAN"; // 使用 Spring Bean
    private String glueSource;
    private String glueRemark;
    private Integer childJobId;
    private Integer triggerStatus; // 0-停止，1-运行
    private Long triggerLastTime;
    private Long triggerNextTime;
    private Integer jobGroup;

    public MultiValueMap<String, Object> toForm() {
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        if (Objects.nonNull(id)) {
            form.add("id", id);
        }
        form.add("jobGroup", jobGroup);
        form.add("jobDesc", jobDesc);
        form.add("author", author);
        form.add("alarmEmail", alarmEmail);
        form.add("scheduleType", scheduleType);
        form.add("scheduleConf", scheduleConf);
        form.add("cronGen_display", scheduleConf);
        form.add("schedule_conf_CRON", null);
        form.add("schedule_conf_FIX_RATE", null);
        form.add("schedule_conf_FIX_DELAY", null);
        form.add("glueType", glueType == null ? "BEAN" : glueType);
        form.add("executorHandler", executorHandler);
        form.add("executorParam", executorParam);
        form.add("executorRouteStrategy", executorRouteStrategy == null ? "FIRST" : executorRouteStrategy);
        form.add("childJobId", null);
        form.add("misfireStrategy", misfireStrategy == null ? "DO_NOTHING" : misfireStrategy);
        form.add("executorBlockStrategy", executorBlockStrategy == null ? "SERIAL_EXECUTION" : executorBlockStrategy);
        form.add("executorTimeout", executorTimeout == null ? 0 : executorTimeout);
        form.add("executorFailRetryCount", executorFailRetryCount == null ? 0 : executorFailRetryCount);
        form.add("glueRemark", glueSource == null ? "GLUE代码初始化" : glueSource);
        form.add("glueSource", null);
        return form;
    }

    public static XxlJobInfo registrationToXxlJobInfo(JobAddInfo registration) {
        XxlJobInfo xxlJobInfoDTO = new XxlJobInfo();
        xxlJobInfoDTO.setExecutorHandler(registration.getName());
        xxlJobInfoDTO.setJobDesc(registration.getDesc());
        xxlJobInfoDTO.setAuthor("XXL");
        SimpleDateFormat sdf = new SimpleDateFormat("ss mm HH dd MM ? yyyy");
        String cron = StringUtils.isNotEmpty(registration.getCron()) ? registration.getCron() : sdf.format(LocalDateTime.now());
        xxlJobInfoDTO.setExecutorRouteStrategy("ROUND");
        xxlJobInfoDTO.setGlueType("BEAN");
        xxlJobInfoDTO.setExecutorBlockStrategy("SERIAL_EXECUTION");
        xxlJobInfoDTO.setExecutorTimeout(0);
        xxlJobInfoDTO.setExecutorFailRetryCount(0);
        xxlJobInfoDTO.setExecutorParam(registration.getParam());

        xxlJobInfoDTO.setScheduleType("CRON");
        xxlJobInfoDTO.setScheduleConf(cron);
        xxlJobInfoDTO.setMisfireStrategy("DO_NOTHING");
        xxlJobInfoDTO.setMisfireStrategy("DO_NOTHING");
        return xxlJobInfoDTO;
    }
}
