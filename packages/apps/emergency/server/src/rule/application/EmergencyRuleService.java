package com.guhecloud.cube.biz.emergency.server.rule.application;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRulePO;
import com.guhecloud.cube.biz.emergency.server.rule.infrastructure.repository.mapper.EmergencyRuleMapper;
import org.springframework.stereotype.Service;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class EmergencyRuleService extends ServiceImpl<EmergencyRuleMapper, EmergencyRulePO> {

    @Override
    public List<EmergencyRulePO> listByIds(Collection<? extends Serializable> idList) {
        return CollectionUtils.isEmpty(idList) ? Collections.emptyList() : this.lambdaQuery().in(EmergencyRulePO::getId, idList).list();
    }
}
