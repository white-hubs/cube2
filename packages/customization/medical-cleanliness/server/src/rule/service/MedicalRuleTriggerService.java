package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.entity.MedicalRuleTrigger;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.param.RuleTriggerParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.vo.RuleTriggerVO;

public interface MedicalRuleTriggerService extends IService<MedicalRuleTrigger> {

    Long save(RuleTriggerParam param);

    RuleTriggerVO getDetail(Long id,Long messageId);
}
