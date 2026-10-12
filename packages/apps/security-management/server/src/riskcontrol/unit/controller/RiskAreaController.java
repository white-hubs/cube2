package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskAreaPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskAreaParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskAreaVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.vo.RiskOptionVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.workflow.RiskUnitWorkflowService;
import com.guhecloud.platform.base.model.Pagination;
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
 * 风险区域接口。
 */
@RestController("securityManagementRiskAreaController")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RiskAreaController {
    private final RiskUnitWorkflowService unitWorkflowService;

    /**
     * 查询风险区域分页列表。
     *
     * @param param 查询条件，keyword 按区域编号、名称、负责人用户 ID 模糊匹配；
     *              departmentId 按所属部门筛选；areaType 按区域类型筛选；riskLevel 按区域现状风险评估等级筛选
     * @return 风险区域分页结果，包含备注 description、固有风险评估 inherentRiskLevel、现状风险评估 controlRiskLevel 和风险单元数 unitCount
     */
    @GetMapping("/risk-control/areas")
    public Pagination<RiskAreaVO> areas(RiskAreaPageParam param) {
        return unitWorkflowService.areaPage(param);
    }

    /**
     * 查询风险区域下拉选项。
     *
     * @param param 查询条件，keyword 按区域名称模糊匹配
     * @return 风险区域选项列表
     */
    @GetMapping("/risk-control/area-options")
    public List<RiskOptionVO> areaOptions(RiskAreaPageParam param) {
        return unitWorkflowService.areaOptions(param);
    }

    /**
     * 查询风险区域详情。
     *
     * @param id 风险区域 ID
     * @return 风险区域详情，包含编号、名称、所属部门、范围、备注、负责人、固有风险评估、现状风险评估、风险单元数和 GIS 信息
     */
    @GetMapping("/risk-control/areas/{id}")
    public RiskAreaVO areaDetail(@PathVariable Long id) {
        return unitWorkflowService.areaDetail(id);
    }

    /**
     * 新增风险区域。
     *
     * @param param 区域入参，code 可选，未传由后端生成；包含 name、departmentId、rangeDesc、description、ownerUserId、areaType、gisGeometry
     * @return 新增后的区域详情
     */
    @PostMapping("/risk-control/areas")
    public RiskAreaVO createArea(@RequestBody RiskAreaParam param) {
        return unitWorkflowService.createArea(param);
    }

    /**
     * 修改风险区域。
     *
     * @param id 风险区域 ID
     * @param param 区域更新入参，code 可选，未传保持原编号；包含 name、departmentId、rangeDesc、description、ownerUserId、areaType、gisGeometry
     * @return 更新后的区域详情
     */
    @PutMapping("/risk-control/areas/{id}")
    public RiskAreaVO updateArea(@PathVariable Long id, @RequestBody RiskAreaParam param) {
        return unitWorkflowService.updateArea(id, param);
    }

    /**
     * 删除风险区域。
     *
     * @param id 风险区域 ID
     */
    @DeleteMapping("/risk-control/areas/{id}")
    public void deleteArea(@PathVariable Long id) {
        unitWorkflowService.deleteArea(id);
    }
}
