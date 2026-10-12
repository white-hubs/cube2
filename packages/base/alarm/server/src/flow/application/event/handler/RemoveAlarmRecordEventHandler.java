package com.guhecloud.cube.base.alarm.server.flow.application.event.handler;

import com.guhecloud.cube.base.alarm.server.flow.domain.event.RemoveAlarmRecordEvent;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmRecordPO;
import com.guhecloud.cube.base.message2.client.MsgRecordClient;
import com.guhecloud.cube.biz.todo.client.TodoTaskClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RemoveAlarmRecordEventHandler {

    private final MsgRecordClient msgRecordClient;

    private final TodoTaskClient todoTaskClient;

    @EventListener
    public void removeAlarmRecordEventHandler(final RemoveAlarmRecordEvent event) {
        Set<String> businessIds = event.getRemoveAlarmRecords().stream().map(AlarmRecordPO::getBusinessKey).collect(Collectors.toSet());
        Set<String> ids = event.getRemoveAlarmRecords().stream().map(alarmRecord -> String.valueOf(alarmRecord.getId()))
                .collect(Collectors.toSet());
        //删除消息
        msgRecordClient.removeBatchByBusinessIds(businessIds);
        msgRecordClient.removeBatchByBusinessIds(ids);
        //删除待办
        todoTaskClient.removeBatchByBusinessIds(ids);
    }
}
