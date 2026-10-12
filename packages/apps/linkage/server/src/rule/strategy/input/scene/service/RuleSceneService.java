package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.scene.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.scene.entity.RuleScene;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.scene.param.RuleSceneQueryParam;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.scene.vo.RuleScenePageVO;

public interface RuleSceneService extends IService<RuleScene> {

    Page<RuleScenePageVO> page(RuleSceneQueryParam param);

    RuleScene getByRuleId(Long ruleId);

    void removeByRuleId(Long ruleId);
}
