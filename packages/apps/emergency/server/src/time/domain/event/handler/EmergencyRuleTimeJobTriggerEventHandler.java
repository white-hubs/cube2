package com.guhecloud.cube.biz.emergency.server.time.domain.event.handler;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.emergency.server.rule.application.EmergencyRuleConditionDeviceService;
import com.guhecloud.cube.biz.emergency.server.rule.application.EmergencyRuleConditionExpService;
import com.guhecloud.cube.biz.emergency.server.rule.application.EmergencyRuleConditionService;
import com.guhecloud.cube.biz.emergency.server.rule.application.EmergencyRuleTimeService;
import com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration.EmergencyConditionTimeTypeEnum;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRuleConditionDevicePO;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRuleConditionExpPO;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRuleConditionGroupPO;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRuleConditionPO;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRuleTimePO;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyRuleConditionDeviceInfo;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyRuleConditionExpInfo;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyRuleConditionGroupInfo;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyRuleConditionInfo;
import com.guhecloud.cube.biz.emergency.server.rule.infrastructure.repository.mapper.EmergencyRuleConditionGroupMapper;
import com.guhecloud.cube.biz.emergency.server.time.core.eumeration.EmergencyRuleTimeExecuteStatusEnum;
import com.guhecloud.cube.biz.emergency.server.time.domain.event.EmergencyRuleTimeJobTriggerEvent;
import com.guhecloud.cube.biz.emergency.server.time.domain.event.IssuanceIotRuleGroupEvent;
import com.guhecloud.cube.biz.emergency.server.time.domain.po.EmergencyRuleTimeJobPO;
import com.guhecloud.cube.biz.emergency.server.time.service.EmergencyRuleTimeJobLogService;
import com.guhecloud.cube.biz.emergency.server.time.service.EmergencyRuleTimeJobService;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.job.jobtask.cron.CronExpression;
import com.guhecloud.platform.redis.util.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonRedLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyRuleTimeJobTriggerEventHandler {

    private final EmergencyRuleTimeService emergencyRuleTimeService;

    private final EmergencyRuleConditionGroupMapper emergencyRuleConditionGroupMapper;

    private final EmergencyRuleTimeJobService emergencyRuleTimeJobService;

    private final EmergencyRuleConditionService emergencyRuleConditionService;

    private final EmergencyRuleConditionExpService emergencyRuleConditionExpService;

    private final EmergencyRuleConditionDeviceService emergencyRuleConditionDeviceService;

    private final EmergencyRuleTimeJobLogService emergencyRuleTimeJobLogService;

    private final ApplicationEventPublisher applicationEventPublisher;

    @EventListener
    public void triggerJob(EmergencyRuleTimeJobTriggerEvent event) {
        RedissonRedLock redissonRedLock = RedisUtils.getDistributedLock()
                .tryLock(event.getTimerTask().getId().toString(), Expiration.from(5000, TimeUnit.MILLISECONDS));
        try {
            if (Objects.nonNull(redissonRedLock)) {
                EmergencyRuleTimeJobPO emergencyRuleTimeJob = this.emergencyRuleTimeJobService.getById(event.getTimerTask().getId());
                if (EmergencyConditionTimeTypeEnum.ALL != emergencyRuleTimeJob.getTimeType()) {
                    LocalDateTime nextValidTimeAfter = new CronExpression(emergencyRuleTimeJob.getCronExpression()).getNextValidTimeAfter(DateUtils.now());
                    emergencyRuleTimeJob.setTriggerLastTime(emergencyRuleTimeJob.getTriggerNextTime());
                    emergencyRuleTimeJob.setTriggerNextTime(DateUtils.toTimestamp(nextValidTimeAfter));
                }
                this.emergencyRuleTimeJobService.updateById(emergencyRuleTimeJob);
                distributeRule(emergencyRuleTimeJob);
                this.emergencyRuleTimeJobLogService.updateLog(event.getJobLogId(), EmergencyRuleTimeExecuteStatusEnum.PROCESS_SUCCESS, StringUtils.EMPTY_STRING);
            }
        } catch (Exception e) {
            this.emergencyRuleTimeJobLogService.updateLog(event.getJobLogId(), EmergencyRuleTimeExecuteStatusEnum.PROCESS_FAIL, e.getMessage());
            log.error("执行报警时段设值job失败,jobId:为{},失败原因{}", event.getTimerTask().getId(), e);
        } finally {
            if (Objects.nonNull(redissonRedLock)) {
                redissonRedLock.unlock();
            }
        }
    }

    public void distributeRule(EmergencyRuleTimeJobPO emergencyRuleTimeJob) {
        EmergencyRuleTimePO emergencyRuleTimePO = this.emergencyRuleTimeService.getById(emergencyRuleTimeJob.getEmergencyRuleTimeId());
        EmergencyRuleConditionGroupPO emergencyRuleConditionGroupPO = this.emergencyRuleConditionGroupMapper.selectById(emergencyRuleTimePO.getGroupId());
        List<EmergencyRuleConditionPO> emergencyRuleConditionPOS = this.emergencyRuleConditionService.list(Wrappers
                                                                                                                   .<EmergencyRuleConditionPO>lambdaQuery()
                                                                                                                   .eq(EmergencyRuleConditionPO::getGroupId, emergencyRuleConditionGroupPO.getId()));
        List<EmergencyRuleConditionDevicePO> emergencyRuleConditionDevicePOS = this.emergencyRuleConditionDeviceService.list(Wrappers
                                                                                                                                     .<EmergencyRuleConditionDevicePO>lambdaQuery()
                                                                                                                                     .eq(EmergencyRuleConditionDevicePO::getEmergencyRuleId, emergencyRuleConditionGroupPO.getEmergencyRuleId()));
        //查询关联的规则条件表达式
        Map<Long, List<EmergencyRuleConditionExpPO>> emergencyRuleConditionExpPOS = this.emergencyRuleConditionExpService
                .list(Wrappers.<EmergencyRuleConditionExpPO>lambdaQuery()
                              .eq(EmergencyRuleConditionExpPO::getGroupId, emergencyRuleConditionGroupPO.getId()))
                .stream().collect(Collectors.groupingBy(EmergencyRuleConditionExpPO::getEmergencyRuleConditionId));
        List<EmergencyRuleConditionInfo> emergencyRuleConditionInfos = emergencyRuleConditionPOS.stream().map(emergencyRuleConditionPO -> {
            EmergencyRuleConditionInfo emergencyRuleConditionInfo = getEmergencyRuleConditionInfo(emergencyRuleConditionPO);
            if (emergencyRuleConditionExpPOS.containsKey(emergencyRuleConditionPO.getId())) {
                emergencyRuleConditionInfo.settingEmergencyRuleConditionExps(ObjectUtils.copying(emergencyRuleConditionExpPOS.get(emergencyRuleConditionPO.getId()), EmergencyRuleConditionExpInfo.class));
            }
            return emergencyRuleConditionInfo;
        }).collect(Collectors.toList());
        EmergencyRuleConditionGroupInfo emergencyRuleConditionGroupInfo = new EmergencyRuleConditionGroupInfo(emergencyRuleConditionGroupPO.getId(), emergencyRuleConditionInfos, null);
        this.applicationEventPublisher.publishEvent(new IssuanceIotRuleGroupEvent(this,
                                                                                  emergencyRuleConditionGroupPO.getId().toString(),
                                                                                  emergencyRuleTimeJob.getOperateType(),
                                                                                  emergencyRuleConditionGroupInfo,
                                                                                  ObjectUtils.copying(emergencyRuleConditionDevicePOS, EmergencyRuleConditionDeviceInfo.class),
                                                                                  emergencyRuleTimeJob));
    }

    private EmergencyRuleConditionInfo getEmergencyRuleConditionInfo(EmergencyRuleConditionPO emergencyRuleConditionPO) {
        return new EmergencyRuleConditionInfo(
                emergencyRuleConditionPO.getGroupId(),
                emergencyRuleConditionPO.getDeviceProdCategoryCode(),
                emergencyRuleConditionPO.getDeviceProdCategoryCodeConcat(),
                emergencyRuleConditionPO.getDeviceProdCategoryName(),
                emergencyRuleConditionPO.getDeviceProdId(),
                emergencyRuleConditionPO.getDeviceProdName(),
                emergencyRuleConditionPO.getConditionType(),
                emergencyRuleConditionPO.getDeviceEventId(),
                emergencyRuleConditionPO.getDeviceEventCode(),
                emergencyRuleConditionPO.getDeviceEventName(),
                emergencyRuleConditionPO.getDeviceEventType(),
                emergencyRuleConditionPO.getLogicalOp(),
                emergencyRuleConditionPO.getEventConfigType(),
                null
        );
    }
}
