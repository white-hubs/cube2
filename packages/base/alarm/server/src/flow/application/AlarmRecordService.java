package com.guhecloud.cube.base.alarm.server.flow.application;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.HandlerAlarmRecordCommand;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmHandlerResultEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmOperatorTypeEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmStateEventEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmRecordOperatorLogPO;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmRecordPO;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmStateEventOperatePO;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.mapper.AlarmRecordMapper;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmRecordService extends ServiceImpl<AlarmRecordMapper, AlarmRecordPO> {

    private final AlarmRecordOperatorLogService alarmRecordOperatorLogService;

    private final AlarmRecordHandlerUserService alarmRecordHandlerUserService;

    private final AlarmStateEventOperateService alarmStateEventOperateService;

    @Transactional(rollbackFor = Exception.class)
    public void oneClickHandlerAlarmRecords() {
        //查询当前用户未处理的报警记录主键
        List<AlarmRecordPO> alarmRecordPOS = this.baseMapper.selectUserUnConfirmedAlarmRecordList(SecurityContextHolder
                                                                                                          .getPrincipal()
                                                                                                          .getId(), null);
        if (CollectionUtils.isEmpty(alarmRecordPOS)) {
            return;
        }
        HandlerAlarmRecordCommand param = new HandlerAlarmRecordCommand();
        param.setAlarmHandlerResult(AlarmHandlerResultEnum.ALARM_IMPRACTICAL);
        param.setAlarmHandlerResultName(AlarmHandlerResultEnum.ALARM_IMPRACTICAL.getDescription());
        List<AlarmStateEventOperatePO> alarmStateEventOperatePOS = new ArrayList<>(alarmRecordPOS.size());
        List<AlarmRecordOperatorLogPO> alarmRecordOperatorLogPOS = new ArrayList<>(alarmRecordPOS.size());
        String operatorLogContent = "处理反馈:" + SecurityContextHolder.getPrincipal().getName() + "反馈了处理结果";
        LocalDateTime operatorTime = DateUtils.now();
        alarmRecordPOS.stream().forEach(alarmRecord -> {
            this.alarmRecordHandlerUserService.removeByAlarmRecordId(alarmRecord.getId(), Collections.singleton(SecurityContextHolder
                                                                                                                        .getPrincipal()
                                                                                                                        .getId()));
            //处理操作信息记录
            alarmStateEventOperatePOS.add(AlarmStateEventOperatePO.builder()
                                                  .alarmRecordId(alarmRecord.getId())
                                                  .alarmStatus(alarmRecord.getAlarmStatus())
                                                  .alarmStatusEvent(AlarmStateEventEnum.PROCESSED)
                                                  .operationParam(JsonUtils.serialize(param))
                                                  .build()
                                         );
            //记录日志消息
            alarmRecordOperatorLogPOS.add(AlarmRecordOperatorLogPO.builder()
                                                  .alarmRecordId(alarmRecord.getId())
                                                  .operatorBy(SecurityContextHolder.getPrincipal().getId()
                                                                      .toString())
                                                  .operatorByName(SecurityContextHolder.getPrincipal().getName())
                                                  .operatorTime(operatorTime)
                                                  .operatorType(AlarmOperatorTypeEnum.ALARM_HANDLER)
                                                  .operatorContent(operatorLogContent)
                                                  .build());
        });
        List<Long> recordIds = alarmRecordPOS.stream().map(AlarmRecordPO::getId).collect(Collectors.toList());
        this.alarmStateEventOperateService.saveBatch(alarmStateEventOperatePOS);
        this.alarmRecordOperatorLogService.saveBatch(alarmRecordOperatorLogPOS);
        this.baseMapper.oneClickHandlerAlarmRecords(SecurityContextHolder.getPrincipal().getId(), SecurityContextHolder
                .getPrincipal().getName(), DateUtils.now(), recordIds);
    }
}
