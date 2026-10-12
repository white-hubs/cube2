package com.guhecloud.cube.srv.gen.energymanagement.server.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.KeyValueWithTsDTO;
import com.guhecloud.cube.srv.gen.energymanagement.server.adapter.servcieadapter.AdapterAlarmService;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyCollectConfig;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.MeterDevice;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.AttributeTypeEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyCollectConfigService;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.MeterDeviceService;
import com.guhecloud.cube.srv.gen.energymanagement.server.util.DeviceAttribute;
import com.guhecloud.cube.srv.gen.energymanagement.server.util.InfluxdbUtil;
import com.guhecloud.cube.srv.gen.energymanagement.server.util.MeterUsageCalculator;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.ReadMeterLogVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.write.Point;
import com.xxl.job.core.biz.model.ReturnT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 本任务 主要负责抄表
 */

@Slf4j
@Component
public class EnergyMonitorJob {

    @Value("${influx.dbNameEnergy}")
    private String dbName;

    @Value("${influx.dborg}")
    private String dborg;

    @Autowired
    private InfluxDBClient influxdbClient;
    @Autowired
    private MeterDeviceService meterDeviceService;
    @Autowired
    private AdapterAlarmService adapterAlarmService;
    @Autowired
    private EnergyCollectConfigService energyCollectConfigService;

    /**
     * 能耗抄表
     * MeterReadeCycleServiceImpl.initRegistryJob 注册的这个job所以不用添加注解
     */
    public ReturnT<String> executeReadeDevice(String param) {
        try {
            log.info("抄表开始：==================================================================================");
            //当前时间
            LocalDateTime currentTime = LocalDateTime.now()
                    .withSecond(0)
                    .withNano(0);
            int tenant = Integer.parseInt(param);
            SessionContextHolder.requestTenant(tenant);
            //查询开启的仪表类型
            List<EnergyCollectConfig> enabledList = energyCollectConfigService.list(Wrappers.<EnergyCollectConfig>lambdaQuery()
                                                                                            .eq(EnergyCollectConfig::getCollectOn, true));
            List<String> categoryCodeList = enabledList.stream().map(EnergyCollectConfig::getCategoryCode).collect(Collectors.toList());
            Map<String, EnergyCollectConfig> collectConfigMap = enabledList.stream()
                    .collect(Collectors.toMap(EnergyCollectConfig::getCategoryCode, n -> n, (oldValue, newValue) -> oldValue));
            //通过仪表类型 过滤开启采集的设备类型
            List<MeterDevice> deviceList = meterDeviceService.list(Wrappers.<MeterDevice>lambdaQuery()
                                                                           .eq(MeterDevice::getTenant, tenant)
                                                                           .in(CollectionUtils.isNotEmpty(categoryCodeList), MeterDevice::getInstrumentType, categoryCodeList));

            if (CollectionUtils.isNotEmpty(deviceList)) {
                deviceList.forEach(device -> {
                    try {
                        //抄表日志记录
                        ReadMeterLogVO readMeterLogVO = this.handleMeterReadLog(device, currentTime, collectConfigMap);
                        if (readMeterLogVO != null) {
                            //更新设备上的数据
                            this.updateEnergyDataForDevice(device, readMeterLogVO);
                            //推送报警
                            this.adapterAlarmService.pushAlarm(device, device.getReadeValue(), readMeterLogVO.getReadValue());
                        }
                    } catch (Exception e) {
                        log.error("设备：" + device.getDeviceId() + " 抄表异常：", e);
                    }
                });
            }
            log.info("抄表结束：==================================================================================");
        } catch (Exception e) {
            log.error("能耗抄表,失败原因:", e);
            return ReturnT.FAIL;
        }
        return ReturnT.SUCCESS;

    }

    /**
     * 从物联网获取设备的最新读数
     * 计算本期的使用量并保存
     *
     * @param device      设备
     * @param currentTime 当前时间
     */
    private ReadMeterLogVO handleMeterReadLog(MeterDevice device,
                                              LocalDateTime currentTime,
                                              Map<String, EnergyCollectConfig> collectConfigMap
                                             ) {
        SessionContextHolder.requestTenant(device.getTenant());
        //通过仪表类型 找到对应的物联网属性
        String propertyCode = AttributeTypeEnum.getByDesc(device.getInstrumentType());
        //通过物联网 获取设备的属性值
        List<KeyValueWithTsDTO> keyValueWithTsDTOS = DeviceAttribute.deviceProp(device.getDeviceId(), AttributeTypeEnum.getByDesc(device.getInstrumentType()));
        KeyValueWithTsDTO keyValueWithTsDTO = keyValueWithTsDTOS.stream()
                .filter(attributeRunningDTO -> propertyCode.equals(attributeRunningDTO.getKey())).findFirst().orElse(null);
        if (keyValueWithTsDTO != null && keyValueWithTsDTO.getValue() != null &&
            InfluxdbUtil.isNumeric(keyValueWithTsDTO.getValue().toString())) {
            //本期读数
            BigDecimal readeValue = new BigDecimal(keyValueWithTsDTO.getValue().toString());
            //处理数据
            String influxdbQuery = InfluxdbUtil.getLatestReadMeterLogTableQuery(device.getDeviceId());
            //查询库中最新的记录
            ReadMeterLogVO preReadMeterLog = InfluxdbUtil.execQueryForT(influxdbQuery, influxdbClient, ReadMeterLogVO.class);
            //构建本次插入的记录
            ReadMeterLogVO currentReadMeterLogVO = this.buildReadMeterLog(device, preReadMeterLog, readeValue, currentTime,
                                                                          collectConfigMap.get(device.getInstrumentType()));

            Point pointForMeterLog = currentReadMeterLogVO.createPointForMeterLog(currentReadMeterLogVO);
            WriteApiBlocking writeApiBlocking = influxdbClient.getWriteApiBlocking();
            writeApiBlocking.writePoint(dbName, dborg, pointForMeterLog);
            return currentReadMeterLogVO;
        }
        return null;
    }

