package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionPlanTaskConfig;

import java.util.List;

public interface DisinfectionPlanTaskConfigService extends IService<DisinfectionPlanTaskConfig> {
    boolean removePlanTaskByPlanId(long planId);

    List<DisinfectionPlanTaskConfig> getPlanTaskByPlanId(long planId);

    List<DisinfectionPlanTaskConfig> getPlanTaskByPlanIds(List<Long> planIds);
}