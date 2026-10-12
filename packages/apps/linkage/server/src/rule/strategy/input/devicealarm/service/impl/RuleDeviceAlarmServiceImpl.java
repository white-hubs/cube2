package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicealarm.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicealarm.dao.RuleDeviceAlarmMapper;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicealarm.entity.RuleDeviceAlarm;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicealarm.param.RuleDeviceAlarmQueryParam;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicealarm.service.RuleDeviceAlarmService;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicealarm.vo.RuleDeviceAlarmPageVO;
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
public class RuleDeviceAlarmServiceImpl extends ServiceImpl<RuleDeviceAlarmMapper, RuleDeviceAlarm> implements RuleDeviceAlarmService {

    @Override
    public Page<RuleDeviceAlarmPageVO> page(RuleDeviceAlarmQueryParam param) {
        return this.baseMapper.page(param, PagingUtils.convert(param));
    }

    @Override
    public List<RuleDeviceAlarm> listByRuleId(Long ruleId) {
        return this.lambdaQuery().eq(RuleDeviceAlarm::getRuleId, ruleId).list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByRuleId(@NonNull Long ruleId) {
        List<RuleDeviceAlarm> ruleDeviceAlarms = this.listByRuleId(ruleId);
        if (!CollectionUtils.isEmpty(ruleDeviceAlarms)) {
            DatabaseContextHolder.softDelete(this, ruleDeviceAlarms);
        }
    }
}
