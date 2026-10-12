package com.guhecloud.cube.biz.linkage.server.linkage.strategy;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.apps.linkage.metadata.enumeration.ExecuteStatusEnum;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.Event;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.EventAction;
import com.guhecloud.cube.biz.linkage.server.linkage.service.EventActionService;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.action.Action;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.action.ActionExecute;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.action.LinkageAction;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.action.result.CallbackResult;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.dto.request.LinkageActionDetailRequest;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.dto.request.LinkageActionExecuteRequest;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventHandler {

    private final EventActionService eventActionService;

    public void executeEventActions(Event event) {
        var eventActions = eventActionService.list(Wrappers.<EventAction>lambdaQuery()
                                                           .eq(EventAction::getEventId, event.getId()));
        executeEventActions(event, eventActions);
    }

    public void executeEventActions(Event event, List<EventAction> eventActions) {
        var linkageActionRequests = new ArrayList<LinkageActionExecuteRequest>();
        eventActions.forEach(eventAction -> {
            eventAction.setExecuteStatus(ExecuteStatusEnum.IN_EXECUTION);
            var linkageActionRequest = new LinkageActionExecuteRequest(eventAction.getActionType());
            linkageActionRequest.setEvent(event);
            LinkageActionDetailRequest linkageActionDetailRequest = ObjectUtils.copying(eventAction, LinkageActionDetailRequest.class);
            linkageActionDetailRequest.setActionConfig(JsonUtils.deserialize(eventAction.getActionConfig(), Object.class));
            linkageActionRequest.setLinkageActionDetailRequest(linkageActionDetailRequest);
            linkageActionRequests.add(linkageActionRequest);
        });
        eventActionService.updateBatchById(eventActions);
        var requester = SessionContextHolder.getRequester();
        var principal = SecurityContextHolder.getPrincipal(true);
        var actionExecute = this.buildActionExecute(linkageActionRequests, requester, principal);
        actionExecute.execute();
    }

    public ActionExecute buildActionExecute(List<LinkageActionExecuteRequest> linkageActionRequests, Requester requester,
                                            Principal principal
                                           ) {
        var actions = new ArrayList<Action<CallbackResult>>();
        linkageActionRequests
                .forEach(linkageActionRequest ->
                                 actions.add(new LinkageAction<>(
                                         linkageActionRequest.getEvent(),
                                         linkageActionRequest.getLinkageActionDetailRequest().getId(),
                                         linkageActionRequest.getLinkageActionDetailRequest().getActionType(),
                                         linkageActionRequest.getLinkageActionDetailRequest().getActionConfig(),
                                         linkageActionRequest.getLinkageActionDetailRequest().getLinkageId(),
                                         requester,
                                         principal))
                        );
        return new ActionExecute(actions);
    }
}
