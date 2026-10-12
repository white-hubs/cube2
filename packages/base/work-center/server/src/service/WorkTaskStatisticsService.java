package com.guhecloud.cube.biz.work.center.server.service;

import com.guhecloud.cube.biz.work.center.server.vo.WorkBusinessTypeStatisticsVO;
import com.guhecloud.cube.biz.work.center.server.vo.WorkBusinessTypeVO;
import com.guhecloud.cube.biz.work.center.server.vo.WorkTaskTrendStatisticsVO;
import com.guhecloud.platform.base.model.Period;

import java.util.List;

public interface WorkTaskStatisticsService {

    /**
     * 所有工作数量
     *
     * @return
     */
    Long allWorkNumTotal(Period period);

    /**
     * 当前用户工作数量
     *
     * @return
     */
    Long workNumTotal();

    /**
     * 所有工作业务类型统计数量
     *
     * @return
     */
    List<WorkBusinessTypeStatisticsVO> allWorkBusinessTypeWorkStatistics(Period period);

    /**
     * 当前用户工作业务类型统计数量
     *
     * @return
     */
    List<WorkBusinessTypeStatisticsVO> workBusinessTypeWorkStatistics();

    /**
     * 当前用户获取工作业务类型列表
     *
     * @return
     */
    List<WorkBusinessTypeVO> workBusinessTypes();

    /**
     * 当前用户获取已办工作业务类型列表
     *
     * @return
     */
    List<WorkBusinessTypeVO> workDoneBusinessTypes();

    /**
     * 当前用户近30天工作趋势
     *
     * @return
     */
    List<WorkTaskTrendStatisticsVO> workTaskLastThirtyDaysTrendStatistics();
}
