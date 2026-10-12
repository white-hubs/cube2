package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.dao.DisinfectionPlanMapper;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionPlan;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionPlanService;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class DisinfectionPlanServiceImpl extends ServiceImpl<DisinfectionPlanMapper, DisinfectionPlan>
        implements DisinfectionPlanService {
    @Override
    public boolean hasPlan(long projectId) {
        return lambdaQuery().eq(DisinfectionPlan::getProjectId, projectId).count() > 0;
    }

    @Override
    public Map<Long, String> getPlanNameMap(List<Long> planIds) {
        if (ListUtils.isEmpty(planIds)) {
            return Collections.emptyMap();
        }
        List<DisinfectionPlan> list = lambdaQuery()
                .select(DisinfectionPlan::getId, DisinfectionPlan::getPlanName)
                .in(DisinfectionPlan::getId, planIds)
                .list();

        return MapUtils.listToMap(list, DisinfectionPlan::getId, DisinfectionPlan::getPlanName);
    }

    @Override
    public List<Integer> listAllTenantCodes() {
        return this.getBaseMapper().listAllTenantCodes();
    }

    @Override
    public DisinfectionPlan getPlanByName(String planName) {
        if (StringUtils.isEmpty(planName)) {
            return null;
        }
        return lambdaQuery().eq(DisinfectionPlan::getPlanName, planName).one();
    }
}