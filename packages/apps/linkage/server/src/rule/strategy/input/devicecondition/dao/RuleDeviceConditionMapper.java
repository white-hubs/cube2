package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicecondition.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicecondition.entity.RuleDeviceCondition;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicecondition.param.RuleDeviceConditionQueryParam;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicecondition.vo.RuleDeviceConditionPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RuleDeviceConditionMapper extends BaseMapper<RuleDeviceCondition> {

    Page<RuleDeviceConditionPageVO> page(@Param("param") RuleDeviceConditionQueryParam param, Page page);
}
