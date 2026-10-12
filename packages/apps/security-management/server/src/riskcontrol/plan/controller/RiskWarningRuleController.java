package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.plan.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskEnabledParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskWarningRulePageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskWarningRuleParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskWarningRuleVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.workflow.RiskPlanWarningWorkflowService;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 风险预警规则接口。
 */
@RestController("securityManagementRiskWarningRuleController")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RiskWarningRuleController {
    private final RiskPlanWarningWorkflowService workflowService;

    /**
     * 查询预警规则分页列表。
     *
     * @param param 查询条件，enabled 按启用状态筛选
     * @return 预警规则分页结果
     */
    @GetMapping("/risk-control/warning-rules")
    public Pagination<RiskWarningRuleVO> warningRules(RiskWarningRulePageParam param) {
        return workflowService.warningRulePage(param);
    }

    /**
     * 查询预警规则详情。
     *
     * @param id 预警规则 ID
     * @return 预警规则详情，包含预警类型、触发条件、严重程度、通知渠道、处置期限和启用状态
     */
    @GetMapping("/risk-control/warning-rules/{id}")
    public RiskWarningRuleVO warningRuleDetail(@PathVariable Long id) {
        return workflowService.warningRuleDetail(id);
    }

    /**
     * 新增预警规则。
     *
     * @param param 预警规则入参，包含 warningType、conditionJson、severity、notifyChannels、disposeDeadlineDays、enabled
     * @return 新增后的预警规则详情
     */
    @PostMapping("/risk-control/warning-rules")
    public RiskWarningRuleVO createWarningRule(@RequestBody RiskWarningRuleParam param) {
        return workflowService.createWarningRule(param);
    }

    /**
     * 修改预警规则。
     *
     * @param id 预警规则 ID
     * @param param 预警规则更新入参，包含 warningType、conditionJson、severity、notifyChannels、disposeDeadlineDays、enabled
     * @return 更新后的预警规则详情
     */
    @PutMapping("/risk-control/warning-rules/{id}")
    public RiskWarningRuleVO updateWarningRule(@PathVariable Long id, @RequestBody RiskWarningRuleParam param) {
        return workflowService.updateWarningRule(id, param);
    }

    /**
     * 启用或禁用预警规则。
     *
     * @param id 预警规则 ID
     * @param param 状态入参，enabled=true 表示启用，enabled=false 表示禁用
     * @return 更新后的预警规则状态
     */
    @PatchMapping("/risk-control/warning-rules/{id}")
    public RiskWarningRuleVO patchWarningRule(@PathVariable Long id, @RequestBody RiskEnabledParam param) {
        return workflowService.patchWarningRule(id, param);
    }

    /**
     * 删除预警规则。
     *
     * @param id 预警规则 ID
     */
    @DeleteMapping("/risk-control/warning-rules/{id}")
    public void deleteWarningRule(@PathVariable Long id) {
        workflowService.deleteWarningRule(id);
    }
}
