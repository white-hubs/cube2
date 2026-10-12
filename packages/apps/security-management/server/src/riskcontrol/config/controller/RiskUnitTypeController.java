package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskUnitTypePageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskUnitTypeParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskUnitTypeVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.vo.RiskOptionVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.workflow.RiskConfigWorkflowService;
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
 * 风险单元类型接口。
 */
@RestController("securityManagementRiskUnitTypeController")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RiskUnitTypeController {
    private final RiskConfigWorkflowService configWorkflowService;

    /**
     * 查询单元类型分页列表。
     *
     * @param param 查询条件，keyword 按类型名称模糊匹配
     * @return 单元类型分页结果
     */
    @GetMapping("/risk-control/unit-types")
    public Pagination<RiskUnitTypeVO> unitTypes(RiskUnitTypePageParam param) {
        return configWorkflowService.unitTypePage(param);
    }

    /**
     * 查询单元类型下拉选项。
     *
     * @param param 查询条件，keyword 按类型名称模糊匹配
     * @return 单元类型选项列表
     */
    @GetMapping("/risk-control/unit-type-options")
    public List<RiskOptionVO> unitTypeOptions(RiskUnitTypePageParam param) {
        return configWorkflowService.unitTypeOptions(param);
    }

    /**
     * 查询单元类型详情。
     *
     * @param id 单元类型 ID
     * @return 单元类型详情，包含编号、名称、默认辨识周期和辨识维度说明
     */
    @GetMapping("/risk-control/unit-types/{id}")
    public RiskUnitTypeVO unitTypeDetail(@PathVariable Long id) {
        return configWorkflowService.unitTypeDetail(id);
    }

    /**
     * 新增单元类型。
     *
     * @param param 单元类型入参，code 可选，未传由后端生成；包含 name、identifyCycleDays、identifyDimensionDesc
     * @return 新增后的单元类型详情
     */
    @PostMapping("/risk-control/unit-types")
    public RiskUnitTypeVO createUnitType(@RequestBody RiskUnitTypeParam param) {
        return configWorkflowService.createUnitType(param);
    }

    /**
     * 修改单元类型。
     *
     * @param id 单元类型 ID
     * @param param 单元类型更新入参，code 可选，未传保持原编号；包含 name、identifyCycleDays、identifyDimensionDesc
     * @return 更新后的单元类型详情
     */
    @PutMapping("/risk-control/unit-types/{id}")
    public RiskUnitTypeVO updateUnitType(@PathVariable Long id, @RequestBody RiskUnitTypeParam param) {
        return configWorkflowService.updateUnitType(id, param);
    }

    /**
     * 删除单元类型。
     *
     * @param id 单元类型 ID
     */
    @DeleteMapping("/risk-control/unit-types/{id}")
    public void deleteUnitType(@PathVariable Long id) {
        configWorkflowService.deleteUnitType(id);
    }
}
