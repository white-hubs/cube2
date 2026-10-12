package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.scene.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.scene.dao.RuleSceneMapper;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.scene.entity.RuleScene;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.scene.param.RuleSceneQueryParam;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.scene.service.RuleSceneService;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.scene.vo.RuleScenePageVO;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RuleSceneServiceImpl extends ServiceImpl<RuleSceneMapper, RuleScene> implements RuleSceneService {

    @Override
    public Page<RuleScenePageVO> page(RuleSceneQueryParam param) {
        return this.baseMapper.page(param, PagingUtils.convert(param));
    }

    @Override
    public RuleScene getByRuleId(@NonNull Long ruleId) {
        return this.baseMapper.selectOne(Wrappers.<RuleScene>lambdaQuery().eq(RuleScene::getRuleId, ruleId));
    }

    @Override
    public void removeByRuleId(@NonNull Long ruleId) {
        RuleScene ruleScene = this.getByRuleId(ruleId);
        if (Objects.nonNull(ruleScene)) {
            DatabaseContextHolder.softDelete(this, ruleScene);
        }
    }
}
