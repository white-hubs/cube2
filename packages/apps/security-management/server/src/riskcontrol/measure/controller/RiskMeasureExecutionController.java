package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.measure.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskControlMeasurePageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskMeasureExecutionRecordParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskControlMeasureVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskMeasureExecutionRecordVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.workflow.RiskMeasureWorkflowService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 风险管控措施执行记录接口。
 */
@RestController("securityManagementRiskMeasureExecutionController")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RiskMeasureExecutionController {
    private final RiskMeasureWorkflowService measureWorkflowService;

    /**
     * 查询措施执行分页列表。
     *
     * @param param 查询条件，executor=current 表示当前登录人的执行任务，status 按执行状态筛选
     * @return 措施执行分页结果
     */
    @DataPermissionSwitch(dataModel = "riskControl:measureExecution")
    @GetMapping("/risk-control/measure-executions")
    public Pagination<RiskControlMeasureVO> measureExecutions(RiskControlMeasurePageParam param) {
        return measureWorkflowService.measureExecutionPage(param);
    }

    /**
     * 导出措施执行列表。
     *
     * @param param 导出条件，executor=current 表示当前登录人的执行任务，status 按执行状态筛选
     * @param response HTTP 响应，用于写出 xlsx 文件流
     */
    @DataPermissionSwitch(dataModel = "riskControl:measureExecution")
    @GetMapping("/risk-control/measure-executions/export")
    public void exportMeasureExecutions(RiskControlMeasurePageParam param, HttpServletResponse response) {
        measureWorkflowService.exportMeasureExecutions(param, response);
    }

    /**
     * 创建措施执行反馈记录。
     *
     * @param measureId 管控措施 ID
     * @param param 执行反馈入参，包含 executionResult、executionDesc、executorUserId、gpsLocation、fileIds；fileIds 为文件服务使用场景码 RISK::CONTROL_MEASURE_EXECUTION_ATTACHMENT 上传得到的文件 ID 集合，最多 9 个
     * @return 创建后的执行记录，包含 fileIds 和由文件服务解析得到的 files
     */
    @PostMapping("/risk-control/control-measures/{measureId}/execution-records")
    public RiskMeasureExecutionRecordVO createExecutionRecord(@PathVariable Long measureId,
                                                              @RequestBody RiskMeasureExecutionRecordParam param) {
        return measureWorkflowService.createExecutionRecord(measureId, param);
    }

    /**
     * 创建 H5 离线执行反馈同步批次。
     *
     * @param params 执行反馈入参数组，每项需包含 measureId、executionResult、executionDesc、executorUserId、gpsLocation、fileIds；fileIds 为文件服务使用场景码 RISK::CONTROL_MEASURE_EXECUTION_ATTACHMENT 上传得到的文件 ID 集合，最多 9 个
     * @return 同步创建的执行记录列表，包含 fileIds 和由文件服务解析得到的 files
     */
    @PostMapping("/risk-control/measure-execution-sync-batches")
    public List<RiskMeasureExecutionRecordVO> createExecutionSyncBatch(
            @RequestBody List<RiskMeasureExecutionRecordParam> params) {
        return measureWorkflowService.createExecutionRecords(params);
    }
}
