package com.guhecloud.cube.biz.subsystem.broadcast.server.rule.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.subsystem.broadcast.server.rule.dao.BroadcastPlayRuleDeviceMapper;
import com.guhecloud.cube.biz.subsystem.broadcast.server.rule.po.BroadcastPlayRuleDevicePO;
import com.guhecloud.cube.biz.subsystem.broadcast.server.rule.service.BroadcastPlayRuleDeviceService;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BroadcastPlayRuleDeviceServiceImpl extends ServiceImpl<BroadcastPlayRuleDeviceMapper, BroadcastPlayRuleDevicePO> implements BroadcastPlayRuleDeviceService {

    @Override
    public List<BroadcastPlayRuleDevicePO> getListByRuleId(@NonNull Long ruleId) {
        return this.baseMapper.selectList(Wrappers.<BroadcastPlayRuleDevicePO>lambdaQuery()
                                                  .eq(BroadcastPlayRuleDevicePO::getRuleId, ruleId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByRuleId(@NonNull Long ruleId) {
        List<BroadcastPlayRuleDevicePO> broadcastPlayRuleDevicePOS = this.getListByRuleId(ruleId);
        if (!CollectionUtils.isEmpty(broadcastPlayRuleDevicePOS)) {
            DatabaseContextHolder.softDelete(this, broadcastPlayRuleDevicePOS);
        }
    }
}
