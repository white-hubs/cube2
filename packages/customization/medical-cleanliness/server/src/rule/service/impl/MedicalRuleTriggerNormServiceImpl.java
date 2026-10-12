package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.dao.MedicalRuleTriggerNormMapper;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.entity.MedicalRuleTriggerNorm;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.param.RuleTriggerNormParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.service.MedicalRuleTriggerNormService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.vo.RuleTriggerNormVO;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MedicalRuleTriggerNormServiceImpl extends ServiceImpl<MedicalRuleTriggerNormMapper, MedicalRuleTriggerNorm> implements MedicalRuleTriggerNormService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(@Nonnull Long ruleTriggerId, @Nonnull List<RuleTriggerNormParam> params) {
        List<MedicalRuleTriggerNorm> medicalRuleTriggerNorms = params.stream()
                .map(ruleTriggerNormParam ->
                             MedicalRuleTriggerNorm.builder()
                                     .medicalRuleTriggerId(ruleTriggerId)
                                     .uploadTime(ruleTriggerNormParam.getUploadTime())
                                     .normValue(ruleTriggerNormParam.getNormValue())
                                     .upperLimit(ruleTriggerNormParam.getUpperLimit())
                                     .lowerLimit(ruleTriggerNormParam.getLowerLimit())
                                     .normCode(ruleTriggerNormParam.getNormCode())
                                     .normName(ruleTriggerNormParam.getNormName())
                                     .unit(ruleTriggerNormParam.getUnit())
                                     .build()
                    ).collect(Collectors.toList());
        this.saveBatch(medicalRuleTriggerNorms);
    }

    @Override
    public List<RuleTriggerNormVO> getNormsById(@Nonnull Long id) {
        List<MedicalRuleTriggerNorm> medicalRuleTriggerNorms = this.baseMapper.selectList(Wrappers.<MedicalRuleTriggerNorm>lambdaQuery()
                                                                                                  .eq(MedicalRuleTriggerNorm::getMedicalRuleTriggerId, id)
                                                                                         );
        return ObjectUtils.copying(medicalRuleTriggerNorms, RuleTriggerNormVO.class);
    }
}
