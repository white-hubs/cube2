package com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.service;

import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.param.StatisticalChartParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.GetGroupControlVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.PieChartValueVO;

import java.util.List;

public interface VisualizationGroupService {
    /**
     * 群控组实时数据
     */
    GetGroupControlVO getGroupControl(StatisticalChartParam param);

    /**
     * 时控实时数据
     */
    List<PieChartValueVO> getActualTimeControl(StatisticalChartParam param);
}
