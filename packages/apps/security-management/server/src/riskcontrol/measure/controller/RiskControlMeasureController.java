package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.measure.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskControlMeasurePageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskObjectMeasureSaveParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskControlMeasureVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.workflow.RiskMeasureWorkflowService;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 风险管控措施接口。
 */
@RestController("securityManagementRiskControlMeasureController")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RiskControlMeasureController {
    private final RiskMeasureWorkflowService measureWorkflowService;

    /**
     * 查询管控措施分页列表。
     *
     * @param param 查询条件，objectId 按风险对象筛选，status 按执行状态筛选
     * @return 管控措施分页结果
     */
    @GetMapping("/risk-control/control-measures")
    public Pagination<RiskControlMeasureVO> controlMeasures(RiskControlMeasurePageParam param) {
        return measureWorkflowService.measurePage(param);
    }

    /**
     * 导出管控措施列表。
     *
     * @param param 导出条件，objectId 按风险对象筛选，status 按执行状态筛选
     * @param response HTTP 响应，用于写出 xlsx 文件流
     */
    @GetMapping("/risk-control/control-measures/export")
    public void exportControlMeasures(RiskControlMeasurePageParam param, HttpServletResponse response) {
        measureWorkflowService.exportMeasures(param, response);
    }

    /**
     * 查询风险对象的管控措施列表。
     *
     * @param objectId 风险对象 ID
     * @return 管控措施列表
     */
    @GetMapping("/risk-control/risk-objects/{objectId}/control-measures")
    public Pagination<RiskControlMeasureVO> objectMeasures(@PathVariable Long objectId) {
        List<RiskControlMeasureVO> records = measureWorkflowService.objectMeasures(objectId);
        return Pagination.<RiskControlMeasureVO>builder()
                .total((long) records.size())
                .pages(1L)
                .records(records)
                .build();
    }

    /**
     * 保存并提交风险对象管控措施配置。
     *
     * @param objectId 风险对象 ID
     * @param param 措施配置入参，measures 为管控措施列表，每项包含 controlType、controlLevel、executorUserId、content
     * @return 保存后的管控措施列表
     */
    @PutMapping("/risk-control/risk-objects/{objectId}/control-measures")
    public List<RiskControlMeasureVO> saveObjectMeasures(@PathVariable Long objectId,
                                                         @RequestBody RiskObjectMeasureSaveParam param) {
        return measureWorkflowService.saveObjectMeasures(objectId, param);
    }

    /**
     * 查询管控措施执行详情。
     *
     * @param measureId 管控措施 ID
     * @return 管控措施详情，包含历史执行记录和附件文件 ID
     */
    @GetMapping("/risk-control/control-measures/{measureId}")
    public RiskControlMeasureVO measureDetail(@PathVariable Long measureId) {
        return measureWorkflowService.measureDetail(measureId);
    }
}
