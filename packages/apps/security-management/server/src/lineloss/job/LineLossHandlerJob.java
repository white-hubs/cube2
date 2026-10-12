package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.alarm.client.SatAlarmLevelClient;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.std.param.DevicePostByIdSetParam;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.biz.linkage.client.RuleClient;
import com.guhecloud.cube.biz.linkage.client.param.RuleTriggerParam;
import com.guhecloud.cube.iot.north.bridge.client.IotDevicePropClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.KeyValueWithTsDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.MultiDevicePropParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.constants.LineLossConstants;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.dao.LineLossAlarmRuleLineMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.dao.LineLossAlarmRuleMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.dao.LineLossJobConfigMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.dao.LineLossNodeMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.dto.LineLossReadRecordDTO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.dto.LineLossRecordDTO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.dto.LineLossTriggerContentDTO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.entity.LineLossAlarmRule;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.entity.LineLossAlarmRuleLine;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.entity.LineLossConnection;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.entity.LineLossJobConfig;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.entity.LineLossNode;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service.LineLossClickHouseService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service.LineLossConnectionService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo.LineLossSupplyDetailVO;
import com.guhecloud.platform.base.context.ExecutorContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import com.guhecloud.platform.job.jobtask.cron.CronExpression;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 线损处理任务job
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LineLossHandlerJob {
    private final LineLossConnectionService lineLossConnectionService;
    private final LineLossNodeMapper lineLossNodeMapper;
    private final LineLossAlarmRuleMapper lineLossAlarmRuleMapper;
    private final LineLossAlarmRuleLineMapper lineLossAlarmRuleLineMapper;
    private final LineLossJobConfigMapper lineLossJobConfigMapper;
    private final IotDevicePropClient iotDevicePropClient;
    private final DeviceCenterStdClient deviceCenterStdClient;
    private final LineLossClickHouseService lineLossClickHouseService;
    private final RuleClient ruleClient;
    private final SatAlarmLevelClient satAlarmLevelClient;

    @JobAnnotation(name = "LineLossHandlerJob", cron = "30 */2 * * * ?", desc = "线损处理任务job")
    public void execute(String param) {
        SessionContextHolder.clearSessionContext();
        SecurityContextHolder.clearSecurityContext();
        var jobConfigList = lineLossJobConfigMapper.selectList(Wrappers.<LineLossJobConfig>lambdaQuery()
                                                                       .eq(LineLossJobConfig::getDeleted, false));
        if (CollectionUtils.isEmpty(jobConfigList)) {
            log.warn("线损处理任务租户表为空！");
            return;
        }
        var tenantMap = jobConfigList.stream().collect(Collectors.groupingBy(LineLossJobConfig::getTenant));
        tenantMap.forEach((tenant, list) -> {
            SessionContextHolder.requestTenant(tenant);
            SecurityContextHolder.setPrincipal(Principal.builder().id(1L).name("xxl").account("1").type(Principal.Type.CLIENT).build());
            this.lineLossTenantHandler(list, LocalDateTime.now());
        });
    }

    private void lineLossTenantHandler(List<LineLossJobConfig> jobConfigList, LocalDateTime now) {
        if (CollectionUtils.isEmpty(jobConfigList)) {
            log.warn("线损处理任务配置表为空！");
            return;
        }
        for (var jobConfig : jobConfigList) {
            switch (jobConfig.getSceneType()) {
                case READ_COLLECT:
                    // 读数采集
                    log.info("线损采集任务开始执行！");
                    this.readCollectHandler(jobConfig, now);
                    break;
                case LINE_LOSS_STAT:
                    // 线损统计
                    log.info("线损统计任务开始执行！");
                    this.lineLossStatisticsHandler(jobConfig, now);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 线损读数采集
     *
     * @param jobConfig 线损采集配置
     * @param now       当前时间
     */
    private void readCollectHandler(LineLossJobConfig jobConfig, LocalDateTime now) {
        if (!this.jobConfigCheck(jobConfig, now)) {
            return;
        }
        var connectionList = lineLossConnectionService.list(Wrappers.<LineLossConnection>lambdaQuery()
                                                                    .eq(LineLossConnection::getDeleted, false)
                                                                    .eq(LineLossConnection::getTenant, jobConfig.getTenant()));
        if (CollectionUtils.isEmpty(connectionList)) {
            log.warn("线损采集数据为空！");
            return;
        }

        // 获取读数
        var deviceIdSet = new HashSet<Long>();
        var multiProp = this.getLineConnectionDeviceValueMap(jobConfig.getAttributeCode(), deviceIdSet, new HashSet<>(), new HashSet<>(), connectionList);
        if (multiProp == null || multiProp.isEmpty()) {
            log.warn("线损采集数据为空！");
            return;
        }
        // 获取上次读数
        var latestList = lineLossClickHouseService.listLatestByWhereColumn(LineLossConstants.LINE_LOSS_READ_TABLE_NAME, LineLossReadRecordDTO.class, deviceIdSet, "device_id");
        var oldReadMap = Optional.ofNullable(latestList).orElse(CollectionUtils.emptyList()).stream()
                .collect(Collectors.toMap(LineLossReadRecordDTO::getDeviceId, item -> item));
        // 插入的数据集合
        var dataList = new ArrayList<LineLossReadRecordDTO>();
        var createTime = DateUtils.newLocalDateTime(now);
        multiProp.forEach((devId, list) -> {
            if (CollectionUtils.isNotEmpty(list)) {
                var deviceId = Long.parseLong(devId);
                // 上次读数对象
                var oldDto = oldReadMap.getOrDefault(deviceId, this.initReadDTO());
                // 本次采集读数
                var readValue = new BigDecimal(String.valueOf(list.getFirst().getValue()));
                // 用量
                var use = readValue.subtract(oldDto.getReadValue());
                use = BigDecimal.ZERO.compareTo(use) < 0 ? use : BigDecimal.ZERO;
                // 插入对象
                var dto = new LineLossReadRecordDTO();
                dto.setDeviceId(deviceId);
                dto.setAttributeCode(jobConfig.getAttributeCode());
                dto.setReadValue(readValue);
                dto.setCollectTime(jobConfig.getLastExecuteTime());
                dto.setLastReadValue(oldDto.getReadValue());
                dto.setLastCollectTime(oldDto.getCollectTime());
                dto.setUseValue(use);
                dto.setTenant(jobConfig.getTenant());
                dto.setCreateTime(createTime);
                dataList.add(dto);
            }
        });
        if (CollectionUtils.isNotEmpty(dataList)) {
            lineLossClickHouseService.batchInsertClickHouse(LineLossConstants.LINE_LOSS_READ_TABLE_NAME, dataList);
        }
    }

    private LineLossReadRecordDTO initReadDTO() {
        var dto = new LineLossReadRecordDTO();
        dto.setReadValue(BigDecimal.ZERO);
        dto.setUseValue(BigDecimal.ZERO);
        return dto;
    }

    /**
     * 线损统计
     *
     * @param jobConfig 线损统计配置
     * @param now       当前时间
     */
    private void lineLossStatisticsHandler(LineLossJobConfig jobConfig, LocalDateTime now) {
        // 线损统计
        if (!this.jobConfigCheck(jobConfig, now)) {
            return;
        }

        // 查询线路
        var connectionList = lineLossConnectionService.list(Wrappers.emptyWrapper());
        if (CollectionUtils.isEmpty(connectionList)) {
            log.warn("线损统计线路为空！");
            return;
        }
        // 获取最新读数
        var deviceIdSet = new HashSet<Long>();
        var nodeIdSet = new HashSet<Long>();
        var lineIdSet = new HashSet<Long>();
        var multiProp = this.getLineConnectionDeviceValueMap(jobConfig.getAttributeCode(), deviceIdSet, nodeIdSet, lineIdSet, connectionList);

        // 获取上一次读数
        var oldDataList = lineLossClickHouseService.listLatestByWhereColumn(LineLossConstants.LINE_LOSS_STAT_TABLE_NAME, LineLossRecordDTO.class, lineIdSet, "line_id");
        var oldMap = new HashMap<Long, BigDecimal>();
        Optional.ofNullable(oldDataList).orElse(CollectionUtils.emptyList()).forEach(item -> {
            oldMap.put(item.getFromDeviceId(), item.getFromReadValue());
            oldMap.put(item.getToDeviceId(), item.getToReadValue());
        });
        // 获取设备信息
        var deviceParam = new DevicePostByIdSetParam();
        deviceParam.setDeviceIdSet(deviceIdSet);
        var deviceInfoList = deviceCenterStdClient.postListByDeviceId(deviceParam);
        var deviceInfoMap = Optional.ofNullable(deviceInfoList).orElse(CollectionUtils.emptyList()).stream()
                .collect(Collectors.toMap(DeviceBaseArchivesInfoDTO::getId, item -> item, (o, n) -> o));

        // 获取节点信息
        var nodeList = lineLossNodeMapper.selectByIds(nodeIdSet);
        var nodeMap = Optional.ofNullable(nodeList).orElse(CollectionUtils.emptyList()).stream()
                .collect(Collectors.toMap(LineLossNode::getId, item -> item, (o, n) -> o));

        // 遍历线路组装线损数据
        var dataList = new ArrayList<LineLossRecordDTO>();
        var createTime = DateUtils.newLocalDateTime(now);
        var lossMap = new HashMap<Long, BigDecimal>();
        connectionList.forEach(connection -> dataList.add(this.getStatDTO(connection, nodeMap, deviceInfoMap, multiProp, oldMap, jobConfig, createTime, lossMap)));
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        lineLossClickHouseService.batchInsertClickHouse(LineLossConstants.LINE_LOSS_STAT_TABLE_NAME, dataList);

        // 异步处理(线损报警规则)
        Integer tenant = dataList.getFirst().getTenant();
        var principal = SecurityContextHolder.getPrincipal();
        ExecutorContextHolder.run(() -> {
            SecurityContextHolder.setPrincipal(principal);
            SessionContextHolder.requestTenant(tenant);
            try {
                // 更新读数
                this.updateConnectionLoss(lineIdSet, lossMap, now);
            } catch (Exception e) {
                log.error("更新读数异常！", e);
            }
            this.checkLossAlarmRule(dataList);
        });
    }

    private void updateConnectionLoss(Set<Long> lineIdSet, Map<Long, BigDecimal> lossMap, LocalDateTime now) {
        if (CollectionUtils.isEmpty(lineIdSet) || lossMap == null || lossMap.isEmpty()) {
            log.warn("更新线路线损，入参有空");
            return;
        }

        var updateReadValueList = lineLossConnectionService.listByIds(lineIdSet);
        if (CollectionUtils.isEmpty(updateReadValueList)) {
            log.warn("更新线路线损，无更新数据");
            return;
        }
        var iterator = updateReadValueList.iterator();
        while (iterator.hasNext()) {
            var connection = iterator.next();
            if (!lossMap.containsKey(connection.getId())) {
                iterator.remove();
            } else {
                connection.setLineLossRate(lossMap.get(connection.getId()));
                connection.setUpdateTime(now);
            }
        }
        if (CollectionUtils.isNotEmpty(updateReadValueList)) {
            lineLossConnectionService.updateBatchById(updateReadValueList, 100);
        }
    }

    private void checkLossAlarmRule(List<LineLossRecordDTO> dataList) {
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        var alarmRuleList = lineLossAlarmRuleMapper.selectList(Wrappers.<LineLossAlarmRule>lambdaQuery()
                                                                       .eq(LineLossAlarmRule::getEnabled, true));
        if (CollectionUtils.isEmpty(alarmRuleList)) {
            log.warn("线损报警规则为空！");
            return;
        }
        var alarmRuleMap = new HashMap<Long, LineLossAlarmRule>();
        var ruleIdSet = new HashSet<Long>();
        alarmRuleList.forEach(item -> {
            ruleIdSet.add(item.getId());
            alarmRuleMap.put(item.getId(), item);
        });

        var lossRuleLineList = lineLossAlarmRuleLineMapper.selectList(Wrappers.<LineLossAlarmRuleLine>lambdaQuery()
                                                                              .in(LineLossAlarmRuleLine::getAlarmRuleId, ruleIdSet));
        if (CollectionUtils.isEmpty(lossRuleLineList)) {
            log.warn("线损报警规则线路为空！");
            return;
        }
        /* 获取线路和节点信息 */
        var lineidSet = new HashSet<Long>();
        var nodeIdSet = new HashSet<Long>();
        dataList.forEach(loss -> {
            lineidSet.add(loss.getLineId());
            nodeIdSet.add(loss.getFromNodeId());
            nodeIdSet.add(loss.getToNodeId());
        });
        var lineList = lineLossConnectionService.list(Wrappers.<LineLossConnection>lambdaQuery()
                                                              .in(LineLossConnection::getId, lineidSet));
        var nodeList = lineLossNodeMapper.selectList(Wrappers.<LineLossNode>lambdaQuery().in(LineLossNode::getId, nodeIdSet));
        var lineMap = Optional.ofNullable(lineList).orElse(CollectionUtils.emptyList()).stream()
                .collect(Collectors.toMap(LineLossConnection::getId, item -> item, (o, n) -> o));
        var nodeMap = Optional.ofNullable(nodeList).orElse(CollectionUtils.emptyList()).stream()
                .collect(Collectors.toMap(LineLossNode::getId, item -> item, (o, n) -> o));
        /* 组装需要报警的数据对象集合 */
        var triggerDtoList = new ArrayList<LineLossTriggerContentDTO>();
        var lossLineRuleMap = lossRuleLineList.stream()
                .collect(Collectors.toMap(LineLossAlarmRuleLine::getLineId, item -> alarmRuleMap.get(item.getAlarmRuleId()), (o, n) -> o));
        var levelIdSet = new HashSet<Long>();
        for (LineLossRecordDTO loss : dataList) {
            var lossAlarmRule = lossLineRuleMap.get(loss.getLineId());
            if (lossAlarmRule == null) {
                continue;
            }
            if (this.checkRuleConditionPushAlarm(lossAlarmRule.getCondition(), loss.getLossRate())) {
                var detail = new LineLossSupplyDetailVO();
                var lineDtoList = new ArrayList<LineLossConnection>();
                lineDtoList.add(lineMap.getOrDefault(loss.getLineId(), new LineLossConnection()));
                detail.setCovertConnectionList(lineDtoList, new HashMap<>(), new HashMap<>());
                var nodeDtoList = new ArrayList<LineLossNode>();
                nodeDtoList.add(nodeMap.getOrDefault(loss.getFromNodeId(), new LineLossNode()));
                nodeDtoList.add(nodeMap.getOrDefault(loss.getToNodeId(), new LineLossNode()));
                detail.setCovertNodeList(nodeDtoList, new HashMap<>());
                levelIdSet.add(lossAlarmRule.getAlarmLevelId());
                var dto = new LineLossTriggerContentDTO();
                dto.setTriggeredRuleId(lossAlarmRule.getTriggeredRuleId());
                dto.setEvent(lossAlarmRule.getTriggeredRuleId().toString());
                dto.setEventName(lossAlarmRule.getName());
                if (CollectionUtils.isNotEmpty(lineList)) {
                    dto.setAreaName(lineList.getFirst().getLineName());
                }
                dto.setTriggerTargetKey(loss.getLineId());
                dto.setTriggerTargetCode(loss.getLineId().toString());
                dto.setTriggerTargetName(loss.getLineName());
                dto.setTriggerLevelId(lossAlarmRule.getAlarmLevelId());
                dto.setTriggerTypeId(lossAlarmRule.getEmergencyEventTypeId());
                dto.setTriggerTypeName(lossAlarmRule.getEmergencyEventTypeName());
                dto.setSnapshot(JsonUtils.serialize(detail));
                dto.setTriggerTime(DateUtils.format(loss.getCollectTime()));
                dto.setTriggerThreshold(loss.getLossRate().multiply(BigDecimal.valueOf(100)).toString());
                triggerDtoList.add(dto);
            }
        }
        if (CollectionUtils.isEmpty(triggerDtoList)) {
            return;
        }
        // 报警级别名称信息
        var levelNameMap = new HashMap<Long, String>();
        if (CollectionUtils.isNotEmpty(levelIdSet)) {
            var alarmLevelList = satAlarmLevelClient.getByIds(levelIdSet);
            Optional.ofNullable(alarmLevelList).orElse(CollectionUtils.emptyList())
                    .forEach(item -> levelNameMap.put(item.getId(), item.getName()));
        }
        // 遍历触发报警
        triggerDtoList.forEach(item -> {
            item.setTriggerLevelName(levelNameMap.get(item.getTriggerLevelId()));
            RuleTriggerParam ruleTriggerParam = new RuleTriggerParam();
            ruleTriggerParam.setTriggerTime(LocalDateTime.now());
            ruleTriggerParam.setEventConfig(item);
            ruleClient.ruleTrigger(item.getTriggeredRuleId(), ruleTriggerParam);
        });
    }

    // 线损报警规则条件判断
    private boolean checkRuleConditionPushAlarm(String lossRuleCondition, BigDecimal lossRate) {
        var pushAlarm = false;
        if (StringUtils.isBlank(lossRuleCondition)) {
            log.warn("线损报警规则条件为空！");
            return pushAlarm;
        }
        var conditions = lossRuleCondition.split(";");
        for (String condition : conditions) {
            var conditionArr = condition.split(",");
            if (conditionArr.length != 2) {
                continue;
            }
            var ruleValue = new BigDecimal(conditionArr[1]);
            switch (conditionArr[0]) {
                case ">":
                    if (lossRate.compareTo(ruleValue) > 0) {
                        pushAlarm = true;
                        break;
                    }
                case "<":
                    if (lossRate.compareTo(ruleValue) < 0) {
                        pushAlarm = true;
                    }
                    break;
                case "=":
                    if (lossRate.compareTo(ruleValue) == 0) {
                        pushAlarm = true;
                    }
                    break;
                case "!=":
                    if (lossRate.compareTo(ruleValue) != 0) {
                        pushAlarm = true;
                    }
                    break;
                case ">=":
                    if (lossRate.compareTo(ruleValue) >= 0) {
                        pushAlarm = true;
                    }
                    break;
                case "<=":
                    if (lossRate.compareTo(ruleValue) <= 0) {
                        pushAlarm = true;
                    }
                    break;
                default:
                    break;
            }
            if (pushAlarm) {
                break;
            }
        }
        return pushAlarm;
    }

    private LineLossRecordDTO getStatDTO(LineLossConnection connection, Map<Long, LineLossNode> nodeMap,
                                         Map<Long, DeviceBaseArchivesInfoDTO> deviceInfoMap,
                                         Map<String, List<KeyValueWithTsDTO>> multiProp,
                                         Map<Long, BigDecimal> oldMap, LineLossJobConfig jobConfig, LocalDateTime now,
                                         Map<Long, BigDecimal> lossMap
                                        ) {

        var fromNode = nodeMap.getOrDefault(connection.getFromNodeId(), new LineLossNode());
        var toNode = nodeMap.getOrDefault(connection.getToNodeId(), new LineLossNode());
        var fromDevice = deviceInfoMap.getOrDefault(connection.getFromDeviceId(), new DeviceBaseArchivesInfoDTO());
        var toDevice = deviceInfoMap.getOrDefault(connection.getToDeviceId(), new DeviceBaseArchivesInfoDTO());
        BigDecimal fromReadValue = BigDecimal.ZERO;
        var fromLs = multiProp.get(String.valueOf(connection.getFromDeviceId()));
        if (CollectionUtils.isNotEmpty(fromLs)) {
            var obj = fromLs.getFirst().getValue();
            if (obj != null) {
                fromReadValue = new BigDecimal(String.valueOf(obj));
            }
        }
        BigDecimal toReadValue = BigDecimal.ZERO;
        var toLs = multiProp.get(String.valueOf(connection.getToDeviceId()));
        if (CollectionUtils.isNotEmpty(toLs)) {
            var obj = toLs.getFirst().getValue();
            if (obj != null) {
                toReadValue = new BigDecimal(String.valueOf(obj));
            }
        }
        var oldFromReadValue = oldMap.getOrDefault(connection.getFromDeviceId(), BigDecimal.ZERO);
        var oldToReadValue = oldMap.getOrDefault(connection.getToDeviceId(), BigDecimal.ZERO);
        var dto = new LineLossRecordDTO();
        dto.setLineId(connection.getId());
        dto.setLineName(connection.getLineName());
        dto.setFromNodeId(connection.getFromNodeId());
        dto.setToNodeId(connection.getToNodeId());
        dto.setFromNodeName(fromNode.getName());
        dto.setToNodeName(toNode.getName());
        dto.setFromNodeLocation(fromNode.getLocation());
        dto.setToNodeLocation(toNode.getLocation());
        dto.setFromDeviceId(connection.getFromDeviceId());
        dto.setToDeviceId(connection.getToDeviceId());
        dto.setFromDeviceName(fromDevice.getName());
        dto.setToDeviceName(toDevice.getName());
        dto.setFromReadValue(fromReadValue);
        dto.setToReadValue(toReadValue);
        dto.setFromUseValue(fromReadValue.subtract(oldFromReadValue));
        dto.setToUseValue(toReadValue.subtract(oldToReadValue));
        dto.setLossValue(dto.getFromUseValue().subtract(dto.getToUseValue()));
        var lossRate = BigDecimal.ZERO;
        if (BigDecimal.ZERO.compareTo(dto.getFromUseValue()) < 0) {
            lossRate = dto.getLossValue().divide(dto.getFromUseValue(), 4, RoundingMode.HALF_UP);
        } else {
            lossRate = dto.getLossValue();
        }
        dto.setLossRate(lossRate);
        dto.setAttributeCode(jobConfig.getAttributeCode());
        dto.setCollectTime(jobConfig.getLastExecuteTime());
        dto.setTenant(jobConfig.getTenant());
        dto.setCreateTime(now);
        lossMap.put(connection.getId(), lossRate);
        return dto;
    }

    private Map<String, List<KeyValueWithTsDTO>> getLineConnectionDeviceValueMap(String attributeCode, Set<Long> connectionDeviceIdSet,
                                                                                 Set<Long> nodeIdSet, Set<Long> lineIdSet,
                                                                                 List<LineLossConnection> connectionList

                                                                                ) {
        if (CollectionUtils.isEmpty(connectionList)) {
            log.warn("线损采集线路为空！");
            return new HashMap<>();
        }
        var devIdAndPropCodesSb = new StringBuilder();
        connectionList.forEach(connection -> {
            lineIdSet.add(connection.getId());
            nodeIdSet.add(connection.getFromNodeId());
            nodeIdSet.add(connection.getToNodeId());
            if (connection.getFromDeviceId() != null) {
                connectionDeviceIdSet.add(connection.getFromDeviceId());
                devIdAndPropCodesSb.append(",").append(connection.getFromDeviceId()).append(".").append(attributeCode);
            }
            if (connection.getToDeviceId() != null) {
                connectionDeviceIdSet.add(connection.getToDeviceId());
                devIdAndPropCodesSb.append(",").append(connection.getToDeviceId()).append(".").append(attributeCode);
            }
        });
        if (devIdAndPropCodesSb.isEmpty()) {
            log.warn("线损采集设备为空！");
            return new HashMap<>();
        }
        var param = new MultiDevicePropParam();
        param.setDevIdAndPropCodes(devIdAndPropCodesSb.substring(1));
        return iotDevicePropClient.multiProp(param);
    }

    private boolean jobConfigCheck(LineLossJobConfig jobConfig, LocalDateTime now) {
        if (Boolean.FALSE.equals(jobConfig.getEnable())) {
            log.warn("线损采集配置未启用！");
            return false;
        }
        if (now.isBefore(jobConfig.getNextExecuteTime())) {
            return false;
        }
        // 计算下次执行时间并构建更新 DTO 对象
        LocalDateTime nextTime;
        try {
            CronExpression cronExpression = new CronExpression(jobConfig.getCron());
            nextTime = cronExpression.getNextValidTimeAfter(now);
        } catch (ParseException e) {
            log.error("线损采集表达式处理失败：", e);
            return false;
        }
        if (DateUtils.betweenMs(nextTime, now) > 1000 * 60 * 10) {
            jobConfig.setLastExecuteTime(DateUtils.newLocalDateTime(now));
        } else {
            jobConfig.setLastExecuteTime(DateUtils.newLocalDateTime(jobConfig.getNextExecuteTime()));
        }

        jobConfig.setNextExecuteTime(nextTime);
        jobConfig.setUpdateTime(now);
        lineLossJobConfigMapper.updateById(jobConfig);
        return true;
    }

}
