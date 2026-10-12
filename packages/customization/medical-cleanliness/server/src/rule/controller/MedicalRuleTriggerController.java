package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.controller;

import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.service.MedicalRuleTriggerService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.vo.RuleTriggerVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MedicalRuleTriggerController {

    private final MedicalRuleTriggerService medicalRuleTriggerService;

    /**
     * 规则触发记录详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/medical-rule/trigger/{id}")
    public RuleTriggerVO getRuleTrigger(@PathVariable("id") Long id, Long messageId) {
        return this.medicalRuleTriggerService.getDetail(id, messageId);
    }
}
