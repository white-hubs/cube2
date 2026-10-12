package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicecondition.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicecondition.entity.RuleDeviceCondition;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicecondition.param.RuleDeviceConditionQueryParam;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicecondition.vo.RuleDeviceConditionPageVO;
import org.apache.ibatis.annotations.Param;

public interface RuleDeviceConditionService extends IService<RuleDeviceCondition> {

    Page<RuleDeviceConditionPageVO> page(@Param("param") RuleDeviceConditionQueryParam param);

    void removeByRuleId(Long ruleId);
}
