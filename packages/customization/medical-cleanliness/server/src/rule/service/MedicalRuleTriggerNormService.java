package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.entity.MedicalRuleTriggerNorm;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.param.RuleTriggerNormParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.vo.RuleTriggerNormVO;

import java.util.List;

public interface MedicalRuleTriggerNormService extends IService<MedicalRuleTriggerNorm> {

    void saveBatch(Long ruleTriggerId, List<RuleTriggerNormParam> params);

    List<RuleTriggerNormVO> getNormsById(Long id);
}
