package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.identify.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskReviewCreateParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskReviewTaskPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskObjectVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskReviewRecordVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.workflow.RiskReviewWorkflowService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 风险辨识审核接口。
 */
@RestController("securityManagementRiskReviewController")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RiskReviewController {
    private final RiskReviewWorkflowService reviewWorkflowService;

    /**
     * 查询辨识审核任务分页列表。
     *
     * @param param 查询条件，unitId 按风险单元筛选，status 按 PENDING_REVIEW/APPROVED/REJECTED 审核状态筛选，identifierUserId 按辨识人筛选
     * @return 审核任务分页结果，按辨识提交记录维度返回；无审核记录为待审核，有审核记录时返回对应审核结论
     */
    @DataPermissionSwitch(dataModel = "riskControl:review")
    @GetMapping("/risk-control/review-tasks")
    public Pagination<RiskObjectVO> reviewTasks(RiskReviewTaskPageParam param) {
        return reviewWorkflowService.reviewTaskPage(param);
    }

    /**
     * 查询辨识审核任务详情。
     *
     * @param objectId 风险对象 ID
     * @return 审核任务详情，待审核对象返回辨识提交信息，已通过或已退回对象额外返回审核结果、审核人、审核时间和审核意见
     */
    @DataPermissionSwitch(dataModel = "riskControl:review")
    @GetMapping("/risk-control/review-tasks/{objectId}")
    public RiskObjectVO reviewTaskDetail(@PathVariable Long objectId) {
        return reviewWorkflowService.reviewTaskDetail(objectId);
    }

    /**
     * 创建单条或批量审核记录。
     *
     * @param param 审核入参，objectId 或 objectIds 指定审核对象，reviewResult 指定审核结论，REJECTED 时 rejectReason 必填
     * @return 审核记录列表
     */
    @PostMapping("/risk-control/reviews")
    public List<RiskReviewRecordVO> createReviews(@RequestBody RiskReviewCreateParam param) {
        return reviewWorkflowService.createReviews(param);
    }
}
