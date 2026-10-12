package com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.param.StatisticalChartParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.service.VisualizationGroupService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.GetGroupControlVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.PieChartValueVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 照明管理可视化
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VisualizationGroupController {
    private final VisualizationGroupService visualizationGroupService;

    /**
     * 群控组实时数据
     */
    @GetMapping("/visualizationGroup/getGroupControl")
    public GetGroupControlVO getGroupControl(StatisticalChartParam param) {
        return visualizationGroupService.getGroupControl(param);
    }

    /**
     * 时控实时数据
     */
    @GetMapping("/visualizationGroup/getActualTimeControl")
    public List<PieChartValueVO> getActualTimeControl(StatisticalChartParam param) {
        return visualizationGroupService.getActualTimeControl(param);

    }
}
