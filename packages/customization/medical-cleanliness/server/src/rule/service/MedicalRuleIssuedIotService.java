package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.entity.MedicalRuleIssuedIot;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.enumeration.RuleIssuedIotStatusEnum;

import java.time.LocalDateTime;

public interface MedicalRuleIssuedIotService extends IService<MedicalRuleIssuedIot> {

    void updateIssuedStatus(Long id, RuleIssuedIotStatusEnum status, LocalDateTime issuedTime);
}
