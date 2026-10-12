package com.guhecloud.cube.biz.linkage.server.linkage.strategy.action;

import com.guhecloud.apps.linkage.metadata.enumeration.ExecuteStatusEnum;
import com.guhecloud.apps.linkage.metadata.enumeration.LinkageActionTypeEnum;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.Event;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.LinkageActionDynamicHandler;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.action.result.CallbackResult;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.dto.request.LinkageActionDetailRequest;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.dto.request.LinkageActionExecuteRequest;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;

/**
 * 联动动作执行
 *
 * @author lanchun
 * @date 2022/10/28 11:52
 */
public class LinkageAction<T extends CallbackResult> extends Action<T> {

    protected Event event;

    protected Long eventActionId;

    protected Object actionConfig;

    protected Long linkageId;

    protected Requester requester;

    protected Principal principal;

    public LinkageAction(Event event, Long eventActionId, LinkageActionTypeEnum actionType, Object actionConfig, Long linkageId,
                         Requester requester, Principal principal
                        ) {
        super.actionType = actionType;
        super.order = actionType.getOrder();
        this.event = event;
        this.eventActionId = eventActionId;
        this.actionConfig = actionConfig;
        this.linkageId = linkageId;
        this.requester = requester;
        this.principal = principal;
    }

    @Override
    protected ExecuteStatusEnum executeAction() {
        SessionContextHolder.requestTenant(requester.getTenant());
        SecurityContextHolder.setPrincipal(principal);
        ExecuteStatusEnum executeStatus = this.run();
        this.callbackResult = new CallbackResult();
        this.callbackResult.setEventId(event.getId());
        this.callbackResult.setEventActionId(eventActionId);
        this.callbackResult.setExecuteStatus(executeStatus);
        return executeStatus;
    }

    @Override
    protected ExecuteStatusEnum fail() {
        SessionContextHolder.requestTenant(requester.getTenant());
        SecurityContextHolder.setPrincipal(principal);
        this.callbackResult = new CallbackResult();
        this.callbackResult.setEventId(event.getId());
        this.callbackResult.setEventActionId(eventActionId);
        this.callbackResult.setExecuteStatus(ExecuteStatusEnum.FAILED_EXECUTION);
        return ExecuteStatusEnum.FAILED_EXECUTION;
    }

    public ExecuteStatusEnum run() {
        LinkageActionDynamicHandler linkageActionDynamicHandler = ApplicationContextHolder.getBean(LinkageActionDynamicHandler.class);
        LinkageActionExecuteRequest request = new LinkageActionExecuteRequest(actionType);
        request.setEvent(event);
        request.setLinkageActionDetailRequest(new LinkageActionDetailRequest(eventActionId, event.getId(), event.getId(), linkageId, actionType, actionConfig));
        return linkageActionDynamicHandler.eventActionExecute(request);
    }
}
