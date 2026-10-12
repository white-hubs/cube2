package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.dao.DisinfectionPlanTaskConfigMapper;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionPlanTaskConfig;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionPlanTaskConfigService;
import com.guhecloud.platform.base.util.ListUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class DisinfectionPlanTaskConfigServiceImpl extends ServiceImpl<DisinfectionPlanTaskConfigMapper, DisinfectionPlanTaskConfig>
        implements DisinfectionPlanTaskConfigService {
    @Override
    public boolean removePlanTaskByPlanId(long planId) {
        return remove(new LambdaQueryWrapper<DisinfectionPlanTaskConfig>().eq(DisinfectionPlanTaskConfig::getPlanId, planId));
    }

    @Override
    public List<DisinfectionPlanTaskConfig> getPlanTaskByPlanId(long planId) {
        return lambdaQuery().eq(DisinfectionPlanTaskConfig::getPlanId, planId).list();
    }

    @Override
    public List<DisinfectionPlanTaskConfig> getPlanTaskByPlanIds(List<Long> planIds) {
        if (ListUtils.isEmpty(planIds)) {
            return Collections.emptyList();
        }
        return lambdaQuery().in(DisinfectionPlanTaskConfig::getPlanId, planIds).list();
    }
}