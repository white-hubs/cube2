package com.guhecloud.cube.base.dataintegrate.server.processer;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.dataintegrate.server.dao.AlarmPushConfigMapper;
import com.guhecloud.cube.base.dataintegrate.server.dao.AlarmRecordMapper;
import com.guhecloud.cube.base.dataintegrate.server.entity.AlarmPushConfig;
import com.guhecloud.cube.base.dataintegrate.server.entity.SafetyAlarmRecord;
import com.guhecloud.cube.base.dataintegrate.server.enumeration.JobHandlerEnum;
import com.guhecloud.cube.base.dataintegrate.server.model.ContextParam;
import com.guhecloud.cube.base.dataintegrate.server.rocketmq.NewAlarmMessage;
import com.guhecloud.cube.base.dataintegrate.server.service.MarketConfigService;
import com.guhecloud.cube.base.dataintegrate.server.util.HttpUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AlarmSyncProcesser extends BaseMarketDataSyncProcesser<SafetyAlarmRecord> {
    private final AlarmRecordMapper alarmRecordMapper;
    private final AlarmPushConfigMapper alarmPushConfigMapper;

    public AlarmSyncProcesser(MarketConfigService marketConfigService,
                              AlarmRecordMapper alarmRecordMapper,
                              AlarmPushConfigMapper alarmPushConfigMapper
                             ) {
        super(marketConfigService);
        this.alarmRecordMapper = alarmRecordMapper;
        this.alarmPushConfigMapper = alarmPushConfigMapper;
    }

    @Override
    public List<SafetyAlarmRecord> getSyncData(ContextParam param) {
        try {
            var startTime = System.currentTimeMillis();
            Map<String, Object> params = new HashMap<>();
            params.put("period", this.getQueryPeriod(param));
            //查询报警配置
            var alarmConfig = this.getAlarmConfig();
            if (alarmConfig != null) {//产品要求 没有告警配置的话 就不采集
                params.put("alarmRecordTypeLevels", alarmConfig);
                log.info("{} 报警采集开始", param.getMarketName());
                var alarmRecords = HttpUtils.restPost(param.getFormatUrl(JobHandlerEnum.ALARM.getDefaultPath()), params, getAuthorization(param), null, new TypeReference<List<SafetyAlarmRecord>>() {});
                if (CollectionUtils.isNotEmpty(alarmRecords)) {
                    log.info("{} 报警采集时间：{}秒 采集数据量：{} 条", param.getMarketName(),
                             (System.currentTimeMillis() - startTime) / 1000, alarmRecords.size());
                    return alarmRecords;
                }
            }
        } catch (Exception e) {
            log.error("报警数据同步异常：", e);
            throw e;
        }
        return Collections.emptyList();
    }

    private String getQueryPeriod(ContextParam param) {
        var alarmRecord = alarmRecordMapper.selectOne(Wrappers.<SafetyAlarmRecord>lambdaQuery()
                                                              .eq(SafetyAlarmRecord::getMarketTenant, param.getMarketTenant())
                                                              .orderByDesc(SafetyAlarmRecord::getUpdateTime)
                                                              .last("limit 0,1"));
        return getPeriod(param, ObjectUtils.isNotNull(alarmRecord) ? alarmRecord.getUpdateTime() : null);
    }

    @Override
    public List<SafetyAlarmRecord> cleanData(List<SafetyAlarmRecord> dataList, ContextParam param) {
        //不清洗  直接返回
        if (!CollectionUtils.isEmpty(dataList)) {
            dataList.parallelStream().forEach(data -> {
                data.setMarketName(param.getMarketName());
                data.setMarketTenant(param.getMarketTenant());
                data.setTenant(param.getTenant());
            });
        }
        return dataList;
    }

    @Override
    public void storeData(List<SafetyAlarmRecord> dataList, ContextParam param) {
        if (!CollectionUtils.isEmpty(dataList)) {
            //只能在插入前执行
            List<SafetyAlarmRecord> subNewAlarms = new ArrayList<>();
            try {
                log.error("发现新的告警处理--方法前");
                subNewAlarms = this.findNewAlarmAndTrigger(dataList);
                log.error("发现新的告警处理--方法后");
            } catch (Exception e) {
                //这里不能影响数据插入，所以不再抛出异常
                log.error("新报警对比推送失败，原因：", e);
            }
            //插入更新数据
            try {
                int batchSize = 500; // 每批500条
                int total = dataList.size();
                for (int i = 0; i < total; i += batchSize) {
                    int end = Math.min(i + batchSize, total);
                    List<SafetyAlarmRecord> subList = dataList.subList(i, end);
                    alarmRecordMapper.insertOrUpdateAlarmBatch(subList);
                }
            } catch (Exception e) {
                log.error("报警数据插入失败，原因：", e);
                throw e;//方便在xxljob上查看日志
            }
            if (!CollectionUtils.isEmpty(subNewAlarms)) {
                log.error("发现新的告警，进行报警推送");
                //发送消息
                RocketMQContextHolder.send(NewAlarmMessage.TOPIC, NewAlarmMessage.builder().dataList(subNewAlarms).build());
            }
        }
    }

    /**
     * 查询报警配置
     *
     * @return
     */
    private List<Map<String, Object>> getAlarmConfig() {
        //获取所有的报警配置
        var alarmPushConfigs = alarmPushConfigMapper.selectList(null);
        if (!CollectionUtils.isEmpty(alarmPushConfigs)) {
            var alarmConfigMap = alarmPushConfigs.stream().collect(Collectors.groupingBy(
                                                                           AlarmPushConfig::getAlarmTypeCode)
                                                                  );
            return alarmConfigMap.entrySet().stream().map(entry -> {
                Map<String, Object> map = new HashMap<>();
                map.put("alarmTypeCode", entry.getKey());
                map.put("alarmLevelQuery", entry.getValue());
                return map;
            }).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 找出新的报警，然后推送消息
     * 这个方法只能在插入之前执行，插入后就找不到新的报警了
     *
     * @param dataList
     */
    private List<SafetyAlarmRecord> findNewAlarmAndTrigger(List<SafetyAlarmRecord> dataList) {
        log.info("发现新的告警处理");
        //获取所有的报警配置
        for (SafetyAlarmRecord alarmRecord : dataList) {
            //配置了超时的报警记录
            //直接先隐藏、根据后续的推送配置进行过滤
            alarmRecord.setDisplayed(Objects.isNull(alarmRecord.getTimeoutEquipped()) ||
                                     !alarmRecord.getTimeoutEquipped() ||
                                     !ObjectUtils.isNotNull(alarmRecord.getTimeoutProcessTime()));
        }
        var alarmIds = dataList.stream().map(SafetyAlarmRecord::getId).collect(Collectors.toList());
        log.info("发现新的告警：{}", alarmIds.size());
        var existIds = alarmRecordMapper.getAllIds(alarmIds);
        log.info("发现旧的告警：{}", existIds.size());
        //找出新的报警
        var newAlarms = dataList.stream()
                .filter(a -> existIds.stream().filter(e -> e.longValue() == a.getId().longValue()).findAny().orElse(null) == null)
                .collect(Collectors.toList());
        //避免数据过多，进行一些限制
        var subNewAlarms = newAlarms.size() > 10 ? newAlarms.subList(0, 10) : newAlarms;
        log.info("发现最终新的告警：{}", subNewAlarms.size());
        return subNewAlarms;
    }
}
