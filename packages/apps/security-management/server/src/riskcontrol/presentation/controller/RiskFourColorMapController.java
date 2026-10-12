package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.presentation.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskFourColorUnitPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskFourColorUnitVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.vo.RiskFourColorStatisticsVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.presentation.service.RiskPresentationQueryService;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 风险四色图接口。
 */
@RestController("securityManagementRiskFourColorMapController")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RiskFourColorMapController {
    private final RiskPresentationQueryService queryService;

    /**
     * 查询四色图风险等级统计。
     *
     * @return 四色风险等级统计，包含 majorCount、largeCount、generalCount、lowCount 和 totalCount
     */
    @GetMapping("/risk-control/four-color-map-statistics")
    public RiskFourColorStatisticsVO fourColorStatistics() {
        return queryService.fourColorStatistics();
    }

    /**
     * 查询四色图风险单元分页列表。
     *
     * @param param 查询条件，keyword 按单元名称模糊匹配，riskLevel 按现状风险等级筛选
     * @return 四色图单元分页结果
     */
    @GetMapping("/risk-control/four-color-map-units")
    public Pagination<RiskFourColorUnitVO> fourColorUnits(RiskFourColorUnitPageParam param) {
        return queryService.fourColorUnits(param);
    }

    /**
     * 查询四色图单元详情。
     *
     * @param unitId 风险单元 ID
     * @return 单元详情，包含风险对象列表和清单聚合信息
     */
    @GetMapping("/risk-control/four-color-map-units/{unitId}")
    public RiskFourColorUnitVO fourColorUnitDetail(@PathVariable Long unitId) {
        return queryService.fourColorUnitDetail(unitId);
    }
}
