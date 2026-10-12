package com.guhecloud.cube.srv.gen.conferencemanage.server.controller;

import com.guhecloud.cube.srv.gen.conferencemanage.server.param.ConferenceStatisticsParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.service.ConferenceStatisticsService;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.BookIntervalStatisticsVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 会议室统计分析
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConferenceStatisticsController {

    private final ConferenceStatisticsService conferenceStatisticsService;

    /**
     * 未来8小时会议空闲情况
     *
     * @return
     */
    @GetMapping("/meeting-availability-next-hours")
    public Map<String, List<BookIntervalStatisticsVo>> meetingAvailabilityNextHours() {
        return conferenceStatisticsService.meetingAvailabilityNextHours();
    }

    /**
     * 预定时段分析
     *
     * @return
     */
    @GetMapping("/analysis-scheduled-time-periods")
    public Map<Integer, Object> analysisScheduledTimePeriods() {
        return conferenceStatisticsService.analysisScheduledTimePeriods();
    }

    /**
     * 会议室利用率分析
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/analysis-conference-room-utilization-rate")
    public List<BookIntervalStatisticsVo> analysisConferenceRoomUtilizationRate(ConferenceStatisticsParam param) {
        return conferenceStatisticsService.analysisConferenceRoomUtilizationRate(param);
    }

}
