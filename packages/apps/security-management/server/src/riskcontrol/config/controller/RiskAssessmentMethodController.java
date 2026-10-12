package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskAssessmentMethodPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskAssessmentMethodParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskEnabledParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskAssessmentMethodVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.vo.RiskOptionVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.workflow.RiskConfigWorkflowService;
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

import java.util.List;

/**
 * 风险评估方法接口。
 */
@RestController("securityManagementRiskAssessmentMethodController")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RiskAssessmentMethodController {
    private final RiskConfigWorkflowService configWorkflowService;

    /**
     * 查询评估方法分页列表。
     *
     * @param param 查询条件，keyword 按方法编码或名称模糊匹配；enabled 按启用状态筛选
     * @return 评估方法分页结果，包含评价维度、维度选项和等级判定规则
     */
    @GetMapping("/risk-control/assessment-methods")
    public Pagination<RiskAssessmentMethodVO> assessmentMethods(RiskAssessmentMethodPageParam param) {
        return configWorkflowService.methodPage(param);
    }

    /**
     * 查询启用的评估方法下拉选项。
     *
     * @param param 查询条件，keyword 按方法编码或名称模糊匹配
     * @return 评估方法选项列表
     */
    @GetMapping("/risk-control/assessment-method-options")
    public List<RiskOptionVO> assessmentMethodOptions(RiskAssessmentMethodPageParam param) {
        return configWorkflowService.methodOptions(param);
    }

    /**
     * 查询评估方法详情。
     *
     * @param id 评估方法 ID
     * @return 评估方法详情，包含方法说明、维度、维度分值范围、选项和等级判定规则控制要求
     */
    @GetMapping("/risk-control/assessment-methods/{id}")
    public RiskAssessmentMethodVO assessmentMethodDetail(@PathVariable Long id) {
        return configWorkflowService.methodDetail(id);
    }

    /**
     * 新增评估方法。
     *
     * @param param 评估方法入参，方法 code 可选，未传由后端生成；包含 name、description、formula、dimensions、levelRules、enabled；
     *              dimensions 每项必须包含作为公式变量的 code、name、scoreRange、options，levelRules 每项包含 riskLevel、minScore、maxScore、color、controlRequirement
     * @return 新增后的评估方法详情
     */
    @PostMapping("/risk-control/assessment-methods")
    public RiskAssessmentMethodVO createAssessmentMethod(@RequestBody RiskAssessmentMethodParam param) {
        return configWorkflowService.createMethod(param);
    }

    /**
     * 修改评估方法。
     *
     * @param id 评估方法 ID
     * @param param 评估方法更新入参，方法 code 可选，未传保持原编号；包含 name、description、formula、dimensions、levelRules、enabled；
     *              dimensions 每项必须包含作为公式变量的 code、name、scoreRange、options，levelRules 每项包含 riskLevel、minScore、maxScore、color、controlRequirement
     * @return 更新后的评估方法详情
     */
    @PutMapping("/risk-control/assessment-methods/{id}")
    public RiskAssessmentMethodVO updateAssessmentMethod(@PathVariable Long id, @RequestBody RiskAssessmentMethodParam param) {
        return configWorkflowService.updateMethod(id, param);
    }

    /**
     * 启用或禁用评估方法。
     *
     * @param id 评估方法 ID
     * @param param 状态入参，enabled=true 表示启用，enabled=false 表示禁用
     * @return 更新后的评估方法状态
     */
    @PatchMapping("/risk-control/assessment-methods/{id}")
    public RiskAssessmentMethodVO patchAssessmentMethod(@PathVariable Long id, @RequestBody RiskEnabledParam param) {
        return configWorkflowService.patchMethod(id, param);
    }

    /**
     * 删除评估方法。
     *
     * @param id 评估方法 ID
     */
    @DeleteMapping("/risk-control/assessment-methods/{id}")
    public void deleteAssessmentMethod(@PathVariable Long id) {
        configWorkflowService.deleteMethod(id);
    }
}
