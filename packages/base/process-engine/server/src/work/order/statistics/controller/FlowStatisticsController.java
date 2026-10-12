package com.guhecloud.cube.base.flow.work.order.statistics.controller;

import com.guhecloud.cube.base.flow.work.order.statistics.dto.WordOrderStatisticsDTO;
import com.guhecloud.cube.base.flow.work.order.statistics.dto.WorkOrderStatisticsDTO1;
import com.guhecloud.cube.base.flow.work.order.statistics.service.FlowStatisticsService;
import com.guhecloud.cube.base.flow.work.order.statistics.vo.WordOrderStatisticsVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 工单统计
 *
 * @Author: lijie
 * @CreateTime: 2024-08-20  13:58
 * @Description:
 * @Version: 1.0
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FlowStatisticsController {
    private final FlowStatisticsService flowStatisticsService;

    /**
     * 查询个人发起工单统计
     *
     * @author: lijie
     * @date: 2024/8/20 14:59
     * @param: Pagination<WordOrderStatisticsDTO>
     * @return: [WordOrderStatisticsVO]
     **/
    @GetMapping("/getWordOrderStatisticsByUser")
    public Pagination<WordOrderStatisticsDTO> getWordOrderStatisticsByUser(WordOrderStatisticsVO parame) {
        return flowStatisticsService.getWordOrderStatisticsByUser(parame);
    }

    /**
     * 个人工单统计导出
     *
     * @author: lijie
     * @date: 2024/8/21 9:22
     * @param: void
     * @return: [WordOrderStatisticsVO]
     **/
    @GetMapping("/exportWordOrderStatisticsByUser")
    public void exportWordOrderStatisticsByUser(WordOrderStatisticsVO parame) {
        flowStatisticsService.exportWordOrderStatisticsByUser(parame);
    }

    /**
     * 工单统计
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/statistics/group/businessType")
    public Pagination<WorkOrderStatisticsDTO1> getNumberGroupByBusinessType(WordOrderStatisticsVO query) {
        return flowStatisticsService.getNumberGroupByBusinessType(query);
    }

    /**
     * 获取全部工单总数
     */
    @GetMapping("/statistics/group/businessType/total")
    public List<WorkOrderStatisticsDTO1> getNumberGroupByBusinessTypeTotal(WordOrderStatisticsVO query) {
        return flowStatisticsService.getNumberGroupByBusinessTypeTotal(query);
    }

    /**
     * 工单统计导出
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/statistics/group/businessType/export")
    public void getNumberGroupByBusinessTypeExport(WordOrderStatisticsVO query, HttpServletResponse response) {
        flowStatisticsService.getNumberGroupByBusinessTypeExport(query, response);
    }
}
