package com.guhecloud.cube.biz.linkage.server.rule.base.controller;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.linkage.server.facade.iot.repository.service.IotEventConsumeService;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.Event;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.EventActionBefore;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.LinkageAction;
import com.guhecloud.cube.biz.linkage.server.linkage.service.EventActionBeforeService;
import com.guhecloud.cube.biz.linkage.server.linkage.service.EventService;
import com.guhecloud.cube.biz.linkage.server.linkage.service.LinkageActionService;
import com.guhecloud.cube.biz.linkage.server.linkage.service.LinkageEventRecordService;
import com.guhecloud.cube.biz.linkage.server.linkage.service.impl.EventTriggerService;
import com.guhecloud.cube.biz.linkage.server.linkage.vo.LinkageActionVO;
import com.guhecloud.cube.biz.linkage.server.rule.base.entity.Rule;
import com.guhecloud.cube.biz.linkage.server.rule.base.entity.RuleLinkage;
import com.guhecloud.cube.biz.linkage.server.rule.base.mq.EventSnapshotUpdateMessage;
import com.guhecloud.cube.biz.linkage.server.rule.base.param.RuleCustomTriggerParam;
import com.guhecloud.cube.biz.linkage.server.rule.base.param.RuleModifyParam;
import com.guhecloud.cube.biz.linkage.server.rule.base.param.RuleTriggerParam;
import com.guhecloud.cube.biz.linkage.server.rule.base.service.RuleLinkageService;
import com.guhecloud.cube.biz.linkage.server.rule.base.service.RuleService;
import com.guhecloud.cube.biz.linkage.server.rule.base.vo.RuleBasicVO;
import com.guhecloud.cube.biz.linkage.server.rule.base.vo.RuleVO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.redis.util.RedisUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 规则保存管理接口
 *
 * @author lanchun
 * @date 2022/6/22 19:01
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RuleController {

    private final RuleService ruleService;

    private final RuleLinkageService ruleLinkageService;

    private final LinkageActionService linkageActionService;

    private final EventTriggerService eventTriggerService;

    private final EventService eventService;

    private final EventActionBeforeService eventActionBeforeService;

    private final IotEventConsumeService iotEventConsumeService;

    private final LinkageEventRecordService linkageEventRecordService;

    /**
     * 规则新增
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/rule")
    public Long save(@Valid @RequestBody RuleModifyParam param) {
        return this.ruleService.save(param);
    }

    /**
     * 规则修改
     *
     * @param id
     * @param param
     */
    @PutMapping("/rule/{id}")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody RuleModifyParam param) {
        this.ruleService.update(id, param);
    }

    /**
     * 规则详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/rule/{id}")
    public RuleVO getRule(@PathVariable("id") Long id) {
        Rule rule = this.ruleService.getById(id);
        Assert.isNotFound(rule, "规则不存在或已被删除");
        RuleVO ruleVO = new RuleVO();
        ruleVO.setRuleBasicVO(ObjectUtils.copying(rule, RuleBasicVO.class));
        ruleVO.setRuleConfig(rule.getRuleConfig());
        //查询关联的联动
        List<RuleLinkage> ruleLinkageList = this.ruleLinkageService.listByRuleId(rule.getId());
        //联动主键
        Set<Long> linkageIds = ruleLinkageList.stream().map(ruleLinkage -> ruleLinkage.getLinkageId()).collect(Collectors.toSet());
        if (!CollectionUtils.isEmpty(linkageIds)) {
            //查询动作
            List<LinkageAction> linkageActions = this.linkageActionService.listByLinkageIds(linkageIds);
            List<LinkageActionVO> linkageActionVOS = new ArrayList<>(linkageActions.size());
            linkageActions.forEach(linkageAction -> {
                LinkageActionVO linkageActionVO = ObjectUtils.copying(linkageAction, LinkageActionVO.class);
                if (!StringUtils.isEmpty(linkageAction.getActionConfig())) {
                    linkageActionVO.setActionConfig(JsonUtils.deserialize(linkageAction.getActionConfig(), Object.class));
                }
                linkageActionVOS.add(linkageActionVO);
            });
            ruleVO.setLinkageActionVOS(linkageActionVOS);
        }
        return ruleVO;
    }

    /**
     * 规则状态修改
     *
     * @param id
     * @param enabled
     */
    @PutMapping("/rule/enabled/{id}/{enabled}")
    public void updateEnabled(@PathVariable("id") Long id, @PathVariable("enabled") Boolean enabled) {
        this.ruleService.updateEnabled(id, enabled);
    }

    /**
     * 规则触发
     *
     * @param id
     * @param param
     */
    @PutMapping("/rule/trigger/{id}")
    public void triggerRule(@PathVariable("id") Long id, @Valid @RequestBody RuleTriggerParam param) {
        this.ruleService.triggerRule(id, param);
    }

    /**
     * 规则触发
     */
    @PutMapping("/rule/trigger")
    public void triggerRule(@Valid @RequestBody RuleCustomTriggerParam param) {
        var rule = ObjectUtils.copying(param, Rule.class);
        if (!rule.getEnabled()) {
            return;
        }
        var id = IdWorker.getId();
        log.info("规则触发事件,触发记录id为{},开始触发时间为:{}", id, System.currentTimeMillis());
        if (eventIfExistProcess(param, rule)) {
            return;
        }
        var redissonRedLock = RedisUtils.getDistributedLock()
                .tryLock(param.getIotEventInstanceId(), Expiration.from(10000, TimeUnit.MILLISECONDS));
        try {
            if (Objects.nonNull(redissonRedLock)) {
                //判断是否在一定周期内触发过事件
                if (eventIfExistProcess(param, rule)) {
                    return;
                }
                log.info("事件触发开始事件入库,规则id为:{},时间为{}", rule.getId().toString(), System.currentTimeMillis());
                var event = this.eventTriggerService.addEventAndTriggerVideoActionOnly(
                        rule,
                        param.getTriggerTargetId(),
                        param.getTriggerTime(),
                        Objects.requireNonNull(JsonUtils.serialize(param.getEventConfig())),
                        param.getIotEventInstanceId(),
                        JsonUtils.serialize(param.getSnapshot()));
                log.info("事件触发事件入库完成,规则id为:{},时间为{}", rule.getId().toString(), System.currentTimeMillis());
                //添加事件记录
                linkageEventRecordService.addEventRecord(event);
            }
        } catch (Exception e) {
            log.error("rule trigger iotEventInstanceId synchronize failed: {}", e.getMessage());
        } finally {
            if (Objects.nonNull(redissonRedLock)) {
                redissonRedLock.unlock();
            }
        }
    }

    private boolean eventIfExistProcess(@RequestBody @Valid RuleCustomTriggerParam param,
                                        Rule rule
                                       ) {
        var lockTriggerCount = this.eventService.count(Wrappers.<Event>lambdaQuery()
                                                               .eq(Event::getIotEventInstanceId, param.getIotEventInstanceId()));
        if (lockTriggerCount > 0) {
            log.info("事件已触发过,本次忽略，规则id为:{},时间为{}", rule.getId().toString(), System.currentTimeMillis());
            boolean consumed = sendEventSnapshotMsg(param);
            addEventSnapshot(param, consumed);
            return true;
        }
        return false;
    }

    private boolean sendEventSnapshotMsg(RuleCustomTriggerParam param) {
        try {
            List<Event> events = this.eventService.getEventByIotEventId(param.getIotEventInstanceId());
            EventSnapshotUpdateMessage message = new EventSnapshotUpdateMessage();
            message.setEventId(events.getFirst().getId());
            message.setSnapshot(JsonUtils.serialize(param.getSnapshot()));
            RocketMQContextHolder.send(EventSnapshotUpdateMessage.TOPIC, message);
            return true;
        } catch (Exception e) {
            log.error("send event snapshot msg error,iot eventInstanceId:{}", param.getIotEventInstanceId(), e);
        }
        return false;
    }

    private void addEventSnapshot(RuleCustomTriggerParam param, Boolean consumed) {
        iotEventConsumeService.save(param.getIotEventInstanceId(), JsonUtils.serialize(param.getSnapshot()), consumed);
    }

    @GetMapping("/rule/re-trigger-event/{id}")
    public void reTriggerEvent(@PathVariable("id") Long id) {
        Event event = this.eventService.getById(id);
        if (Objects.isNull(event)) {
            log.debug("事件记录不存在,id:{}", id);
            return;
        }
        //事件保存
        event.setId(IdWorker.getId());
        event.setTriggerTime(DateUtils.now());
        EntityAuditor.populateAudit(event);
        this.eventService.saveByEntity(event);
        linkageEventRecordService.addEventRecord(event);
        this.eventActionBeforeService.remove(Wrappers.<EventActionBefore>lambdaQuery()
                                                     .eq(EventActionBefore::getEventId, event.getId()));
        eventTriggerService.eventTrigger(event);
    }

    /**
     * 规则删除
     *
     * @param id
     */
    @DeleteMapping("/rule/{id}")
    public void remove(@PathVariable("id") Long id) {
        this.ruleService.remove(id);
    }

    /**
     * 规则批量删除
     *
     * @param ids
     */
    @DeleteMapping("/rule/batch")
    public void removeBatch(@RequestParam(name = "ids") @NotBlank(message = "消息主键不能为空") List<Long> ids) {
        this.ruleService.removeBatch(ids);
    }
}
