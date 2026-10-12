package com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenanceRuleCondition;

import java.util.List;

public interface MaintenanceRuleConditionService extends IService<MaintenanceRuleCondition> {

    List<MaintenanceRuleCondition> listByProductId(Long productId);
}
