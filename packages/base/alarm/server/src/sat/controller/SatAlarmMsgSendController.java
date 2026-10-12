package com.guhecloud.cube.base.alarm.server.sat.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.alarm.server.facade.emergency.dto.EmergencyRuleConfigDTO;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.AlarmRecordCommandService;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.IncreaseAlarmRecordCommand;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.IncreaseAlarmRecordTargetCommand;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmFilterScopeEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.DateUnitEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmRecordPO;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.mapper.AlarmRecordMapper;
import com.guhecloud.cube.base.alarm.server.sat.entity.SatAlarmLevel;
import com.guhecloud.cube.base.alarm.server.sat.entity.SatAlarmType;
import com.guhecloud.cube.base.alarm.server.sat.param.InitAssignAlarmRecordCommand;
import com.guhecloud.cube.base.alarm.server.sat.param.SatAlarmMsgSendParam;
import com.guhecloud.cube.base.alarm.server.sat.param.SatAlarmMsgTargetSendParam;
import com.guhecloud.cube.base.alarm.server.sat.service.SatAlarmLevelService;
import com.guhecloud.cube.base.alarm.server.sat.service.SatAlarmTypeService;
import com.guhecloud.cube.base.alarm.server.util.DateUnitUtils;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.redis.util.RedisUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonRedLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 报警消息发送接口
 *
 * @author lanchun
 * @date 2022/6/6 15:01
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SatAlarmMsgSendController {

    private final SatAlarmTypeService satAlarmTypeService;

    private final SatAlarmLevelService satAlarmLevelService;

    private final FileClient fileClient;

    private final AlarmRecordCommandService alarmRecordCommandService;

    private final AlarmRecordMapper alarmRecordMapper;

    /**
     * 报警消息发送
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/sat-alarm-msg/send")
    public Boolean sendMsg(@Valid @RequestBody SatAlarmMsgSendParam param) {
        EmergencyRuleConfigDTO emergencyRuleConfigDTO = JsonUtils
                .deserialize(param.getRuleConfig(), EmergencyRuleConfigDTO.class);
        if (Objects.isNull(emergencyRuleConfigDTO)) {
            return this.doSendMsg(param, null);
        }
        String lockKey = String.format("sat-alarm-repeat:%s:%s", emergencyRuleConfigDTO.getId(), param.getAlarmTargetKey());
        log.info("事件触发开始获取分布式锁,规则id为:{},时间为{}", emergencyRuleConfigDTO.getId(), System.currentTimeMillis());
        RedissonRedLock repeatTimeLock = RedisUtils.getDistributedLock()
                .tryLock(lockKey, Expiration.from(3000, TimeUnit.MILLISECONDS));
        if (Objects.isNull(repeatTimeLock)) {
            log.info("报警发送失败, 未获取到重复推送校验锁,规则id为:{}", emergencyRuleConfigDTO.getId());
            return Boolean.FALSE;
        }
        try {
            log.info("事件触发获取分布式锁成功,规则id为:{},时间为{}", emergencyRuleConfigDTO.getId(), System.currentTimeMillis());
            if (this.repeatTimeValid(param, emergencyRuleConfigDTO)) {
                log.info("报警发送失败, 触发时间在一定周期内已触发过");
                return Boolean.FALSE;
            }
            return this.doSendMsg(param, emergencyRuleConfigDTO);
        } finally {
            repeatTimeLock.unlock();
        }
    }

    private Boolean doSendMsg(SatAlarmMsgSendParam param, EmergencyRuleConfigDTO emergencyRuleConfigDTO) {
        //通过应用获取报警类型
        SatAlarmType satAlarmType = this.satAlarmTypeService.getByApplicationCode(param.getApplicationCode());
        Assert.isNotFound(satAlarmType, "发送失败,报警类型不存在");
        SatAlarmLevel satAlarmLevel = this.satAlarmLevelService.getById(param.getAlarmLevelId());
        Assert.isNotFound(satAlarmLevel, "发送失败,报警级别不存在");
        //报警类型图标附件
        List<FileVO> fileVOS = this.fileClient.getByBusinessKey(satAlarmType.getId());
        //报警记录入库
        IncreaseAlarmRecordCommand increaseAlarmRecordCommand = ObjectUtils.copying(param, IncreaseAlarmRecordCommand.class);
        increaseAlarmRecordCommand.setAlarmLevelCode(satAlarmLevel.getCode());
        increaseAlarmRecordCommand.setAlarmLevelName(satAlarmLevel.getName());
        increaseAlarmRecordCommand.setAlarmLevelSort(satAlarmLevel.getSort());
        increaseAlarmRecordCommand.setAlarmTypeId(satAlarmType.getId());
        increaseAlarmRecordCommand.setAlarmTypeCode(satAlarmType.getCode());
        increaseAlarmRecordCommand.setAlarmTypeName(satAlarmType.getName());
        increaseAlarmRecordCommand.setAlarmTypeFileId(!CollectionUtils.isEmpty(fileVOS) ? fileVOS.get(0).getId() : null);
        increaseAlarmRecordCommand.setTimeoutProcessTimes(param.getTimeoutProcessTimes());
        increaseAlarmRecordCommand.setTimeoutProcessTimesUnit(param.getTimeoutProcessTimesUnit());
        increaseAlarmRecordCommand.setChannelTypes(param.getChannelTypes());
        increaseAlarmRecordCommand.setRuleId(param.getRuleId());
        if (!CollectionUtils.isEmpty(param.getSendTargetParams())) {
            increaseAlarmRecordCommand.setHandlerUsers(this.buildSendTarget(param.getSendTargetParams()));
        }
        //报警规则
        if (Objects.nonNull(emergencyRuleConfigDTO)) {
            increaseAlarmRecordCommand.setFilterScope(
                    Objects.equals(Boolean.FALSE, emergencyRuleConfigDTO.getNotifyCommandDispatch()) ? AlarmFilterScopeEnum.DISPATCH
                                                                                                     : null);
            increaseAlarmRecordCommand.setTurnWarningSound(emergencyRuleConfigDTO.getTurnWarningSound());
        }
        this.alarmRecordCommandService.increaseAlarmRecord(increaseAlarmRecordCommand);
        return Boolean.TRUE;
    }

    /**
     * 报警指派
     *
     * @param initAssignAlarmRecordCommand
     */
    @PostMapping("/sat-alarm/assign")
    public void initAssignAlarmRecord(@Valid @RequestBody InitAssignAlarmRecordCommand initAssignAlarmRecordCommand) {
        initAssignAlarmRecordCommand.setHandlerUsers(buildSendTarget(initAssignAlarmRecordCommand.getAssignUsers()));
        this.alarmRecordCommandService.initAssignAlarmRecord(initAssignAlarmRecordCommand);
    }

    /**
     * 构造发送对象信息
     *
     * @param sendTargetParams
     *
     * @return
     */
    private List<IncreaseAlarmRecordTargetCommand> buildSendTarget(List<SatAlarmMsgTargetSendParam> sendTargetParams) {
        List<SatAlarmMsgTargetSendParam> disSendTargetParams = sendTargetParams.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator
                                                                            .comparing(SatAlarmMsgTargetSendParam::getUserId))), ArrayList::new)
                                                                                                );
        List<IncreaseAlarmRecordTargetCommand> msgSendTargets = new ArrayList<>();
        //构造发送对象列表
        disSendTargetParams.stream().forEach(satAlarmMsgTargetSendParam -> {
            IncreaseAlarmRecordTargetCommand alarmMsgSendTargetMessage = ObjectUtils
                    .copying(satAlarmMsgTargetSendParam, IncreaseAlarmRecordTargetCommand.class);
            msgSendTargets.add(alarmMsgSendTargetMessage);
        });
        return msgSendTargets;
    }

    /**
     * 重复推送时间校验
     *
     * @param param
     * @param rule
     *
     * @return
     */
    private boolean repeatTimeValid(SatAlarmMsgSendParam param, EmergencyRuleConfigDTO rule) {
        if (Objects.isNull(rule)) {
            return false;
        }
        LocalDateTime lastTime = DateUnitUtils.add(param.getAlarmTime(), DateUnitEnum.valueOf(rule.getRepeatTimeUnit()), -rule.getRepeatTime());
        //判断是否在一定周期内触发过事件
        var triggerCount = this.alarmRecordMapper.selectCount(Wrappers.<AlarmRecordPO>lambdaQuery()
                                                                      .eq(AlarmRecordPO::getRuleId, rule.getId())
                                                                      .eq(Objects.nonNull(param.getAlarmTargetKey()), AlarmRecordPO::getAlarmTargetKey, param.getAlarmTargetKey())
                                                                      .gt(AlarmRecordPO::getAlarmTime, lastTime)
                                                                      .le(AlarmRecordPO::getAlarmTime, param.getAlarmTime())
                                                             );
        return triggerCount > 0;
    }
}
