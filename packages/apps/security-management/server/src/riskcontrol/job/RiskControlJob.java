package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.job;

import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.workflow.RiskPlanWarningWorkflowService;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component("securityManagementRiskControlJob")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RiskControlJob {
    private final RiskPlanWarningWorkflowService workflowService;

    @JobAnnotation(name = "securityManagementRiskIdentifyCycleJob", cron = "0 0 1 * * ?", desc = "风险辨识周期扫描")
    public void executeIdentifyCycleJob(String param) {
        workflowService.executeIdentifyCycleJob();
    }

    @JobAnnotation(name = "securityManagementRiskAssessmentPlanJob", cron = "0 10 1 * * ?", desc = "风险评估计划扫描")
    public void executeAssessmentPlanJob(String param) {
        workflowService.executeAssessmentPlanJob();
    }
}
