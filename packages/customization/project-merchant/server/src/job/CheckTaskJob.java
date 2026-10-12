package com.guhecloud.cube.deliver.project.merchant.job;

import com.guhecloud.cube.deliver.project.merchant.commons.utils.CronUtil;
import com.guhecloud.cube.deliver.project.merchant.commons.utils.DateUtils;
import com.guhecloud.cube.deliver.project.merchant.enums.WholesalerEnum;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.ObjectIdUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.job.jobtask.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CheckTaskJob {

    private final JobService jobService;

    public String createTask(String businessType, String businessKey, LocalDateTime beginTime, LocalDateTime endTime) {
        String jobDes1 = "";
        String jobDes2 = "";
        String jobHandler1 = "";
        String jobHandler2 = "";
        Object jobBean = ApplicationContextHolder.getBean(MerchantJobHandler.class);
        String jobMethod1 = "";
        String jobMethod2 = "";
        if (WholesalerEnum.BUSINESS_TYPE_PROTOCOL.getCode().equals(businessType)) {
            jobDes1 = "商户协议签订任务生效job";
            jobDes2 = "商户协议签订任务失效job";
            jobHandler1 = "generateWholesalerProtocolValid";
            jobHandler2 = "generateWholesalerProtocolNotValid";
            jobMethod1  = "generateWholesalerProtocolValid";
            jobMethod2 = "generateWholesalerProtocolNotValid";

        } else if (WholesalerEnum.BUSINESS_TYPE_QUESTIONNAIRE.getCode().equals(businessType)) {
            jobDes1 = "商户调查问卷生效job";
            jobDes2 = "商户调查问卷失效job";
            jobHandler1 = "generateQuestionnaireValid";
            jobHandler2 = "generateQuestionnaireNotValid";
            jobMethod1  = "generateQuestionnaireValid";
            jobMethod2 = "generateQuestionnaireNotValid";
        }
        String taskId1 = this.createJob(businessKey, beginTime, jobDes1, jobHandler1,jobBean,jobMethod1);
        String taskId2 = this.createJob(businessKey, endTime, jobDes2, jobHandler2,jobBean,jobMethod2);
        return taskId1 + "," + taskId2;
    }

    public void removeTask(String xxlTaskIds) {
        if (StringUtils.isEmpty(xxlTaskIds)) return;
        String[] split = xxlTaskIds.split(",");
        jobService.removeJob(split[0]);
        jobService.removeJob(split[1]);
    }

    private String createJob(String businessKey, LocalDateTime time, String jobDes, String jobHandler,Object bean,String jobMethod) {
        // 返回一个永远不可能存在的任务ID
        if (time == null) return "999999";
        String cronExpression = CronUtil.createCronExpression(DateUtils.TIMERANGE_MONTH, String.valueOf(time.getDayOfMonth()),  DateUtils.getDate(time, "HH:mm:ss"));
        String jobName = String.format("%s_%s",ObjectIdUtils.next(),jobHandler);
        jobService.addJob(jobName,bean,jobMethod,cronExpression,jobDes,null,true);
        return jobName;
    }
}
