package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.identify.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskAssessmentParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskAssessmentPatchParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskAssessmentTaskPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskAssessmentRecordVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskObjectVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.workflow.RiskAssessmentWorkflowService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 风险评估接口。
 */
@RestController("securityManagementRiskAssessmentController")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RiskAssessmentController {
    private final RiskAssessmentWorkflowService assessmentWorkflowService;

    /**
     * 查询风险评估任务分页列表。
     *
     * @param param 查询条件，unitId 按风险单元筛选
     * @return 固有评估或控制评估待办对象分页结果
     */
    @DataPermissionSwitch(dataModel = "riskControl:assessment")
    @GetMapping("/risk-control/assessment-tasks")
    public Pagination<RiskObjectVO> assessmentTasks(RiskAssessmentTaskPageParam param) {
        return assessmentWorkflowService.assessmentTaskPage(param);
    }

    /**
     * 查询风险对象评估记录。
     *
     * @param objectId 风险对象 ID
     * @return 评估记录列表，包含固有评估和控制评估记录
     */
    @GetMapping("/risk-control/risk-objects/{objectId}/assessments")
    public Pagination<RiskAssessmentRecordVO> objectAssessments(@PathVariable Long objectId,
                                                                @RequestParam(required = false) String assessmentType) {
        List<RiskAssessmentRecordVO> records = assessmentWorkflowService.objectAssessments(objectId, assessmentType);
        return Pagination.<RiskAssessmentRecordVO>builder()
                .total((long) records.size())
                .pages(1L)
                .records(records)
                .build();
    }

    /**
     * 创建评估记录或评估草稿。
     *
     * @param param 评估入参，包含 objectId、assessmentType、methodId、dimensionScores、submitted；
     *              dimensionScores 的 key 为维度编码，value 为用户选择的维度选项分值
     * @return 创建后的评估记录
     */
    @PostMapping("/risk-control/assessments")
    public RiskAssessmentRecordVO createAssessment(@RequestBody RiskAssessmentParam param) {
        return assessmentWorkflowService.createAssessment(param);
    }

    /**
     * 修改评估草稿或提交评估。
     *
     * @param id 评估记录 ID
     * @param param 评估更新入参，包含 methodId、dimensionScores、submitted；
     *              dimensionScores 的 key 为维度编码，value 为用户选择的维度选项分值
     * @return 更新后的评估记录
     */
    @PatchMapping("/risk-control/assessments/{id}")
    public RiskAssessmentRecordVO patchAssessment(@PathVariable Long id, @RequestBody RiskAssessmentPatchParam param) {
        return assessmentWorkflowService.patchAssessment(id, param);
    }
}
