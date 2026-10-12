package com.guhecloud.cube.base.flow.work.order.statistics.service;

import com.guhecloud.cube.base.flow.work.order.statistics.dto.WordOrderStatisticsDTO;
import com.guhecloud.cube.base.flow.work.order.statistics.dto.WorkOrderStatisticsDTO1;
import com.guhecloud.cube.base.flow.work.order.statistics.vo.WordOrderStatisticsVO;
import com.guhecloud.platform.base.model.Pagination;

import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-08-20  14:55
 * @Description:
 * @Version: 1.0
 */
public interface FlowStatisticsService {
    Pagination<WordOrderStatisticsDTO> getWordOrderStatisticsByUser(WordOrderStatisticsVO parame);

    void exportWordOrderStatisticsByUser(WordOrderStatisticsVO parame);

    /**
     * 根据业务类型统计工单数量
     *
     * @param query
     *
     * @return
     */
    Pagination<WorkOrderStatisticsDTO1> getNumberGroupByBusinessType(WordOrderStatisticsVO query);

    /**
     * 根据业务类型统计工单数量-导出
     *
     * @param query
     * @param response
     *
     * @return
     */
    void getNumberGroupByBusinessTypeExport(WordOrderStatisticsVO query, HttpServletResponse response);

    /**
     * 根据业务类型统计工单数量-总数
     *
     * @param query
     *
     * @return
     */
    List<WorkOrderStatisticsDTO1> getNumberGroupByBusinessTypeTotal(WordOrderStatisticsVO query);
}
