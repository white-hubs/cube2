package com.guhecloud.cube.biz.linkage.server.linkage.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.apps.linkage.metadata.param.EventActionTriggerParam;
import com.guhecloud.apps.linkage.metadata.vo.EventActionBeforeVO;
import com.guhecloud.apps.linkage.metadata.vo.EventActionVO;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.Event;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.EventAction;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.EventActionBefore;
import com.guhecloud.cube.biz.linkage.server.linkage.param.EventQueryParam;
import com.guhecloud.cube.biz.linkage.server.linkage.service.EventActionBeforeService;
import com.guhecloud.cube.biz.linkage.server.linkage.service.EventActionService;
import com.guhecloud.cube.biz.linkage.server.linkage.service.EventService;
import com.guhecloud.cube.biz.linkage.server.linkage.service.impl.EventTriggerService;
import com.guhecloud.cube.biz.linkage.server.linkage.vo.EventPageVO;
import com.guhecloud.cube.biz.linkage.server.linkage.vo.EventVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 事件管理接口
 *
 * @author lanchun
 * @date 2022/6/29 20:06
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventController {

    private final EventService eventService;

    private final EventActionService eventActionService;

    private final EventActionBeforeService eventActionBeforeService;

    private final EventTriggerService eventTriggerService;

    /**
     * 事件分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/events")
    public Pagination<EventPageVO> page(@Valid EventQueryParam param) {
        var page = this.eventService.page(param);
        var pageVOS = ObjectUtils.copying(page.getRecords(), EventPageVO.class);
        if (CollectionUtils.isEmpty(pageVOS)) {
            return Pagination.empty();
        }
        return PagingUtils.convert(page, pageVOS);
    }

    /**
     * 事件详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/event/{id}")
    public EventVO getEvent(@PathVariable("id") Long id) {
        Event event = this.eventService.getById(id);
        Assert.isNotFound(event, "事件不存在");
        return ObjectUtils.copying(event, EventVO.class);
    }

    /**
     * 获取事件执行动作
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/events/{id}/actions")
    public List<EventActionVO> getEventActions(@PathVariable("id") Long id) {
        var eventActions = eventActionService.list(Wrappers.<EventAction>lambdaQuery()
                                                           .eq(EventAction::getEventId, id));
        if (CollectionUtils.isEmpty(eventActions)) {
            return Collections.emptyList();
        }
        return eventActions.stream().map(eventAction ->
                                                 new EventActionVO(eventAction.getId(),
                                                                   eventAction.getEventId(),
                                                                   eventAction.getLinkageId(),
                                                                   eventAction.getEventLinkageId(),
                                                                   eventAction.getActionType(),
                                                                   eventAction.getExecuteStatus(),
                                                                   eventAction.getActionConfig())
                                        ).collect(Collectors.toList());
    }

    /**
     * 获取事件执行前置配置
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/events/{id}/action/before/list")
    public List<EventActionBeforeVO> getEventActionBeforeList(@PathVariable("id") Long id) {
        var eventActionBeforeList = eventActionBeforeService.list(Wrappers.<EventActionBefore>lambdaQuery()
                                                                          .eq(EventActionBefore::getEventId, id));
        if (CollectionUtils.isEmpty(eventActionBeforeList)) {
            return Collections.emptyList();
        }
        return eventActionBeforeList.stream().map(eventActionBefore ->
                                                          new EventActionBeforeVO(eventActionBefore.getId(),
                                                                                  eventActionBefore.getEventId(),
                                                                                  eventActionBefore.getLinkageId(),
                                                                                  eventActionBefore.getEventLinkageId(),
                                                                                  eventActionBefore.getActionBeforeType(),
                                                                                  eventActionBefore.getActionBeforeConfig())
                                                 ).collect(Collectors.toList());
    }

    /**
     * 手动触发事件动作
     *
     * @param id     事件id
     * @param params 触发动作
     */
    @PostMapping("/events/{id}/action/trigger")
    public void triggerEventAction(@PathVariable("id") Long id, @Valid @RequestBody List<EventActionTriggerParam> params) {
        if (CollectionUtils.isEmpty(params)) {
            return;
        }
        Event event = this.eventService.getById(id);
        Assert.isNotFound(event, "事件不存在");
        var eventActionIds = params.stream().map(EventActionTriggerParam::getId).collect(Collectors.toSet());
        eventTriggerService.eventTriggerAction(event, eventActionService.getListByIds(eventActionIds));
    }
}
