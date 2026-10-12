package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.task;

import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.entity.MedicalRuleNotifyConfig;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.enumeration.RuleIssuedIotStatusEnum;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.facade.iot.service.MedicalIotRuleService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.service.MedicalRuleIssuedIotService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.service.MedicalRuleNotifyConfigService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.service.MedicalRuleService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.task.dto.RuleIssuedIotDTO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.vo.RuleVO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class RuleIssuedTask implements Runnable {

    private final RuleIssuedIotDTO ruleIssuedIotDTO;

    public RuleIssuedTask(RuleIssuedIotDTO ruleIssuedIotDTO) {
        this.ruleIssuedIotDTO = ruleIssuedIotDTO;
    }

    @Override
    public void run() {
        SessionContextHolder.requestTenant(ruleIssuedIotDTO.getTenant());
        RuleIssuedIotStatusEnum ruleIssuedIotStatusEnum = RuleIssuedIotStatusEnum.ISSUED_SUCCESS;
        try {
            MedicalRuleService medicalRuleService = ApplicationContextHolder.getBean(MedicalRuleService.class);
            MedicalRuleNotifyConfigService medicalRuleNotifyConfigService = ApplicationContextHolder.getBean(MedicalRuleNotifyConfigService.class);
            MedicalIotRuleService medicalIotRuleService = ApplicationContextHolder.getBean(MedicalIotRuleService.class);
            MedicalRuleNotifyConfig medicalRuleNotifyConfig = medicalRuleNotifyConfigService.getMedicalRuleNotifyConfig();
            RuleVO ruleVO = medicalRuleService.queryById(ruleIssuedIotDTO.getId());
            medicalIotRuleService.addIotRule(ruleVO, medicalRuleNotifyConfig.getTimes() * 60);
        } catch (Exception e) {
            ruleIssuedIotStatusEnum = RuleIssuedIotStatusEnum.ISSUED_FAIL;
            log.error("下发规则到iot平台失败,失败原因:{}", e.getMessage(), e);
        } finally {
            MedicalRuleIssuedIotService medicalRuleIssuedIotService = ApplicationContextHolder.getBean(MedicalRuleIssuedIotService.class);
            //标记下发失败
            var date = DateUtils.parse(ruleIssuedIotDTO.getTimestamp());
            medicalRuleIssuedIotService.updateIssuedStatus(ruleIssuedIotDTO.getId(),
                                                           ruleIssuedIotStatusEnum,
                                                           date);
        }
    }
}
