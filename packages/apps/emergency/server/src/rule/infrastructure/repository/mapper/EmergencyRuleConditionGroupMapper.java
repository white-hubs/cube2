package com.guhecloud.cube.biz.emergency.server.rule.infrastructure.repository.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRuleConditionGroupPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmergencyRuleConditionGroupMapper extends BaseMapper<EmergencyRuleConditionGroupPO> {

    /**
     * 忽略租户插件的查询
     *
     * @param ruleId
     *
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    @Select("select * from emergency_rule_condition_group where id=#{ruleId}")
    EmergencyRuleConditionGroupPO selectOneByIgnoreTenant(@Param("ruleId") Long ruleId);
}
