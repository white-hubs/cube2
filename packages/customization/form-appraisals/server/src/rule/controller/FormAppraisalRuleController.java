
package com.guhecloud.cube.srv.gen.appraisals.server.rule.controller;

import com.guhecloud.cube.srv.gen.appraisals.server.rule.param.FormAppraisalRuleCommand;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.param.FormAppraisalRulePageParam;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.service.FormAppraisalRuleQueryService;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.service.FormAppraisalRuleService;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.vo.FormAppraisalRuleDetailVO;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.vo.FormAppraisalRulePageVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 表单考核规则控制器
 * 提供考核规则的 CRUD 操作接口
 */
@RestController
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FormAppraisalRuleController {
    private final FormAppraisalRuleService ruleService;
    private final FormAppraisalRuleQueryService ruleQueryService;

    /**
     * 根据 ID 获取考核规则详情
     *
     * @param id 规则 ID
     * @return 考核规则详情 VO
     */
    @GetMapping("/form-appraisal-rule/{id}")
    public FormAppraisalRuleDetailVO getById(@PathVariable Long id) {
        return ruleQueryService.getById(id);
    }

    /**
     * 分页获取考核规则列表
     *
     * @param param 分页查询参数
     * @return 考核规则分页数据
     */
    @GetMapping("/form-appraisal-rule/page")
    public Pagination<FormAppraisalRulePageVO> getPage(FormAppraisalRulePageParam param) {
        return ruleQueryService.getPage(param);
    }

    /**
     * 保存考核规则
     *
     * @param command 考核规则保存命令
     * @return 新创建的规则 ID
     */
    @PostMapping("/form-appraisal-rule")
    public Long save(@RequestBody @Valid FormAppraisalRuleCommand command) {
        return ruleService.save(command);
    }

    /**
     * 更新考核规则
     *
     * @param id      规则 ID
     * @param command 考核规则更新命令
     */
    @PutMapping("/form-appraisal-rule/{id}")
    public void update(@PathVariable Long id, @RequestBody @Valid FormAppraisalRuleCommand command) {
        ruleService.update(id, command);
    }

    /**
     * 启用或禁用考核规则
     *
     * @param id     规则 ID
     * @param enable 启用状态 true-启用 false-禁用
     */
    @PostMapping("/form-appraisal-rule/{id}/enabled")
    public void enabled(@PathVariable Long id, @RequestParam("enable") Boolean enable) {
        ruleService.enabled(id, enable);
    }

    /**
     * 删除考核规则
     *
     * @param id 规则 ID
     */
    @DeleteMapping("/form-appraisal-rule/{id}")
    public void remove(@PathVariable Long id) {
        ruleService.remove(id);
    }
}
