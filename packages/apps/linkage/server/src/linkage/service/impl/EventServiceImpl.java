package com.guhecloud.cube.biz.linkage.server.linkage.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.apps.linkage.metadata.enumeration.ExecuteStatusEnum;
import com.guhecloud.cube.biz.linkage.server.linkage.dao.EventMapper;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.Event;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.EventAction;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.EventActionBefore;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.EventLinkage;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.Linkage;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.LinkageAction;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.LinkageActionBefore;
import com.guhecloud.cube.biz.linkage.server.linkage.param.EventQueryParam;
import com.guhecloud.cube.biz.linkage.server.linkage.service.EventActionBeforeService;
import com.guhecloud.cube.biz.linkage.server.linkage.service.EventActionService;
import com.guhecloud.cube.biz.linkage.server.linkage.service.EventLinkageService;
import com.guhecloud.cube.biz.linkage.server.linkage.service.EventService;
import com.guhecloud.cube.biz.linkage.server.linkage.service.LinkageActionBeforeService;
import com.guhecloud.cube.biz.linkage.server.linkage.service.LinkageActionService;
import com.guhecloud.cube.biz.linkage.server.linkage.service.LinkageService;
import com.guhecloud.cube.biz.linkage.server.rule.base.entity.Rule;
import com.guhecloud.cube.biz.linkage.server.rule.base.entity.RuleLinkage;
import com.guhecloud.cube.biz.linkage.server.rule.base.service.RuleLinkageService;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.redis.util.RedisUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventServiceImpl extends ServiceImpl<EventMapper, Event> implements EventService {

    private final RuleLinkageService ruleLinkageService;

    private final EventLinkageService eventLinkageService;

    private final EventActionService eventActionService;

    private final EventActionBeforeService eventActionBeforeService;

    private final LinkageService linkageService;

    private final LinkageActionService linkageActionService;

    private final LinkageActionBeforeService linkageActionBeforeService;

    @Override
    public List<Event> getEventByIotEventId(@NonNull String iotEventInstanceId) {
        return this.baseMapper.selectList(Wrappers.<Event>lambdaQuery().eq(Event::getIotEventInstanceId, iotEventInstanceId));
    }

    @Override
    public List<Event> getEventByIotEventIdAndDeviceId(String iotEventInstanceId, String deviceId) {
        return this.baseMapper.selectList(Wrappers.<Event>lambdaQuery()
                                                  .eq(Event::getIotEventInstanceId, iotEventInstanceId)
                                                  .eq(Event::getTriggerTargetId, deviceId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Event save(@NonNull Rule rule, String triggerTargetId, @NonNull LocalDateTime triggerTime, @NonNull String eventConfig,
                      String iotEventInstanceId,
                      String snapshot
                     ) {
        //规则
        var event = this.buildEvent(rule, triggerTargetId, triggerTime, eventConfig, iotEventInstanceId, snapshot);
        this.baseMapper.insert(event);
        this.saveLinkageAction(event);
        return event;
    }

    @Override
    public Event saveByEntity(Event event) {
        this.baseMapper.insert(event);
        this.saveLinkageAction(event);
        return null;
    }

    private void saveLinkageAction(Event event) {
        //规则联动信息
        var ruleLinkageEmergencies = this.ruleLinkageService.listByRuleId(event.getRuleId());
        //联动信息
        var linkageIds = ruleLinkageEmergencies.stream().map(RuleLinkage::getLinkageId).collect(Collectors.toList());
        var linkageEmergencies = this.linkageService.listByIds(linkageIds);
        //事件关联预案
        var eventLinkageEmergencies = new ArrayList<EventLinkage>();
        //事件关联动作
        var eventActions = new ArrayList<EventAction>();
        var eventActionBefore = new ArrayList<EventActionBefore>();
        var linkageActions = this.linkageActionService.listEnabledByLinkageIds(linkageIds);
        var linkageActionBeforeList = this.linkageActionBeforeService.listEnabledByLinkageIds(linkageIds);
        linkageEmergencies.forEach(linkage -> {
            var eventLinkage = buildEventLinkage(event, linkage);
            eventLinkageEmergencies.add(eventLinkage);
            //查询启用动作列表
            linkageActions.forEach(linkageEmergencyAction -> eventActions.add(buildEventAction(event, linkageEmergencyAction, eventLinkage)));
            //查询动作前置配置
            linkageActionBeforeList.forEach(linkageActionBefore -> eventActionBefore.add(buildEventActionBefore(event, linkageActionBefore, eventLinkage)));
        });
        //保存事件动作
        this.eventActionService.saveBatch(eventActions);
        //保存事件动作前置配置
        this.eventActionBeforeService.saveBatch(eventActionBefore);
        //保存事件与预案关联
        this.eventLinkageService.saveBatch(eventLinkageEmergencies);
    }

    private EventAction buildEventAction(Event event, LinkageAction linkageEmergencyAction, EventLinkage eventLinkage) {
        var eventAction = new EventAction();
        eventAction.setId(IdWorker.getId());
        eventAction.setEventId(event.getId());
        eventAction.setLinkageId(linkageEmergencyAction.getLinkageId());
        eventAction.setActionType(linkageEmergencyAction.getActionType());
        eventAction.setActionConfig(linkageEmergencyAction.getActionConfig());
        eventAction.setEventLinkageId(eventLinkage.getId());
        eventAction.setExecuteStatus(ExecuteStatusEnum.STAY_EXECUTING);//执行中
        return eventAction;
    }

    private EventActionBefore buildEventActionBefore(Event event, LinkageActionBefore linkageActionBefore, EventLinkage eventLinkage) {
        var actionBefore = new EventActionBefore();
        actionBefore.setId(IdWorker.getId());
        actionBefore.setEventId(event.getId());
        actionBefore.setLinkageId(linkageActionBefore.getLinkageId());
        actionBefore.setActionBeforeType(linkageActionBefore.getActionBeforeType());
        actionBefore.setActionBeforeConfig(linkageActionBefore.getActionBeforeConfig());
        actionBefore.setEventLinkageId(eventLinkage.getId());
        return actionBefore;
    }

    private EventLinkage buildEventLinkage(Event event, Linkage linkage) {
        var eventLinkage = new EventLinkage();
        eventLinkage.setId(IdWorker.getId());
        eventLinkage.setEventId(event.getId());
        eventLinkage.setLinkageId(linkage.getId());
        eventLinkage.setLinkageCode(linkage.getCode());
        eventLinkage.setLinkageName(linkage.getName());
        return eventLinkage;
    }

    @Override
    public Page<Event> page(EventQueryParam param) {
        return this.lambdaQuery()
                .like(StringUtils.isNotEmpty(param.getKeyword()), Event::getName, param.getKeyword())
                .eq(StringUtils.isNotEmpty(param.getApplicationCode()), Event::getApplicationCode, param.getApplicationCode())
                .eq(Objects.nonNull(param.getBusinessType()), Event::getBusinessType, param.getBusinessType())
                .ge(Objects.nonNull(param.getPeriod()) && Objects.nonNull(param.getPeriod()
                                                                                  .getBegin()), Event::getTriggerTime, ObjectUtils.ifNull(param.getPeriod(), Period::getBegin))
                .lt(Objects.nonNull(param.getPeriod()) && Objects.nonNull(param.getPeriod()
                                                                                  .getEnd()), Event::getTriggerTime, ObjectUtils.ifNull(param.getPeriod(), Period::getEnd))
                .orderByDesc(Event::getTriggerTime)
                .page(PagingUtils.convert(param));
    }

    private Event buildEvent(Rule rule, String triggerTargetId, LocalDateTime triggerTime, String eventConfig, String iotEventInstanceId,
                             String snapshot
                            ) {
        var principal = SecurityContextHolder.getPrincipal(true);
        var event = ObjectUtils.copying(rule, Event.class);
        event.setId(IdWorker.getId());
        event.setCode(String.valueOf(RedisUtils.generateIdentity()));
        event.setName(rule.getName());
        event.setRuleId(rule.getId());
        event.setRuleCode(rule.getCode());
        event.setRuleName(rule.getName());
        event.setRuleConfig(rule.getRuleConfig());
        event.setEventConfig(eventConfig);
        event.setSnapshot(snapshot);
        event.setTriggerTargetId(triggerTargetId);
        event.setIotEventInstanceId(iotEventInstanceId);
        event.setTriggerTime(triggerTime);
        event.setOperatorBy(Objects.nonNull(principal) ? principal.getAccount() : null);
        event.setOperatorName(Objects.nonNull(principal) ? principal.getName() : null);
        return event;
    }
}
