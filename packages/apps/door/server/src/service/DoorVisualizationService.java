package com.guhecloud.cube.biz.door.server.service;

import com.guhecloud.cube.biz.door.server.param.DeviceStatusParam;
import com.guhecloud.cube.biz.door.server.param.StatisticalChartParam;
import com.guhecloud.cube.biz.door.server.vo.PieChartVO;
import com.guhecloud.cube.biz.door.server.vo.PieChartValueVO;
import com.guhecloud.cube.biz.door.server.vo.StatisticalChartVO;

import java.util.List;

public interface DoorVisualizationService {
    /**
     * 人流通行趋势
     */
    StatisticalChartVO getEventRecord(StatisticalChartParam param);

    /**
     * 人流通行趋势
     */
    StatisticalChartVO getEventResultRecord(StatisticalChartParam param);

    /**
     * 通行类型占比
     */
    PieChartVO getDoorThroughType(StatisticalChartParam param);

    /**
     * 通行汇总
     */
    PieChartVO getDoorAccessSummary(StatisticalChartParam param);

    /**
     * 设备实时状态
     */
    List<PieChartValueVO> getDeviceStatus(DeviceStatusParam param);

    /**
     * 异常事件top5
     */
    List<PieChartValueVO> getDoorAnomalousTop(StatisticalChartParam param);

}
