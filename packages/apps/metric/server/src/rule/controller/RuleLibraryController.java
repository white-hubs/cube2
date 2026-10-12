package com.guhecloud.cube.biz.metric.server.rule.controller;

import com.guhecloud.cube.biz.metric.server.rule.command.ChangeRuleConfigCommand;
import com.guhecloud.cube.biz.metric.server.rule.command.DelRuleConfigCommand;
import com.guhecloud.cube.biz.metric.server.rule.command.IncreaseRuleConfigCommand;
import com.guhecloud.cube.biz.metric.server.rule.service.command.RuleLibraryCommandService;
import com.guhecloud.cube.biz.metric.server.rule.service.query.RuleLibraryQueryService;
import com.guhecloud.cube.biz.metric.server.rule.vo.RuleDetailVO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RuleLibraryController {

    private final RuleLibraryCommandService ruleLibraryCommandService;

    private final RuleLibraryQueryService ruleLibraryQueryService;

    public RuleLibraryController(RuleLibraryCommandService ruleLibraryCommandService,
                                 RuleLibraryQueryService ruleLibraryQueryService
                                ) {
        this.ruleLibraryCommandService = ruleLibraryCommandService;
        this.ruleLibraryQueryService = ruleLibraryQueryService;
    }

    /**
     * 新增规则配置
     *
     * @param command
     *
     * @return
     */
    @PostMapping("/rule-libraries")
    public Long increaseConfig(@RequestBody IncreaseRuleConfigCommand command) {
        return ruleLibraryCommandService.increaseConfig(command);
    }

    /**
     * 修改规则配置
     *
     * @param command
     *
     * @return
     */
    @PutMapping("/rule-libraries")
    public Long changeConfig(@RequestBody ChangeRuleConfigCommand command) {
        return ruleLibraryCommandService.changeConfig(command);
    }

    /**
     * 获取规则树形列表
     *
     * @return
     */
    @GetMapping("/rule-libraries/tree")
    public List<RuleDetailVO> getSafetyReportRulesTree() {
        return ruleLibraryQueryService.getDetailConfigsTree();
    }

    /**
     * 规则详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/rule-libraries/{id}")
    public RuleDetailVO getSafetyReportRule(@PathVariable("id") Long id) {
        return ruleLibraryQueryService.getDetail(id);
    }

    /**
     * 删除规则
     *
     * @param id
     */
    @DeleteMapping("/rule-libraries/{id}")
    public void removeById(@PathVariable("id") Long id) {
        DelRuleConfigCommand command = new DelRuleConfigCommand();
        command.setId(id);
        ruleLibraryCommandService.removeConfig(command);
    }
}
