package com.guhecloud.cube.srv.gen.property.management.server.property.service.depository;

import com.guhecloud.cube.property.management.client.common.param.StatisticalChartParam;
import com.guhecloud.cube.property.management.client.common.vo.PieChartValueVO;
import com.guhecloud.cube.property.management.client.common.vo.PropertyTypePieChartVO;
import com.guhecloud.cube.property.management.client.common.vo.StatisticalChartVO;
import com.guhecloud.cube.property.management.client.property.vo.property.OverdueVO;

import java.util.List;

public interface VisualizationPropertyService {
    /**
     * 资产报废统计
     *
     * @param param 查询时间
     *
     * @return 某个时间段内的资产到期、报废统计
     */
    OverdueVO overdue(StatisticalChartParam param);

    /**
     * 资产状态占比
     *
     * @return 资产状态占比
     */
    List<PieChartValueVO> getPropertyByStatus(StatisticalChartParam param);

    /**
     * 资产分类占比
     *
     * @param param 查询时间
     *
     * @return 资产分类占比
     */
    List<PieChartValueVO> getPropertyByType(StatisticalChartParam param);

    /**
     * 资产分类占比(可选类型)
     *
     * @param param 查询条件
     *
     * @return 资产分类占比
     */
    PropertyTypePieChartVO getPropertyByOptType(StatisticalChartParam param);

    /**
     * 部门资产分布情况
     *
     * @param param 查询时间
     *
     * @return 部门资产分布情况
     */
    StatisticalChartVO getPropertyByDepHistogram(StatisticalChartParam param);

    /**
     * 部门资产占比
     *
     * @param param 查询时间
     *
     * @return 部门资产占比
     */
    List<PieChartValueVO> getPropertyByDepPie(StatisticalChartParam param);

    /**
     * 区域资产分布占比
     *
     * @param param 查询时间
     *
     * @return 区域资产分布占比
     */
    List<PieChartValueVO> getPropertyBySourcePie(StatisticalChartParam param);
}
