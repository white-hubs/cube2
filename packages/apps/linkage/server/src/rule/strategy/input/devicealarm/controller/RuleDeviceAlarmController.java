package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicealarm.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.alarm.client.SatAlarmLevelClient;
import com.guhecloud.cube.base.alarm.client.SatAlarmTypeClient;
import com.guhecloud.cube.base.alarm.client.dto.SatAlarmLevelDTO;
import com.guhecloud.cube.base.alarm.client.dto.SatAlarmTypeDTO;
import com.guhecloud.cube.base.alarm.client.param.SatAlarmTypeQueryParam;
import com.guhecloud.cube.biz.linkage.server.rule.base.entity.Rule;
import com.guhecloud.cube.biz.linkage.server.rule.base.service.RuleService;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.RuleBusinessDynamicHandler;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessEventHandlerRequest;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicealarm.entity.RuleDeviceAlarm;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicealarm.param.RuleDeviceAlarmQueryParam;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicealarm.service.RuleDeviceAlarmService;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicealarm.vo.RuleDeviceAlarmPageVO;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RuleDeviceAlarmController {

    private final RuleDeviceAlarmService ruleDeviceAlarmService;

    private final RuleService ruleService;

    private final SatAlarmLevelClient satAlarmLevelClient;

    private final SatAlarmTypeClient satAlarmTypeClient;

    private final RuleBusinessDynamicHandler ruleBusinessDynamicHandler;

    /**
     * 回调设备报警设备触发或事件触发
     */
    @PostMapping("/public/test/event-notify-callback")
    public void callBackDeviceAlarmRuleOrEvent(@RequestBody String callBackObject) {
        Map callBackRequest = JsonUtils.deserialize(callBackObject, Map.class);
        LocalDateTime triggerTime = DateUtils.parse(callBackRequest.get("timestamp").toString());
        if (callBackRequest.containsKey("ruleNum")) {
            //条件策略报警
            String ruleNum = callBackRequest.get("ruleNum").toString();
            Rule rule = this.ruleService.getByCode(ruleNum);
            if (Objects.isNull(rule) || !rule.getEnabled()) {
                return;
            }
            RuleBusinessEventHandlerRequest request = new RuleBusinessEventHandlerRequest(rule.getBusinessType());
            request.setRule(rule);
            request.setTriggerTime(triggerTime);
            request.setTriggerJson(callBackObject);
            this.ruleBusinessDynamicHandler.ruleEvent(request);
        } else if (callBackRequest.containsKey("eventCode")) {
            Long eventId = Long.valueOf(callBackRequest.get("eventCode").toString());
            //符合条件的事件
            List<RuleDeviceAlarm> ruleDeviceAlarms = ruleDeviceAlarmService.list(Wrappers.<RuleDeviceAlarm>lambdaQuery()
                                                                                         .eq(RuleDeviceAlarm::getEventId, eventId));
            if (CollectionUtils.isEmpty(ruleDeviceAlarms)) {
                return;
            }
            List<Long> ruleIds = ruleDeviceAlarms.stream().map(ruleDeviceAlarm -> ruleDeviceAlarm.getRuleId()).collect(Collectors.toList());
            //查询规则列表
            List<Rule> rules = this.ruleService.listEnabledRulesByIds(ruleIds);
            rules.parallelStream().forEach(rule -> {
                RuleBusinessEventHandlerRequest request = new RuleBusinessEventHandlerRequest(rule.getBusinessType());
                request.setRule(rule);
                request.setTriggerTime(triggerTime);
                request.setTriggerJson(callBackObject);
                this.ruleBusinessDynamicHandler.ruleEvent(request);
            });
        }
    }

    /**
     * 设备报警规则列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/rule-device-alarms")
    public Pagination<RuleDeviceAlarmPageVO> page(@Valid RuleDeviceAlarmQueryParam param) {
        Page<RuleDeviceAlarmPageVO> page = this.ruleDeviceAlarmService.page(param);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        Map<Long, SatAlarmTypeDTO> satAlarmTypeDTOS = this.satAlarmTypeClient.listAlarmTypes(new SatAlarmTypeQueryParam()).stream()
                .collect(Collectors.toMap(SatAlarmTypeDTO::getId, satAlarmTypeDTO -> satAlarmTypeDTO, (o, n) -> n));
        Map<Long, SatAlarmLevelDTO> satAlarmLevelDTOS = this.satAlarmLevelClient.listAlarmLevels().stream()
                .collect(Collectors.toMap(SatAlarmLevelDTO::getId, satAlarmLevelDTO -> satAlarmLevelDTO, (o, n) -> n));
        page.getRecords().stream().forEach(ruleDeviceAlarmPageVO -> {
            if (Objects.nonNull(ruleDeviceAlarmPageVO.getAlarmLevelId()) &&
                satAlarmLevelDTOS.containsKey(ruleDeviceAlarmPageVO.getAlarmLevelId())) {
                ruleDeviceAlarmPageVO.setAlarmLevelName(satAlarmLevelDTOS.get(ruleDeviceAlarmPageVO.getAlarmLevelId()).getName());
            }
            if (Objects.nonNull(ruleDeviceAlarmPageVO.getAlarmTypeId()) &&
                satAlarmTypeDTOS.containsKey(ruleDeviceAlarmPageVO.getAlarmTypeId())) {
                ruleDeviceAlarmPageVO.setAlarmTypeName(satAlarmTypeDTOS.get(ruleDeviceAlarmPageVO.getAlarmTypeId()).getName());
            }
        });
        return PagingUtils.convert(page);
    }

}
