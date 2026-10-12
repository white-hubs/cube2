package com.guhecloud.cube.base.alarm.server.flow.application.process.step;

import com.guhecloud.apps.linkage.metadata.enumeration.ExecuteStatusEnum;
import com.guhecloud.apps.linkage.metadata.enumeration.LinkageActionTypeEnum;
import com.guhecloud.apps.linkage.metadata.param.EventActionTriggerParam;
import com.guhecloud.cube.base.alarm.server.flow.application.process.model.ProcessContext;
import com.guhecloud.cube.base.alarm.server.flow.application.process.model.result.ProcessResult;
import com.guhecloud.cube.biz.linkage.client.EventClient;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Order(4)
@Component
public class TriggerOtherActionStep implements AlarmProcessStep {

    private final EventClient eventClient;

    private static final String STEP_NAME = "TRIGGER_OTHER_ACTION_STEP";

    public TriggerOtherActionStep(EventClient eventClient) {this.eventClient = eventClient;}

    @Override
    public ProcessResult execute(ProcessContext context) {
        var eventId = Long.valueOf(context.getAlarmRecord().getBusinessKey());
        var eventActions = eventClient.getEventActions(eventId);
        var otherActions = eventActions.stream()
                .filter(eventAction -> LinkageActionTypeEnum.VIDEO_DEVICE_ACTION != eventAction.getActionType() &&
                                       ExecuteStatusEnum.STAY_EXECUTING == eventAction.getExecuteStatus())
                .toList();
        if (CollectionUtils.isNotEmpty(otherActions)) {
            var params = otherActions.stream().map(action -> new EventActionTriggerParam(action.getId()))
                    .collect(Collectors.toList());
            eventClient.triggerEventAction(eventId, params);
        }
        return ProcessResult.continued();
    }

    @Override
    public String getName() {
        return STEP_NAME;
    }
}
