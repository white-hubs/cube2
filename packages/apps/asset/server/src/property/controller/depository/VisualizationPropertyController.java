package com.guhecloud.cube.srv.gen.property.management.server.property.controller.depository;

import com.guhecloud.cube.property.management.client.common.param.StatisticalChartParam;
import com.guhecloud.cube.property.management.client.common.vo.PieChartValueVO;
import com.guhecloud.cube.property.management.client.common.vo.PropertyTypePieChartVO;
import com.guhecloud.cube.property.management.client.common.vo.StatisticalChartVO;
import com.guhecloud.cube.property.management.client.property.vo.property.OverdueVO;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.depository.VisualizationPropertyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 资产总览
 *
 * @author : lijie
 * @date : 2023-4-16
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/property/visualization")
public class VisualizationPropertyController {
    private final VisualizationPropertyService visualizationPropertyService;

    /**
     * 资产报废统计
     *
     * @param param 查询时间
     *
     * @return 某个时间段内的资产到期、报废统计
     */
    @GetMapping("/overdue")
    public OverdueVO overdue(StatisticalChartParam param) {
        return visualizationPropertyService.overdue(param);
    }

    /**
     * 资产状态占比
     *
     * @param param 查询时间
     *
     * @return 资产状态占比
     */
    @GetMapping("/getPropertyByStatus")
    public List<PieChartValueVO> getPropertyByStatus(StatisticalChartParam param) {
        return visualizationPropertyService.getPropertyByStatus(param);
    }

    /**
     * 资产分类占比
     *
     * @param param 查询时间
     *
     * @return 资产分类占比
     */
    @GetMapping("/getPropertyByType")
    public List<PieChartValueVO> getPropertyByType(StatisticalChartParam param) {
        return visualizationPropertyService.getPropertyByType(param);
    }

    /**
     * 资产分类占比(可选类型)
     *
     * @param param 查询条件
     *
     * @return 资产分类占比
     */
    @GetMapping("/getPropertyByOptType")
    public PropertyTypePieChartVO getPropertyByOptType(StatisticalChartParam param) {
        return visualizationPropertyService.getPropertyByOptType(param);
    }

    /**
     * 部门资产分布情况
     *
     * @param param 查询时间
     *
     * @return 部门资产分布情况
     */
    @GetMapping("/getPropertyByDepHistogram")
    public StatisticalChartVO getPropertyByDepHistogram(StatisticalChartParam param) {
        return visualizationPropertyService.getPropertyByDepHistogram(param);
    }

    /**
     * 部门资产占比
     *
     * @param param 查询时间
     *
     * @return 部门资产占比
     */
    @GetMapping("/getPropertyByDepPie")
    public List<PieChartValueVO> getPropertyByDepPie(StatisticalChartParam param) {
        return visualizationPropertyService.getPropertyByDepPie(param);
    }

    /**
     * 区域资产分布占比
     *
     * @param param 查询时间
     *
     * @return 区域资产分布占比
     */
    @GetMapping("/getPropertyBySourcePie")
    public List<PieChartValueVO> getPropertyBySourcePie(StatisticalChartParam param) {
        return visualizationPropertyService.getPropertyBySourcePie(param);
    }
}
