package com.guhecloud.cube.srv.gen.maintain.server.upkeep.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.command.MaintenancePlanRuleCommand;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenanceRuleCondition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-19  15:37
 * @Description:
 * @Version: 1.0
 */
@Mapper
public interface MaintenanceRuleConditionMapper extends BaseMapper<MaintenanceRuleCondition> {
    List<MaintenancePlanRuleCommand> selectAllByPlanId(@Param("planId") Long planId, @Param("conditionType") String conditionType);
}
