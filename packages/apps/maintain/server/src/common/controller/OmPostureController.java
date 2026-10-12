package com.guhecloud.cube.srv.gen.maintain.server.common.controller;

import com.guhecloud.cube.srv.gen.maintain.server.common.service.OmPostureService;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.om.FaultHandleStatusStatisticsVo;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.om.FaultObjectTop5Vo;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.om.InspectRecordTrendVo;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.om.InspectTaskTop5Vo;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.om.MaintenanceStatisticsVo;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.om.TicketStatisticsVo;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.model.PeriodTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 运维态势统计
 * author: ht
 * create: 2025-06-04 14:49
 */
@RequiredArgsConstructor
@Slf4j
@RestController
public class OmPostureController {
    private final OmPostureService omPostureService;

    /**
     * 运维工单统计
     */
    @GetMapping("/om/posture/ticket-statistics")
    public TicketStatisticsVo ticketStatistics(@RequestParam Period period) {
        return omPostureService.ticketStatistics(period);
    }

    /**
     * 故障对象排行榜top5
     */
    @GetMapping("/om/posture/fault-object-top5")
    public List<FaultObjectTop5Vo> faultObjectTop5(@RequestParam Period period) {
        return omPostureService.faultObjectTop5(period);
    }

    /**
     * 故障处理状态统计
     */
    @GetMapping("/om/posture/fault-handle-status-statistics")
    public List<FaultHandleStatusStatisticsVo> faultHandleStatusStatistics(@RequestParam Period period) {
        return omPostureService.faultHandleStatusStatistics(period);

    }

    /**
     * 维保统计
     */
    @GetMapping("/om/posture/maintenance-statistics")
    public List<MaintenanceStatisticsVo> maintenanceStatistics(@RequestParam Period period, @RequestParam PeriodTypeEnum type) {
        return omPostureService.maintenanceStatistics(period, type);
    }

    /**
     * 人员巡检任务top5
     */
    @GetMapping("/om/posture/patrol-task-top5")
    public List<InspectTaskTop5Vo> patrolTaskTop5(@RequestParam Period period) {
        return omPostureService.patrolTaskTop5(period);
    }

    /**
     * 巡检记录异常趋势
     */
    @GetMapping("/om/posture/patrol-task-trend")
    public List<InspectRecordTrendVo> inspectRecordTrend(@RequestParam Period period, @RequestParam PeriodTypeEnum type) {
        return omPostureService.inspectRecordTrend(period, type);
    }
}
