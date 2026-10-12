package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.plan.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskWarningDisposeParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskWarningRecordPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskWarningRecordVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.workflow.RiskPlanWarningWorkflowService;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 风险预警记录接口。
 */
@RestController("securityManagementRiskWarningRecordController")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RiskWarningRecordController {
    private final RiskPlanWarningWorkflowService workflowService;

    /**
     * 查询预警记录分页列表。
     *
     * @param param 查询条件，status 按预警状态筛选，unitId 按风险单元筛选，objectId 按风险对象筛选
     * @return 预警记录分页结果
     */
    @GetMapping("/risk-control/warning-records")
    public Pagination<RiskWarningRecordVO> warningRecords(RiskWarningRecordPageParam param) {
        return workflowService.warningRecordPage(param);
    }

    /**
     * 导出预警记录列表。
     *
     * @param param 导出条件，status 按预警状态筛选，unitId 按风险单元筛选，objectId 按风险对象筛选
     * @param response HTTP 响应，用于写出 xlsx 文件流
     */
    @GetMapping("/risk-control/warning-records/export")
    public void exportWarningRecords(RiskWarningRecordPageParam param, HttpServletResponse response) {
        workflowService.exportWarningRecords(param, response);
    }

    /**
     * 查询预警记录详情。
     *
     * @param id 预警记录 ID
     * @return 预警记录详情
     */
    @GetMapping("/risk-control/warning-records/{id}")
    public RiskWarningRecordVO warningRecordDetail(@PathVariable Long id) {
        return workflowService.warningRecordDetail(id);
    }

    /**
     * 处置预警记录。
     *
     * @param id 预警记录 ID
     * @return 处置后的预警记录详情
     */
    @PatchMapping("/risk-control/warning-records/{id}")
    public RiskWarningRecordVO disposeWarningRecord(@PathVariable Long id,
                                                   @RequestBody(required = false) RiskWarningDisposeParam param) {
        return workflowService.disposeWarningRecord(id, param);
    }
}
