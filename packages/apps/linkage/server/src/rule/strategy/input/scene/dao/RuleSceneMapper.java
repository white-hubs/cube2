package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.scene.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.scene.entity.RuleScene;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.scene.param.RuleSceneQueryParam;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.scene.vo.RuleScenePageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RuleSceneMapper extends BaseMapper<RuleScene> {

    Page<RuleScenePageVO> page(@Param("param") RuleSceneQueryParam param, Page page);
}
