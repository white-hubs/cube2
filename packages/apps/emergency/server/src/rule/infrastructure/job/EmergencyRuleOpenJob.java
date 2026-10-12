package com.guhecloud.cube.biz.emergency.server.rule.infrastructure.job;

import com.guhecloud.cube.biz.emergency.server.rule.application.EmergencyRuleDisableService;
import com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.EmergencyRuleCommandService;
import com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.command.ChangeEmergencyRuleEnabledCommand;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmergencyRuleOpenJob {

    private final EmergencyRuleDisableService emergencyRuleDisableService;

    private final EmergencyRuleCommandService emergencyRuleCommandService;

    public EmergencyRuleOpenJob(EmergencyRuleDisableService emergencyRuleDisableService,
                                EmergencyRuleCommandService emergencyRuleCommandService
                               ) {
        this.emergencyRuleDisableService = emergencyRuleDisableService;
        this.emergencyRuleCommandService = emergencyRuleCommandService;
    }

    @JobAnnotation(name = "emergencyOpenDisableEmergencyRule", cron = "0 * * * * ?", desc = "预案管理-预案规则-定时打开关闭的规则")
    public void openDisableEmergencyRule(String param) {
        SessionContextHolder.clearSessionContext();
        var now = DateUtils.now();
        var endTime = now.withSecond(59).withNano(0);
        var emergencyRuleDisableList = this.emergencyRuleDisableService.getEmergencyRuleDisableList(endTime);
        var emergencyRuleEnabled = new ChangeEmergencyRuleEnabledCommand();
        emergencyRuleEnabled.setEnabled(true);
        emergencyRuleDisableList.forEach(emergencyRuleDisablePO -> {
            SessionContextHolder.requestTenant(emergencyRuleDisablePO.getTenant());
            emergencyRuleEnabled.setId(emergencyRuleDisablePO.getRuleId());
            emergencyRuleCommandService.changeEmergencyRuleEnabled(emergencyRuleEnabled);
        });
    }
}
