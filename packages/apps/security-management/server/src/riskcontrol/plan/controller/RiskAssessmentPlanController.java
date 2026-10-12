package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.plan.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskAssessmentPlanPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskAssessmentPlanParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskEnabledParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskAssessmentPlanVO;
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
 * 风险评估计划接口。
 */
@RestController("securityManagementRiskAssessmentPlanController")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RiskAssessmentPlanController {
    private final RiskPlanWarningWorkflowService workflowService;

    /**
     * 查询评估计划分页列表。
     *
     * @param param 查询条件，unitId 按风险单元筛选，status 按计划状态筛选
     * @return 评估计划分页结果
     */
    @GetMapping("/risk-control/assessment-plans")
    public Pagination<RiskAssessmentPlanVO> assessmentPlans(RiskAssessmentPlanPageParam param) {
        return workflowService.planPage(param);
    }

    /**
     * 查询评估计划详情。
     *
     * @param id 评估计划 ID
     * @return 评估计划详情，包含通知人员
     */
    @GetMapping("/risk-control/assessment-plans/{id}")
    public RiskAssessmentPlanVO assessmentPlanDetail(@PathVariable Long id) {
        return workflowService.planDetail(id);
    }

    /**
     * 新增评估计划。
     *
     * @param param 评估计划入参，包含 unitId、planName、cycleType、nextExecuteTime、notifyUserIds
     * @return 新增后的评估计划详情
     */
    @PostMapping("/risk-control/assessment-plans")
    public RiskAssessmentPlanVO createAssessmentPlan(@RequestBody RiskAssessmentPlanParam param) {
        return workflowService.createPlan(param);
    }

    /**
     * 修改评估计划。
     *
     * @param id 评估计划 ID
     * @param param 评估计划更新入参，包含 unitId、planName、cycleType、nextExecuteTime、notifyUserIds
     * @return 更新后的评估计划详情
     */
    @PutMapping("/risk-control/assessment-plans/{id}")
    public RiskAssessmentPlanVO updateAssessmentPlan(@PathVariable Long id, @RequestBody RiskAssessmentPlanParam param) {
        return workflowService.updatePlan(id, param);
    }

    /**
     * 启用或禁用评估计划。
     *
     * @param id 评估计划 ID
     * @param param 状态入参，enabled=true 表示启用，enabled=false 表示禁用
     * @return 更新后的评估计划状态
     */
    @PatchMapping("/risk-control/assessment-plans/{id}")
    public RiskAssessmentPlanVO patchAssessmentPlan(@PathVariable Long id, @RequestBody RiskEnabledParam param) {
        return workflowService.patchPlan(id, param);
    }

    /**
     * 删除评估计划。
     *
     * @param id 评估计划 ID
     */
    @DeleteMapping("/risk-control/assessment-plans/{id}")
    public void deleteAssessmentPlan(@PathVariable Long id) {
        workflowService.deletePlan(id);
    }
}