    /**
     * 更新设备上的能耗相关数据
     *
     * @param device         设备
     * @param readMeterLogVO 读数记录
     */
    private void updateEnergyDataForDevice(MeterDevice device,
                                           ReadMeterLogVO readMeterLogVO
                                          ) {
        //构建仪表实体
        device.setLastReadeTime(readMeterLogVO.getPreDateTime());
        //本期抄表时间
        device.setNowReadeTime(readMeterLogVO.getCurDateTime());
        //上次读数
        device.setLastReadeValue(readMeterLogVO.getPreReadValue());
        //本次用量
        device.setNowReadeValue(readMeterLogVO.getUsage());
        //本期读数
        device.setReadeValue(readMeterLogVO.getReadValue());
        //初始读数 定义为首次抄表 读到的数据
        if (readMeterLogVO.getPreDateTime() == null || readMeterLogVO.getPreReadValue() == null) {
            device.setInitValue(readMeterLogVO.getReadValue());
        }
        meterDeviceService.updateById(device);
    }

    /**
     * 构建本次的抄表数据
     *
     * @param device          设备
     * @param preReadMeterLog 上一次读数记录
     * @param readeValue      当前读数
     * @param currentTime     当前时间
     *
     * @return 新读数记录
     */
    private ReadMeterLogVO buildReadMeterLog(MeterDevice device,
                                             ReadMeterLogVO preReadMeterLog,
                                             BigDecimal readeValue,
                                             LocalDateTime currentTime,
                                             EnergyCollectConfig collectConfig
                                            ) {
        ReadMeterLogVO readMeterLogVO = null;
        if (preReadMeterLog != null) {
            MeterUsageCalculator.Result usageResult = MeterUsageCalculator.calculate(preReadMeterLog.getReadValue(), readeValue,
                                                                                    getRolloverValue(collectConfig), getMaxStepUsage(collectConfig));
            if (usageResult.isAbnormal()) {
                log.warn("能耗抄表用量异常，deviceId={}, instrumentType={}, previous={}, current={}, rolloverValue={}, maxStepUsage={}, reason={}",
                         device.getDeviceId(), device.getInstrumentType(), preReadMeterLog.getReadValue(), readeValue,
                         getRolloverValue(collectConfig), getMaxStepUsage(collectConfig), usageResult.getReason());
            }
            //处理数据
            readMeterLogVO = ReadMeterLogVO.builder()
                    .instrumentType(device.getInstrumentType())
                    .preTime(preReadMeterLog.getCurTime())
                    .preReadValue(preReadMeterLog.getReadValue())
                    .curTime(DateUtils.toTimestamp(currentTime))
                    .readValue(readeValue)
                    .usage(usageResult.getUsage().setScale(2, RoundingMode.HALF_UP))
                    .deviceId(device.getDeviceId())
                    .deviceCode(device.getCode())
                    .deviceName(device.getDeviceName())
                    .areaId(device.getDeviceAreaId())
                    .tenant(device.getTenant().toString())
                    .time(currentTime.atZone(ZoneId.systemDefault()).toInstant())
                    .build();
        } else {
            readMeterLogVO = ReadMeterLogVO.builder()
                    .instrumentType(device.getInstrumentType())
                    .curTime(DateUtils.toTimestamp(currentTime))
                    .readValue(readeValue)
                    .deviceId(device.getDeviceId())
                    .deviceCode(device.getCode())
                    .deviceName(device.getDeviceName())
                    .areaId(device.getDeviceAreaId())
                    .tenant(device.getTenant().toString())
                    .time(currentTime.atZone(ZoneId.systemDefault()).toInstant())
                    .build();
        }
        return readMeterLogVO;
    }

    private BigDecimal getRolloverValue(EnergyCollectConfig collectConfig) {
        return collectConfig == null ? null : collectConfig.getRolloverValue();
    }

    private BigDecimal getMaxStepUsage(EnergyCollectConfig collectConfig) {
        return collectConfig == null ? null : collectConfig.getMaxStepUsage();
    }

}
