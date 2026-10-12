package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.timing.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.timing.entity.RuleTiming;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.timing.param.RuleTimingQueryParam;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.timing.vo.RuleTimingPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RuleTimingMapper extends BaseMapper<RuleTiming> {

    Page<RuleTimingPageVO> page(@Param("param") RuleTimingQueryParam param, Page page);
}
