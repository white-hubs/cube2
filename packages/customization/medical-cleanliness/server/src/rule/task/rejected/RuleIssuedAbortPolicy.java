package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.task.rejected;

import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.enumeration.RuleIssuedIotStatusEnum;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.service.MedicalRuleIssuedIotService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.task.RuleIssuedTask;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.DateUtils;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class RuleIssuedAbortPolicy implements RejectedExecutionHandler {

    public RuleIssuedAbortPolicy() {}

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        MedicalRuleIssuedIotService medicalRuleIssuedIotService = ApplicationContextHolder.getBean(MedicalRuleIssuedIotService.class);
        //标记下发失败
        RuleIssuedTask ruleIssuedTask = (RuleIssuedTask) r;
        var date = DateUtils.parse(ruleIssuedTask.getRuleIssuedIotDTO().getTimestamp());
        medicalRuleIssuedIotService.updateIssuedStatus(ruleIssuedTask.getRuleIssuedIotDTO().getId(),
                                                       RuleIssuedIotStatusEnum.ISSUED_FAIL,
                                                       date);
        throw new RejectedExecutionException("Task " + r +
                                             " rejected from " +
                                             e.toString());
    }
}
