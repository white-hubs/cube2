package com.guhecloud.cube.srv.gen.securitymanagement.server.passengerflow.job;

import com.guhecloud.cube.srv.gen.securitymanagement.server.passengerflow.entity.PassengerFlowRecord;
import com.guhecloud.cube.srv.gen.securitymanagement.server.passengerflow.service.PassengerFlowRecordService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import com.guhecloud.subsystem.requester.sdk.client.PassengerClient;
import com.guhecloud.subsystem.requester.sdk.param.passenger.PassengerParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PassengerJob {

    private final PassengerClient passengerClient;

    private final SubsystemRedisUtils subsystemRedisUtils;

    private final PassengerFlowRecordService passengerFlowRecordService;

    private final RedisTemplate redisTemplate;

    /**
     * 客流采集
     *
     * @param param
     *
     * @return
     */
    @JobAnnotation(name = "executePassengerRecord", cron = "0 0/5 0 * * ?", desc = "客流采集")
    public void executePassengerRecord(String param) {
        log.info("客流采集采集");
        var date = (LocalDateTime) null;
        try {
            var subsystemEndpointConfigs = this.subsystemRedisUtils.getSubEndpointRefreshConfigs(SubsystemTypeConstants.PASSENGER_SERVER);
            var st = (LocalDateTime) redisTemplate.opsForValue().get("passengerRecordTime");
            date = DateUtils.now();
            if (Objects.isNull(st)) {
                st = DateUtils.offset(date, ChronoUnit.DAYS, -7);
            }
            var passengerParam = new PassengerParam();
            passengerParam.setStTime(DateUtils.format(st, "yyyy-MM-dd HH:00:00"));
            passengerParam.setEtTime(DateUtils.format(date, "yyyy-MM-dd HH:mm:ss"));
            SessionContextHolder.requestTenant(subsystemEndpointConfigs.get(0).getTenant());
            //客流信息信息
            var passengerList = passengerClient.getPassengerList(passengerParam);
            if (CollectionUtils.isNotEmpty(passengerList)) {
                var dataList = passengerList.stream().map(p -> {
                    var pf = ObjectUtils.copying(p, PassengerFlowRecord.class);
                    pf.setTenant(SessionContextHolder.getRequester().getTenant());
                    pf.setDeleted(false);
                    EntityAuditor.populateAudit(pf, "admin", StringUtils.EMPTY_STRING, ActorTypeEnum.SYSTEM, LocalDateTime.now());
                    return pf;
                }).collect(Collectors.toList());
                passengerFlowRecordService.insertOrUpdatePassengerFlowStat(dataList);
            }
            redisTemplate.opsForValue().set("passengerRecordTime", date);
            log.info("客流采集成功============================");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("客流采集失败============================", e);
        }
        log.info("客流采集结束");
    }

}
