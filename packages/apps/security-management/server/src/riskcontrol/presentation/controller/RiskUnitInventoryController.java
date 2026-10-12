package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.presentation.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskUnitInventoryPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskObjectVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskUnitInventoryVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.presentation.service.RiskPresentationQueryService;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 风险单元清单接口。
 */
@RestController("securityManagementRiskUnitInventoryController")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RiskUnitInventoryController {
    private final RiskPresentationQueryService queryService;

    /**
     * 查询风险单元清单分页列表。
     *
     * @param param 查询条件，keyword 按单元名称模糊匹配，riskLevel 按现状风险等级筛选
     * @return 风险单元清单分页结果，包含区域、类型、责任人、辨识人员、安全警示标志、对象数、措施数和隐患数
     */
    @GetMapping("/risk-control/unit-inventory")
    public Pagination<RiskUnitInventoryVO> unitInventory(RiskUnitInventoryPageParam param) {
        return queryService.unitInventory(param);
    }

    /**
     * 导出风险单元清单。
     *
     * @param param 导出条件，keyword 按单元名称模糊匹配，riskLevel 按现状风险等级筛选
     * @param response HTTP 响应，用于写出 xlsx 文件流
     */
    @GetMapping("/risk-control/unit-inventory/export")
    public void exportUnitInventory(RiskUnitInventoryPageParam param, HttpServletResponse response) {
        queryService.exportUnitInventory(param, response);
    }

    /**
     * 查询单元清单展开的风险对象列表。
     *
     * @param unitId 风险单元 ID
     * @return 风险对象列表
     */
    @GetMapping("/risk-control/unit-inventory/{unitId}/risk-objects")
    public List<RiskObjectVO> unitInventoryObjects(@PathVariable Long unitId) {
        return queryService.unitInventoryObjects(unitId);
    }
}
