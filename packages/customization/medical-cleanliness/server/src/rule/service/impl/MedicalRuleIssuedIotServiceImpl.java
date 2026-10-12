package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.dao.MedicalRuleIssuedIotMapper;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.entity.MedicalRuleIssuedIot;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.enumeration.RuleIssuedIotStatusEnum;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.service.MedicalRuleIssuedIotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
public class MedicalRuleIssuedIotServiceImpl extends ServiceImpl<MedicalRuleIssuedIotMapper, MedicalRuleIssuedIot> implements MedicalRuleIssuedIotService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateIssuedStatus(Long id, RuleIssuedIotStatusEnum status, LocalDateTime issuedTime) {
        LambdaUpdateWrapper<MedicalRuleIssuedIot> issuedIotLambdaUpdateWrapper = Wrappers.<MedicalRuleIssuedIot>lambdaUpdate()
                .eq(MedicalRuleIssuedIot::getId, id)
                .and(q -> q
                        .le(MedicalRuleIssuedIot::getLastIssuedTime, issuedTime)
                        .or()
                        .isNull(MedicalRuleIssuedIot::getLastIssuedTime))
                .set(MedicalRuleIssuedIot::getLastIssuedTime, issuedTime)
                .set(MedicalRuleIssuedIot::getIssuedStatus, status);
        this.update(issuedIotLambdaUpdateWrapper);
    }
}
