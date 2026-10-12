package com.guhecloud.cube.biz.linkage.server.rule.base.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.linkage.server.rule.base.entity.Rule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RuleMapper extends BaseMapper<Rule> {

    int addTriggerNum(@Param("id") Long id);
}
