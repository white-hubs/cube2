package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicecondition.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicecondition.dao.RuleDeviceConditionMapper;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicecondition.entity.RuleDeviceCondition;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicecondition.param.RuleDeviceConditionQueryParam;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicecondition.service.RuleDeviceConditionService;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicecondition.vo.RuleDeviceConditionPageVO;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RuleDeviceConditionServiceImpl extends ServiceImpl<RuleDeviceConditionMapper, RuleDeviceCondition> implements RuleDeviceConditionService {

    @Override
    public Page<RuleDeviceConditionPageVO> page(RuleDeviceConditionQueryParam param) {
        return this.baseMapper.page(param, PagingUtils.convert(param));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByRuleId(@NonNull Long ruleId) {
        List<RuleDeviceCondition> ruleDeviceConditions = this
                .list(Wrappers.<RuleDeviceCondition>lambdaQuery().eq(RuleDeviceCondition::getRuleId, ruleId));
        if (!CollectionUtils.isEmpty(ruleDeviceConditions)) {
            DatabaseContextHolder.softDelete(this, ruleDeviceConditions);
        }
    }
}
