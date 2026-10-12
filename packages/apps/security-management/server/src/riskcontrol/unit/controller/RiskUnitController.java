package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskUnitPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskUnitParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskUnitVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.vo.RiskOptionVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.workflow.RiskUnitWorkflowService;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 风险单元接口。
 */
@RestController("securityManagementRiskUnitController")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RiskUnitController {
    private final RiskUnitWorkflowService unitWorkflowService;

    /**
     * 查询风险单元分页列表。
     *
     * @param param 查询条件，keyword 按单元名称模糊匹配；areaId 按所属区域筛选；
     * unitTypeId 按单元类型筛选；responsibleUserId 按责任人筛选；status 按辨识状态筛选，兼容待辨识/辨识中/待审核/已审核
     * @return 风险单元分页结果；列表项包含 area、unitType、warningSign、responsibleUser、identifierUsers，供列表直接返显
     */
    @GetMapping("/risk-control/units")
    public Pagination<RiskUnitVO> units(RiskUnitPageParam param) {
        return unitWorkflowService.unitPage(param);
    }

    /**
     * 导出风险单元列表。
     *
     * @param param 导出条件，keyword 按单元名称模糊匹配；areaId 按所属区域筛选；unitTypeId 按单元类型筛选
     * @param response HTTP 响应，用于写出 xlsx 文件流
     */
    @GetMapping("/risk-control/units/export")
    public void exportUnits(RiskUnitPageParam param, HttpServletResponse response) {
        unitWorkflowService.exportUnits(param, response);
    }

    /**
     * 查询风险单元下拉选项。
     *
     * @param param 查询条件，keyword 按单元名称模糊匹配
     * @return 风险单元选项列表
     */
    @GetMapping("/risk-control/unit-options")
    public List<RiskOptionVO> unitOptions(RiskUnitPageParam param) {
        return unitWorkflowService.unitOptions(param);
    }

    /**
     * 查询风险单元详情。
     *
     * @param id 风险单元 ID
     * @return 风险单元详情，包含区域、类型、责任人、警示标志、辨识人员和风险等级
     */
    @GetMapping("/risk-control/units/{id}")
    public RiskUnitVO unitDetail(@PathVariable Long id) {
        return unitWorkflowService.unitDetail(id);
    }

    /**
     * 新增风险单元。
     *
     * @param param 单元入参，code 可选，未传由后端生成；必填 name、areaId、unitTypeId、warningSignId、
     * responsibleUserId、identifyCycleDays、identifierUserIds；description 可选，最多 500 字；所在位置传 gisPoint，或同时传 longitude、latitude
     * @return 新增后的风险单元详情
     */
    @PostMapping("/risk-control/units")
    public RiskUnitVO createUnit(@RequestBody RiskUnitParam param) {
        return unitWorkflowService.createUnit(param);
    }

    /**
     * 修改风险单元。
     *
     * @param id 风险单元 ID
     * @param param 单元更新入参，code 可选，未传保持原编号；必填 name、areaId、unitTypeId、warningSignId、
     * responsibleUserId、identifyCycleDays、identifierUserIds；description 可选，最多 500 字；所在位置传 gisPoint，或同时传 longitude、latitude
     * @return 更新后的风险单元详情
     */
    @PutMapping("/risk-control/units/{id}")
    public RiskUnitVO updateUnit(@PathVariable Long id, @RequestBody RiskUnitParam param) {
        return unitWorkflowService.updateUnit(id, param);
    }

    /**
     * 删除风险单元。
     *
     * @param id 风险单元 ID
     */
    @DeleteMapping("/risk-control/units/{id}")
    public void deleteUnit(@PathVariable Long id) {
        unitWorkflowService.deleteUnit(id);
    }
}
