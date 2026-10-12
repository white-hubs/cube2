package com.guhecloud.cube.base.alarm.server.flow.application.commandgateway;

import com.guhecloud.cube.base.alarm.server.flow.application.AlarmRecordOperatorLogService;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.follow.IncreaseFollowUpCommand;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmFileSceneCodeEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmOperatorTypeEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmRecordFollowUpPO;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.mapper.AlarmRecordFollowUpMapper;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmRecordFollowUpCommandService {

    private final AlarmRecordFollowUpMapper alarmRecordFollowUpMapper;

    private final AlarmRecordOperatorLogService alarmRecordOperatorLogService;

    private final FileRefClient fileRefClient;

    private final UserClient userClient;

    @Transactional(rollbackFor = Exception.class)
    public Long save(IncreaseFollowUpCommand command) {
        UserDTO userDTO = this.userClient.getUser(SecurityContextHolder.getPrincipal().getId());
        AlarmRecordFollowUpPO alarmRecordFollowUpPO = AlarmRecordFollowUpPO
                .builder()
                .alarmRecordId(command.getId())
                .content(command.getContent())
                .followUpUserId(SecurityContextHolder.getPrincipal().getId())
                .followUpUserName(userDTO.getName())
                .followUpTime(DateUtils.now())
                .build();
        this.alarmRecordFollowUpMapper.insert(alarmRecordFollowUpPO);
        //记录日志消息
        String operatorLogContent = "提交了报警任务的跟进记录";
        this.alarmRecordOperatorLogService.save(command.getId(), AlarmOperatorTypeEnum.ALARM_FOLLOW_UP, SecurityContextHolder
                .getPrincipal().getId()
                .toString(), userDTO.getName(), DateUtils.now(), operatorLogContent, null);
        if (!CollectionUtils.isEmpty(command.getFiles())) {
            FileRefCreateParam refCreateParam = new FileRefCreateParam();
            refCreateParam.setSceneCode(AlarmFileSceneCodeEnum.ALARM_RECORD_FOLLOW_UP.getCode());
            refCreateParam.setFileIdList(new ArrayList<>(command.getFiles()));
            refCreateParam.setBusinessKey(alarmRecordFollowUpPO.getId());
            this.fileRefClient.refInduce(refCreateParam);
        }
        return alarmRecordFollowUpPO.getId();
    }
}
