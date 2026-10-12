package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.scene.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.linkage.server.rule.base.entity.Rule;
import com.guhecloud.cube.biz.linkage.server.rule.base.service.RuleService;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessEventHandlerRequest;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.scene.entity.RuleScene;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.scene.handler.RuleSceneHandler;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.scene.param.RuleSceneQueryParam;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.scene.param.RuleSceneTriggerParam;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.scene.service.RuleSceneService;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.scene.vo.RuleScenePageVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 场景规则管理接口
 *
 * @author lanchun
 * @date 2022/6/30 11:06
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RuleSceneController {

    private final RuleService ruleService;

    private final RuleSceneService ruleSceneService;

    private final RuleSceneHandler ruleSceneHandler;

    private final PasswordEncoder passwordEncoder;

    /**
     * 场景规则分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/rule-scenes")
    public Pagination<RuleScenePageVO> page(@Valid RuleSceneQueryParam param) {
        Page<RuleScenePageVO> page = this.ruleSceneService.page(param);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        return PagingUtils.convert(page);
    }

    /**
     * 场景规则触发
     *
     * @param param
     */
    @PostMapping("/rule-scene/trigger")
    public void trigger(@Valid @RequestBody RuleSceneTriggerParam param) {
        Rule rule = this.ruleService.getById(param.getId());
        Assert.isNotFound(rule, "规则不存在");
        RuleScene ruleScene = this.ruleSceneService.getByRuleId(param.getId());
        Assert.isNotFound(ruleScene, "规则不存在");
        if (StringUtils.isNotEmpty(ruleScene.getPassword())) {
            Assert.isUnauthenticated(!this.passwordEncoder.matches(param.getPassword(), ruleScene.getPassword()), "密码输入不正确");
        }
        RuleBusinessEventHandlerRequest request = new RuleBusinessEventHandlerRequest(rule.getBusinessType());
        request.setRule(rule);
        request.setTriggerTime(DateUtils.now());
        this.ruleSceneHandler.ruleEvent(request);
    }
}
