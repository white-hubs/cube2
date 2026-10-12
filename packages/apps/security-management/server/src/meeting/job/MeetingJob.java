package com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.job;

import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.entity.MeetingApplyEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.entity.MeetingEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.service.MeetingApplyService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.service.MeetingService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MeetingJob {

    private final MeetingApplyService meetingApplyService;

    private final MeetingService meetingService;

    private final SubsystemRedisUtils subsystemRedisUtils;

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 采集会见申请记录
     *
     * @return
     */
    public void fetchingMeetingApplyHandler(String param) {
        var subEndpointRefreshConfigs = subsystemRedisUtils.getSubEndpointRefreshConfigs("meetingServer");
        subEndpointRefreshConfigs.forEach(config -> {
            SessionContextHolder.requestTenant(config.getTenant());
            var programScheduleTimeKey =
                    "securitymanagement:meeting:" + config.getServiceAddrSuffix() + ":meetingApplyTime:" + config.getTenant();
            var meetingApplyTime = stringRedisTemplate.opsForValue().get(programScheduleTimeKey);
            var date = DateUtils.now();
            var endTime = DateUtils.format(date, "yyyy-MM-dd");

            if (Objects.isNull(meetingApplyTime)) {
                var offset = DateUtils.offset(date, ChronoUnit.DAYS, -30);
                meetingApplyTime = DateUtils.format(offset, "yyyy-MM-dd");
            }

            //采集会见申请记录
            List<MeetingApplyEntity> dataList = new ArrayList<>();

            meetingApplyService.insertOrUpdateMeetingApply(dataList);

            stringRedisTemplate.opsForValue().set(programScheduleTimeKey, endTime);
        });
    }

    /**
     * 采集会见记录
     *
     * @return
     */
    public void fetchingMeetingHandler(String param) {
        var subEndpointRefreshConfigs = subsystemRedisUtils.getSubEndpointRefreshConfigs("meetingServer");
        subEndpointRefreshConfigs.forEach(config -> {
            SessionContextHolder.requestTenant(config.getTenant());
            var programScheduleTimeKey =
                    "securitymanagement:meeting:" + config.getServiceAddrSuffix() + ":meetingTime:" + config.getTenant();
            var meetingTime = stringRedisTemplate.opsForValue().get(programScheduleTimeKey);
            var date = DateUtils.now();
            var endTime = DateUtils.format(date, "yyyy-MM-dd");

            if (Objects.isNull(meetingTime)) {
                var offset = DateUtils.offset(date, ChronoUnit.DAYS, -30);
                meetingTime = DateUtils.format(offset, "yyyy-MM-dd");
            }
            List<MeetingEntity> dataList = new ArrayList<>();

            meetingService.insertOrUpdateMeeting(dataList);

            stringRedisTemplate.opsForValue().set(programScheduleTimeKey, endTime);
        });
    }
}
