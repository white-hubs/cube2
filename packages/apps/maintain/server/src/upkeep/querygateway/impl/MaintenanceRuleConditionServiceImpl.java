package com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.dao.MaintenanceRuleConditionMapper;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenanceRuleCondition;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.MaintenanceRuleConditionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MaintenanceRuleConditionServiceImpl extends ServiceImpl<MaintenanceRuleConditionMapper, MaintenanceRuleCondition> implements MaintenanceRuleConditionService {

    @Override
    public List<MaintenanceRuleCondition> listByProductId(Long productId) {
        return this.lambdaQuery()
                .eq(MaintenanceRuleCondition::getDeviceProdId, productId)
                .list();
    }
}
