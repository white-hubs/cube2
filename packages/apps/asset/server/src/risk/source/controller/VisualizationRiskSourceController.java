package com.guhecloud.cube.srv.gen.property.management.server.risk.source.controller;

import com.guhecloud.cube.property.management.client.common.vo.RiskSourceTypeVO;
import com.guhecloud.cube.property.management.client.common.vo.RiskSourceVO;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.param.RiskSourceRecordParam;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.service.VisualizationRiskSourceService;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 风险源分析
 *
 * @author : lijie
 * @date : 2024-4-15
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/stat")
public class VisualizationRiskSourceController {
    private final VisualizationRiskSourceService visualizationRiskSourceService;

    /**
     * 风险源分析
     *
     * @return 资产分类占比
     */
    @GetMapping("/risk/source/archive/statistics")
    public RiskSourceVO archiveStatistics() {
        return visualizationRiskSourceService.archiveStatistics();
    }

    /**
     * 风险源建档统计
     *
     * @param param 查询条件
     *
     * @return 风险源建档情况
     */
    @PostMapping("/risk/source/record")
    @DataPermissionSwitch(dataModel = "riskSource:detailed")
    public List<RiskSourceTypeVO> riskSourceRecord(@RequestBody RiskSourceRecordParam param) {
        return visualizationRiskSourceService.riskSourceRecord(param);
    }

}
