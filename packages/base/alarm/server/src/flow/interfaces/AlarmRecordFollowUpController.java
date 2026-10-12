package com.guhecloud.cube.base.alarm.server.flow.interfaces;

import com.guhecloud.cube.base.alarm.server.flow.application.AlarmRecordService;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.AlarmRecordFollowUpCommandService;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.follow.IncreaseFollowUpCommand;
import com.guhecloud.cube.base.alarm.server.flow.application.querygateway.AlarmRecordFollowUpQueryService;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmStatusEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmRecordPO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmRecordFollowUpVO;
import com.guhecloud.platform.base.standard.exception.Assert;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 报警记录跟进信息接口管理
 *
 * @author lanchun
 * @date 2024/11/25 15:32
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmRecordFollowUpController {

    private final AlarmRecordFollowUpCommandService alarmRecordFollowUpCommandService;

    private final AlarmRecordFollowUpQueryService alarmRecordFollowUpQueryService;

    private final AlarmRecordService alarmRecordService;

    /**
     * 报警记录跟进信息保存
     *
     * @param command 保存命令
     *
     * @return
     */
    @PostMapping("/alarm-record/follow-up")
    public Long saveFollowUp(@Valid @RequestBody IncreaseFollowUpCommand command) {
        AlarmRecordPO alarmRecordPO = this.alarmRecordService.getById(command.getId());
        Assert.isNotFound(alarmRecordPO, "报警记录不存在或已被删除");

        boolean canFollowUp = AlarmStatusEnum.ALARM_SUSPEND == alarmRecordPO.getAlarmStatus() ||
                              AlarmStatusEnum.ALARM_PROGRESS_CONFIRMED == alarmRecordPO.getAlarmStatus();
        Assert.isNotAcceptable(!canFollowUp, "当前状态无法新增跟进记录");
        return this.alarmRecordFollowUpCommandService.save(command);
    }

    /**
     * 获取报警记录的跟进纪录列表
     *
     * @param alarmRecordId 报警记录主键
     *
     * @return AlarmRecordFollowUpVO
     */
    @GetMapping("/alarm-record/{alarmRecordId}/follow-ups")
    public List<AlarmRecordFollowUpVO> getAlarmRecordFollowUps(@PathVariable("alarmRecordId") Long alarmRecordId) {
        return this.alarmRecordFollowUpQueryService.getAlarmRecordFollowUpByAlarmId(alarmRecordId);
    }
}
