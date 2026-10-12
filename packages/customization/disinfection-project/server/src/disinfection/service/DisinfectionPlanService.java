package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionPlan;

import java.util.List;
import java.util.Map;

public interface DisinfectionPlanService extends IService<DisinfectionPlan> {
    /**
     * 项目下是否有计划
     *
     * @param projectId 项目id
     *
     * @return true:有计划
     */
    boolean hasPlan(long projectId);

    /**
     * 获取计划名称
     *
     * @param planIds 计划id
     *
     * @return 计划名称
     */
    Map<Long, String> getPlanNameMap(List<Long> planIds);

    /**
     * 查询计划所有关联的租户
     */
    List<Integer> listAllTenantCodes();

    DisinfectionPlan getPlanByName(String planName);
}