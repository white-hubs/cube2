package com.guhecloud.cube.srv.gen.conferencemanage.server.service;

import com.guhecloud.cube.srv.gen.conferencemanage.server.param.ConferenceStatisticsParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.BookIntervalStatisticsVo;

import java.util.List;
import java.util.Map;

public interface ConferenceStatisticsService {
    Map<String, List<BookIntervalStatisticsVo>> meetingAvailabilityNextHours();

    Map<Integer, Object> analysisScheduledTimePeriods();

    List<BookIntervalStatisticsVo> analysisConferenceRoomUtilizationRate(ConferenceStatisticsParam param);
}
