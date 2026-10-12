package com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.job;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.entity.PersonRollCallTaskEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.service.PersonRollCallTaskService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemEndpointConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import com.guhecloud.subsystem.requester.sdk.client.PersonRollCallClient;
import com.guhecloud.subsystem.requester.sdk.param.rollcall.PersonRollCallRecordParam;
import com.guhecloud.subsystem.requester.sdk.vo.rollcall.PersonRollCallRecordVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PersonRollCallTaskJob {

    private final PersonRollCallTaskService personRollCallTaskService;

    private final SubsystemRedisUtils subsystemRedisUtils;

    private final StringRedisTemplate stringRedisTemplate;

    private final PersonRollCallClient personRollCallClient;

    /**
     * 采集点名任务记录（人员清点详情走mq订阅）
     *
     * @return
     */
    @JobAnnotation(name = "fetchingPersonRollCallTaskHandler", cron = "0 0/10 * * * ?", desc = "人员清点-采集点名任务记录")
    public void fetchingPersonRollCallTaskHandler(String param) {
        SessionContextHolder.clearSessionContext();
        var subEndpointRefreshConfigs = subsystemRedisUtils.getSubEndpointRefreshConfigs(SubsystemTypeConstants.PERSON_ROLLCALL_SERVER);
        subEndpointRefreshConfigs.forEach(config -> {
            SessionContextHolder.requestTenant(config.getTenant());
            var personRollCallTimeKey =
                    "securitymanagement:personRollCall:" + config.getServiceAddrSuffix() + ":personRollCallTime:" + config.getTenant();
            var personRollCallTime = stringRedisTemplate.opsForValue().get(personRollCallTimeKey);
            var date = DateUtils.now();
            var endTime = DateUtils.format(date, "yyyy-MM-dd HH:mm:ss.SSS");

            if (Objects.isNull(personRollCallTime)) {
                var offset = DateUtils.offset(date, ChronoUnit.DAYS, -30);
                personRollCallTime = DateUtils.format(offset, "yyyy-MM-dd HH:mm:ss.SSS");
            }

            //采集点名任务记录
            var personRollCallRecordParam = new PersonRollCallRecordParam();
            personRollCallRecordParam.setStartTime(personRollCallTime.toString());
            personRollCallRecordParam.setEndTime(endTime);
            var personRollCallRecordVOs = personRollCallClient.getRollCallRecord(personRollCallRecordParam);
            var dataList = copyToRollCallTask(personRollCallRecordVOs, config);

            personRollCallTaskService.insertOrUpdateTask(dataList);
            stringRedisTemplate.opsForValue().set(personRollCallTimeKey, endTime);
        });
    }

    private List<PersonRollCallTaskEntity> copyToRollCallTask(List<PersonRollCallRecordVO> personRollCallRecordVOs,
                                                              SubsystemEndpointConfig config
                                                             ) {
        LocalDateTime now = LocalDateTime.now();
        return personRollCallRecordVOs.stream().map(vo -> {
            Long id = IdWorker.getId();

            PersonRollCallTaskEntity personRollCallTaskEntity = PersonRollCallTaskEntity.builder()
                    .id(id)
                    .taskCode(vo.getRecordCode())
                    .taskName(vo.getTaskName())
                    .locationName(vo.getAreaName())
                    .callType(vo.getType())
                    .callStartTime(convertToCallTime(vo.getStartTime()))
                    .callEndTime(convertToCallTime(vo.getEndTime()))
                    .status(vo.getStatus())
                    .attendedCount(vo.getRollcallNum())
                    .unattendedCount(vo.getUnrollcallNum())
                    .specialCount(vo.getSpecialNum())
                    .duration(vo.getConsumingTime())
                    .thirdRecordId(vo.getRecordCode())
                    .build();
            personRollCallTaskEntity.setTenant(config.getTenant());
            personRollCallTaskEntity.setDeleted(Boolean.FALSE);
            EntityAuditor.populateAudit(personRollCallTaskEntity, "admin", StringUtils.EMPTY_STRING, ActorTypeEnum.SYSTEM, now);
            return personRollCallTaskEntity;
        }).collect(Collectors.toList());
    }

    private LocalDateTime convertToCallTime(String time) {
        if (time != null) {
            try {
                return DateUtils.parse(time);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
