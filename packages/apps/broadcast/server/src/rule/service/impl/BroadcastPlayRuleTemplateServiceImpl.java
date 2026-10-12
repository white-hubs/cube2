package com.guhecloud.cube.biz.subsystem.broadcast.server.rule.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.subsystem.broadcast.server.rule.dao.BroadcastPlayRuleTemplateMapper;
import com.guhecloud.cube.biz.subsystem.broadcast.server.rule.po.BroadcastPlayRuleTemplatePO;
import com.guhecloud.cube.biz.subsystem.broadcast.server.rule.service.BroadcastPlayRuleTemplateService;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BroadcastPlayRuleTemplateServiceImpl extends ServiceImpl<BroadcastPlayRuleTemplateMapper, BroadcastPlayRuleTemplatePO> implements BroadcastPlayRuleTemplateService {

    @Override
    public List<BroadcastPlayRuleTemplatePO> getListByRuleId(@NonNull Long ruleId) {
        return this.baseMapper.selectList(Wrappers.<BroadcastPlayRuleTemplatePO>lambdaQuery()
                                                  .eq(BroadcastPlayRuleTemplatePO::getRuleId, ruleId)
                                         );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByRuleId(@NonNull Long ruleId) {
        List<BroadcastPlayRuleTemplatePO> broadcastPlayRuleTemplatePOS = this.getListByRuleId(ruleId);
        if (!CollectionUtils.isEmpty(broadcastPlayRuleTemplatePOS)) {
            DatabaseContextHolder.softDelete(this, broadcastPlayRuleTemplatePOS);
        }
    }
}
