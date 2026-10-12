package com.guhecloud.cube.srv.gen.energymanagement.server.job;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.KeyValueWithTsDTO;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyCollectConfig;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.MeterDevice;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.AttributeTypeEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.DateEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.InstrumentTypeEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.AreaProjectService;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyCollectConfigService;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyCollectPeriodService;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyProjectService;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.MeterDeviceService;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.RouteProjectService;
import com.guhecloud.cube.srv.gen.energymanagement.server.util.DeviceAttribute;
import com.guhecloud.cube.srv.gen.energymanagement.server.util.InfluxdbUtil;
import com.guhecloud.cube.srv.gen.energymanagement.server.util.MeterUsageCalculator;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.DataVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.DeviceAreaVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.DeviceCollectDataTmpVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.DeviceProjectVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.RouteDeviceVo;
import com.guhecloud.platform.base.context.ExecutorContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import com.influxdb.client.DeleteApi;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.guhecloud.platform.base.constants.DateFormat.DEFAULT_DATE_FORMAT;

@Slf4j
@Component
public class EnergyCollectJob {

    @Value("${influx.dbNameEnergy}")
    private String dbName;

    @Value("${influx.dborg}")
    private String dborg;

    @Value("${energy.weatherUrl}")
    private String weatherUrl;

    @Value("${energy.province}")
    private String province;

    @Autowired
    private InfluxDBClient influxdbClient;

    @Autowired
    private MeterDeviceService meterDeviceService;

    @Autowired
    private EnergyProjectService energyProjectService;

    @Autowired
    private AreaProjectService areaProjectService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RouteProjectService routeProjectService;

    //    @Autowired
    //    private EnergyChargeConfigService energyChargeConfigService;

    @Autowired
    private EnergyCollectPeriodService energyCollectPeriodService;

    @Autowired
    private EnergyCollectConfigService energyCollectConfigService;

    @Autowired
    private EnergyJobParamHandler energyJobParamHandler;

    /**
     * 能耗基础数据采集
     */
    @JobAnnotation(name = "newEnergy", cron = "0 0/10 * * * ?", desc = "能耗-数据基础采集")
    public void execute(String param) {
        var variable = StringUtils.isBlank(param) ? energyJobParamHandler.getJobParam("newEnergy") : param;
        if (StringUtils.isBlank(variable)) {
            return;
        }
        var variables = variable.split(";");
        for (var var : variables) {
            // var 租户id(如：58197260)
            executeByTenant(var);
        }
    }

    public void executeByTenant(String param) {
        log.info("能耗采集开始：================================================================{}", param);
        if (StringUtils.isBlank(param)) {
            return;
        }
        var tenant = Integer.valueOf(param);
        SessionContextHolder.requestTenant(tenant);
        try {
            //获取当前时间
            var date = LocalDateTime.now();
            //时间校正
            var min = date.getMinute() / 10 * 10; //利用 / 除法 向下取整
            var dateTime = date.withMinute(min).withSecond(0).withNano(0);
            //获取设备列表
            var meterDevices = meterDeviceService.list();
            //排除了能量表 这里需要优化：哪些表可以采集，哪些不可以
            var devices = meterDevices.stream()
                    .filter(s -> !s.getInstrumentType().equals(InstrumentTypeEnum.ENERGY_METER.getCode())).collect(Collectors.toList());
            //获取分项列表中的设备
            var deviceProjectsList = energyProjectService.getDeviceProject();
            var deviceProjects = deviceProjectsList.stream()
                    .filter(s -> !s.getInstrumentType().equals(InstrumentTypeEnum.ENERGY_METER.getCode())).collect(Collectors.toList());
            //获取区域分项中的设备
            var deviceAreasList = areaProjectService.getDeviceAreas();
            List<DeviceAreaVo> deviceAreas = deviceAreasList.stream()
                    .filter(s -> !InstrumentTypeEnum.ENERGY_METER.getCode().equals(s.getInstrumentType())).collect(Collectors.toList());
            //支路列表中的设备
            var routeDevicesList = routeProjectService.getRouteDevices();
            var routeDevices = routeDevicesList.stream()
                    .filter(s -> !s.getInstrumentType().equals(InstrumentTypeEnum.ENERGY_METER.getCode())).collect(Collectors.toList());
            //获取峰平谷值，目前是统一配置；如果后期不同类型如：电、水 不同的话，建议写一个方法，返回一个Map,把每种类型的都返回来
            //然后每个设备根据仪表类型，直接获取
            var fpgj = energyCollectPeriodService.getPeriodFlagInFPG("default", dateTime);
            var collectConfigMap = energyCollectConfigService.getCategoryMap();
            //时间枚举值
            var values = DateEnum.values();
            //线程数
            ExecutorContextHolder.run(() -> {
                try {
                    //设备采集
                    collectionDevice(devices, dateTime, values, fpgj, collectConfigMap);
                } catch (Exception e) {
                    log.info("多线程数据采集设备及分项出错======", e);
                }
            });
            ExecutorContextHolder.run(() -> {
                try {
                    //分项采集
                    collectionProjectDevice(deviceProjects, dateTime, values, fpgj, collectConfigMap);
                } catch (Exception e) {
                    log.info("多线程数据采集设备及分项出错======", e);
                }
            });
            ExecutorContextHolder.run(() -> {
                try {
                    //区域采集
                    collectionAreaDevice(deviceAreas, dateTime, values, fpgj, collectConfigMap);
                } catch (Exception e) {
                    log.info("多线程数据采集区域及支路出错======", e);
                }
            });
            ExecutorContextHolder.run(() -> {
                try {
                    //支路采集
                    collectionRouteDevice(routeDevices, dateTime, values, fpgj, collectConfigMap);
                } catch (Exception e) {
                    log.info("多线程数据采集区域及支路出错======", e);
                }
            });
            log.info("能耗采集结束：================================================================");
        } catch (Exception e) {
            log.error("能耗采集,失败原因:", e);
        }

    }

    /**
     * 删除数据  数据删完之后 表会自动删除
     *
     * @param measurement
     *
     * @throws ParseException
     */
    public void deleteEnergy(String measurement) throws ParseException {
        DeleteApi deleteApi = influxdbClient.getDeleteApi();
        LocalDateTime parse = DateUtils.parse("2020-01-01 06:00:00");
        LocalDateTime parse1 = DateUtils.parse("2027-12-31 06:00:00");

        String flux = "_measurement=\"" + measurement + "\"";
        OffsetDateTime begin = OffsetDateTime.of(parse, ZoneOffset.of("Z"));
        OffsetDateTime end = OffsetDateTime.of(parse1, ZoneOffset.of("Z"));
        System.out.println("================删除===================");
        deleteApi.delete(begin, end, flux, "energy", "ghy");
    }

    /**
     * @Description 采集数设备数据
     * @Author lrj
     * @Param
     * @Return
     * @Exception
     */
    private void collectionDevice(List<MeterDevice> devices,
                                  LocalDateTime dateTime,
                                  DateEnum[] values,
                                  String fpgj,
                                  Map<String, EnergyCollectConfig> collectConfigMap
                                 ) {

        if (!devices.isEmpty()) {
            var insertPointList = new ArrayList<Point>();
            WriteApiBlocking writeApiBlocking = influxdbClient.getWriteApiBlocking();
            final int influxWriteBatchSize = 100;
            for (MeterDevice device : devices) {
                SessionContextHolder.requestTenant(device.getTenant());
                String energyProCode = getProperties(device.getInstrumentType());
                //属性
                List<KeyValueWithTsDTO> keyValueWithTsDTOS = DeviceAttribute.deviceProp(device.getDeviceId(), AttributeTypeEnum.getByDesc(device.getInstrumentType()));
                KeyValueWithTsDTO keyValueWithTsDTO = keyValueWithTsDTOS.stream()
                        .filter(attributeRunningDTO -> energyProCode.equals(attributeRunningDTO.getKey())).findFirst().orElse(null);
                if (Objects.isNull(keyValueWithTsDTO)) {
                    log.info(energyProCode + "的属性对象为空");
                    continue;
                }
                for (DateEnum dateEnum : values) {
                    //数据库中的最新数据
                    DataVo deviceLatestDataInDB = InfluxdbUtil.getDeviceLatestDataInDB(dateTime, dateEnum, device.getDeviceId());
                    if (deviceLatestDataInDB != null) {
                        String energyValue = StringUtils.isEmpty(keyValueWithTsDTO.getValue().toString()) ? "0.00"
                                                                                                          : keyValueWithTsDTO.getValue()
                                                     .toString();
                        if (InfluxdbUtil.isNumeric(energyValue)) {
                            //构造采集数据对象
                            DeviceCollectDataTmpVO collectDataTmp = new DeviceCollectDataTmpVO();
                            EnergyCollectConfig collectConfig = collectConfigMap.get(device.getInstrumentType());
                            MeterUsageCalculator.Result result = collectDataTmp.fillTmpData(dateTime, deviceLatestDataInDB.getTime(), deviceLatestDataInDB,
                                                                                            new BigDecimal(energyValue), dateEnum, fpgj,
                                                                                            getRolloverValue(collectConfig), getMaxStepUsage(collectConfig));
                            logAbnormalUsage(result, device.getDeviceId(), device.getInstrumentType(), dateEnum, collectDataTmp.getLastReadeValue(),
                                             collectDataTmp.getReadeValue(), collectConfig);
                            Point devicePoint = createDevicePoint(
                                    device,
                                    InfluxdbUtil.getAggTime(dateEnum, dateTime),
                                    dateEnum,
                                    collectDataTmp);
                            insertPointList.add(devicePoint);
                            if (insertPointList.size() >= influxWriteBatchSize) {
                                writeApiBlocking.writePoints(dbName, dborg, insertPointList);
                                insertPointList.clear();
                            }
                        }
                    } else {
                        String energyValue = keyValueWithTsDTO.getValue().toString();
                        if (InfluxdbUtil.isNumeric(energyValue)) {
                            //没有初始值，首次采集的时候没有统计用量
                            DeviceCollectDataTmpVO collectDataTmp = new DeviceCollectDataTmpVO(new BigDecimal(energyValue));
                            Point devicePoint = createDevicePoint(
                                    device,
                                    InfluxdbUtil.getAggTime(dateEnum, dateTime),
                                    dateEnum,
                                    collectDataTmp);
                            insertPointList.add(devicePoint);
                            if (insertPointList.size() >= influxWriteBatchSize) {
                                writeApiBlocking.writePoints(dbName, dborg, insertPointList);
                                insertPointList.clear();
                            }
                        }
                    }
                }
            }
            if (!insertPointList.isEmpty()) {
                writeApiBlocking.writePoints(dbName, dborg, insertPointList);
            }
        }

    }

    /**
     * @Description 采集数分项数据
     * @Author lrj
     * @Param
     * @Return
     * @Exception
     */
    private void collectionProjectDevice(List<DeviceProjectVo> deviceProjects, LocalDateTime dateTime, DateEnum[] values,
                                         String fpgj,
                                         Map<String, EnergyCollectConfig> collectConfigMap
                                        ) {
        if (deviceProjects.size() > 0) {
            var influxWriteBatchSize = 100;
            WriteApiBlocking writeApiBlocking = influxdbClient.getWriteApiBlocking();
            var insertPointList = new ArrayList<Point>();
            SessionContextHolder.requestTenant(deviceProjects.get(0).getTenant());
            for (DeviceProjectVo deviceProjectVo : deviceProjects) {
                SessionContextHolder.requestTenant(deviceProjectVo.getTenant());
                String energyProCode = getProperties(deviceProjectVo.getInstrumentType());
                //属性
                List<KeyValueWithTsDTO> keyValueWithTsDTOS = DeviceAttribute.deviceProp(deviceProjectVo.getDeviceId()
                                                                                                .toString(), AttributeTypeEnum.getByDesc(deviceProjectVo.getInstrumentType()));
                if (!CollectionUtils.isEmpty(keyValueWithTsDTOS)) {
                    KeyValueWithTsDTO keyValueWithTsDTO = keyValueWithTsDTOS.stream()
                            .filter(attributeRunningDTO -> energyProCode.equals(attributeRunningDTO.getKey())).findFirst().orElse(null);
                    if (Objects.isNull(keyValueWithTsDTO)) {
                        log.info(energyProCode + "的属性对象为空");
                        continue;
                    }
                    for (DateEnum dateEnum : values) {
                        DataVo itemLatestDataInDB = InfluxdbUtil.getItemLatestDataInDB(dateTime, dateEnum, deviceProjectVo.getDeviceId(), deviceProjectVo.getProjectId());
                        if (itemLatestDataInDB != null) {
                            String energyValue = StringUtils.isEmpty(keyValueWithTsDTO.getValue().toString()) ? "0.00"
                                                                                                              : keyValueWithTsDTO.getValue()
                                                         .toString();
                            if (InfluxdbUtil.isNumeric(energyValue)) {
                                DeviceCollectDataTmpVO collectDataTmpVO = new DeviceCollectDataTmpVO();
                                EnergyCollectConfig collectConfig = collectConfigMap.get(deviceProjectVo.getInstrumentType());
                                MeterUsageCalculator.Result result = collectDataTmpVO.fillTmpData(dateTime, itemLatestDataInDB.getTime(), itemLatestDataInDB,
                                                                                                  new BigDecimal(energyValue), dateEnum, fpgj,
                                                                                                  getRolloverValue(collectConfig), getMaxStepUsage(collectConfig));
                                logAbnormalUsage(result, deviceProjectVo.getDeviceId().toString(), deviceProjectVo.getInstrumentType(), dateEnum,
                                                 collectDataTmpVO.getLastReadeValue(), collectDataTmpVO.getReadeValue(), collectConfig);
                                Point devicePoint = createProjectPoint(deviceProjectVo,
                                                                       InfluxdbUtil.getAggTime(dateEnum, dateTime),
                                                                       dateEnum,
                                                                       collectDataTmpVO);
                                insertPointList.add(devicePoint);
                                if (insertPointList.size() >= influxWriteBatchSize) {
                                    writeApiBlocking.writePoints(dbName, dborg, insertPointList);
                                    insertPointList.clear();
                                }
                            }
                        } else {
                            String energyValue = keyValueWithTsDTO.getValue().toString();
                            if (InfluxdbUtil.isNumeric(energyValue)) {
                                DeviceCollectDataTmpVO collectDataTmpVO = new DeviceCollectDataTmpVO(new BigDecimal(energyValue));
                                Point devicePoint = createProjectPoint(deviceProjectVo,
                                                                       InfluxdbUtil.getAggTime(dateEnum, dateTime),
                                                                       dateEnum,
                                                                       collectDataTmpVO);
                                insertPointList.add(devicePoint);
                                if (insertPointList.size() >= influxWriteBatchSize) {
                                    writeApiBlocking.writePoints(dbName, dborg, insertPointList);
                                    insertPointList.clear();
                                }
                            }

                        }
                    }
                }
            }
            if (!insertPointList.isEmpty()) {
                writeApiBlocking.writePoints(dbName, dborg, insertPointList);
            }
        }
    }

    /**
     * @Description采集数区域数据
     * @Author lrj
     * @Param
     * @Return
     * @Exception
     */
    private void collectionAreaDevice(List<DeviceAreaVo> deviceAreaVos,
                                      LocalDateTime dateTime,
                                      DateEnum[] values,
                                      String fpgj,
                                      Map<String, EnergyCollectConfig> collectConfigMap
                                     ) {
        if (deviceAreaVos.size() > 0) {
            var influxWriteBatchSize = 100;
            WriteApiBlocking writeApiBlocking = influxdbClient.getWriteApiBlocking();
            var insertPointList = new ArrayList<Point>();
            SessionContextHolder.requestTenant(deviceAreaVos.get(0).getTenant());
            for (DeviceAreaVo deviceAreaVo : deviceAreaVos) {
                SessionContextHolder.requestTenant(deviceAreaVo.getTenant());
                String energyProCode = getProperties(deviceAreaVo.getInstrumentType());
                //属性
                List<KeyValueWithTsDTO> keyValueWithTsDTOS = DeviceAttribute.deviceProp(deviceAreaVo.getDeviceId()
                                                                                                .toString(), AttributeTypeEnum.getByDesc(deviceAreaVo.getInstrumentType()));
                if (!CollectionUtils.isEmpty(keyValueWithTsDTOS)) {
                    KeyValueWithTsDTO keyValueWithTsDTO = keyValueWithTsDTOS.stream()
                            .filter(attributeRunningDTO -> energyProCode.equals(attributeRunningDTO.getKey())).findFirst().orElse(null);
                    if (Objects.isNull(keyValueWithTsDTO)) {
                        log.info(energyProCode + "的属性对象为空");
                        continue;
                    }
                    for (DateEnum dateEnum : values) {
                        DataVo itemLatestDataInDB = InfluxdbUtil.getAreaLatestDataInDB(dateTime, dateEnum, deviceAreaVo.getDeviceId(), deviceAreaVo.getAreaId());
                        if (itemLatestDataInDB != null) {
                            String energyValue = StringUtils.isEmpty(keyValueWithTsDTO.getValue().toString()) ? "0.00"
                                                                                                              : keyValueWithTsDTO.getValue()
                                                         .toString();
                            if (InfluxdbUtil.isNumeric(energyValue)) {
                                DeviceCollectDataTmpVO collectDataTmpVO = new DeviceCollectDataTmpVO();
                                EnergyCollectConfig collectConfig = collectConfigMap.get(deviceAreaVo.getInstrumentType());
                                MeterUsageCalculator.Result result = collectDataTmpVO.fillTmpData(dateTime, itemLatestDataInDB.getTime(), itemLatestDataInDB,
                                                                                                  new BigDecimal(energyValue), dateEnum, fpgj,
                                                                                                  getRolloverValue(collectConfig), getMaxStepUsage(collectConfig));
                                logAbnormalUsage(result, deviceAreaVo.getDeviceId().toString(), deviceAreaVo.getInstrumentType(), dateEnum,
                                                 collectDataTmpVO.getLastReadeValue(), collectDataTmpVO.getReadeValue(), collectConfig);
                                Point devicePoint = createAreaPoint(deviceAreaVo,
                                                                    InfluxdbUtil.getAggTime(dateEnum, dateTime),
                                                                    dateEnum, collectDataTmpVO);
                                insertPointList.add(devicePoint);
                                if (insertPointList.size() >= influxWriteBatchSize) {
                                    writeApiBlocking.writePoints(dbName, dborg, insertPointList);
                                    insertPointList.clear();
                                }
                            }

                        } else {
                            String energyValue = keyValueWithTsDTO.getValue().toString();
                            if (InfluxdbUtil.isNumeric(energyValue)) {
                                DeviceCollectDataTmpVO collectDataTmpVO = new DeviceCollectDataTmpVO(new BigDecimal(energyValue));
                                Point devicePoint = createAreaPoint(
                                        deviceAreaVo,
                                        InfluxdbUtil.getAggTime(dateEnum, dateTime),
                                        dateEnum,
                                        collectDataTmpVO);
                                insertPointList.add(devicePoint);
                                if (insertPointList.size() >= influxWriteBatchSize) {
                                    writeApiBlocking.writePoints(dbName, dborg, insertPointList);
                                    insertPointList.clear();
                                }
                            }

                        }
                    }
                }

            }
            if (!insertPointList.isEmpty()) {
                writeApiBlocking.writePoints(dbName, dborg, insertPointList);
            }
        }
    }

    /**
     * @Description采集数支路数据
     * @Author lrj
     * @Param
     * @Return
     * @Exception
     */
    private void collectionRouteDevice(List<RouteDeviceVo> routeDevices,
                                       LocalDateTime dateTime,
                                       DateEnum[] values,
                                       String fpgj,
                                       Map<String, EnergyCollectConfig> collectConfigMap
                                      ) {
        if (routeDevices.size() > 0) {
            var influxWriteBatchSize = 100;
            WriteApiBlocking writeApiBlocking = influxdbClient.getWriteApiBlocking();
            var insertPointList = new ArrayList<Point>();
            SessionContextHolder.requestTenant(routeDevices.get(0).getTenant());
            for (RouteDeviceVo routeDeviceVo : routeDevices) {
                SessionContextHolder.requestTenant(routeDeviceVo.getTenant());
                String energyProCode = getProperties(routeDeviceVo.getInstrumentType());
                //属性
                List<KeyValueWithTsDTO> keyValueWithTsDTOS = DeviceAttribute.deviceProp(routeDeviceVo.getDeviceId(), AttributeTypeEnum.getByDesc(routeDeviceVo.getInstrumentType()));
                if (!CollectionUtils.isEmpty(keyValueWithTsDTOS)) {
                    KeyValueWithTsDTO keyValueWithTsDTO = keyValueWithTsDTOS.stream()
                            .filter(attributeRunningDTO -> energyProCode.equals(attributeRunningDTO.getKey())).findFirst().orElse(null);
                    if (Objects.isNull(keyValueWithTsDTO)) {
                        log.info(energyProCode + "的属性对象为空");
                        continue;
                    }
                    for (DateEnum dateEnum : values) {
                        DataVo latestDataInDB = InfluxdbUtil.getRouteLatestDataInDB(influxdbClient, dateTime, dateEnum, routeDeviceVo);
                        if (latestDataInDB != null) {
                            String energyValue = StringUtils.isEmpty(keyValueWithTsDTO.getValue().toString()) ? "0.00"
                                                                                                              : keyValueWithTsDTO.getValue()
                                                         .toString();
                            if (InfluxdbUtil.isNumeric(energyValue)) {
                                DeviceCollectDataTmpVO collectDataTmpVO = new DeviceCollectDataTmpVO();
                                EnergyCollectConfig collectConfig = collectConfigMap.get(routeDeviceVo.getInstrumentType());
                                MeterUsageCalculator.Result result = collectDataTmpVO.fillTmpData(dateTime, latestDataInDB.getTime(), latestDataInDB,
                                                                                                  new BigDecimal(energyValue), dateEnum, fpgj,
                                                                                                  getRolloverValue(collectConfig), getMaxStepUsage(collectConfig));
                                logAbnormalUsage(result, routeDeviceVo.getDeviceId(), routeDeviceVo.getInstrumentType(), dateEnum,
                                                 collectDataTmpVO.getLastReadeValue(), collectDataTmpVO.getReadeValue(), collectConfig);
                                Point devicePoint = createRoutePoint(
                                        routeDeviceVo,
                                        InfluxdbUtil.getAggTime(dateEnum, dateTime),
                                        dateEnum,
                                        collectDataTmpVO);
                                insertPointList.add(devicePoint);
                                if (insertPointList.size() >= influxWriteBatchSize) {
                                    writeApiBlocking.writePoints(dbName, dborg, insertPointList);
                                    insertPointList.clear();
                                }
                            }
                        } else {
                            String energyValue = keyValueWithTsDTO.getValue().toString();
                            if (InfluxdbUtil.isNumeric(energyValue)) {
                                DeviceCollectDataTmpVO collectDataTmpVO = new DeviceCollectDataTmpVO(new BigDecimal(energyValue));
                                Point devicePoint = createRoutePoint(
                                        routeDeviceVo,
                                        InfluxdbUtil.getAggTime(dateEnum, dateTime),
                                        dateEnum,
                                        collectDataTmpVO);
                                insertPointList.add(devicePoint);
                                if (insertPointList.size() >= influxWriteBatchSize) {
                                    writeApiBlocking.writePoints(dbName, dborg, insertPointList);
                                    insertPointList.clear();
                                }
                            }

                        }

                    }
                }
            }
            if (!insertPointList.isEmpty()) {
                writeApiBlocking.writePoints(dbName, dborg, insertPointList);
            }
        }

    }

    private BigDecimal getRolloverValue(EnergyCollectConfig collectConfig) {
        return collectConfig == null ? null : collectConfig.getRolloverValue();
    }

    private BigDecimal getMaxStepUsage(EnergyCollectConfig collectConfig) {
        return collectConfig == null ? null : collectConfig.getMaxStepUsage();
    }

    private void logAbnormalUsage(MeterUsageCalculator.Result result,
                                  String deviceId,
                                  String instrumentType,
                                  DateEnum dateEnum,
                                  BigDecimal previous,
                                  BigDecimal current,
                                  EnergyCollectConfig collectConfig
                                 ) {
        if (result == null || !result.isAbnormal()) {
            return;
        }
        log.warn("能耗采集用量异常，deviceId={}, instrumentType={}, dateType={}, previous={}, current={}, rolloverValue={}, maxStepUsage={}, reason={}",
                 deviceId, instrumentType, dateEnum, previous, current, getRolloverValue(collectConfig), getMaxStepUsage(collectConfig),
                 result.getReason());
    }

    /**
     * @Description 温度采集
     * @Author lrj
     */
    public void collectionTemperature(LocalDateTime dateTime) {
        //获取温度
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, headers);
        String url = weatherUrl + province;
        ResponseEntity<JSONObject> exchange = restTemplate.exchange(url, HttpMethod.GET, httpEntity, JSONObject.class);
        JSONObject body = exchange.getBody();
        Object o = body.get("weather");
        JSONObject json = (JSONObject) JSON.toJSON(o);
        Object temperature = json.get("temperature");
        BigDecimal bigDecimal = new BigDecimal(temperature.toString());
        QueryApi queryApi = influxdbClient.getQueryApi();
        //采集维度时、天、月、年时间
        for (DateEnum value : DateEnum.values()) {
            LocalDateTime switchTime = InfluxdbUtil.getAggTime(value, dateTime);
            //小周期平均值
            BigDecimal temperatureValue = new BigDecimal(BigInteger.ZERO);
            String flux = "";
            if (value.toString().equals(DateEnum.Month.name())) {
                LocalDateTime dateTime1 = DateUtils.beginOfMonth(dateTime);
                String s = DateUtils.formatDate(dateTime1, DEFAULT_DATE_FORMAT) + "T00:00:00Z";
                String s1 = DateUtils.formatDate(dateTime, DEFAULT_DATE_FORMAT) + "T00:00:00Z";
                flux = "from(bucket: \"energy\")\n" +
                       "  |> range(start: " + s + ", stop: " + s1 + ")\n" +
                       "  |> filter(fn: (r) => r[\"_measurement\"] == \"temperatureDay\" and r[\"_field\"] == \"value\")\n" +
                       "  |> mean()";
                List<DataVo> query = queryApi.query(flux, DataVo.class);
                if (query.size() > 0) {
                    temperatureValue = temperatureValue.add(query.get(0).getValue());
                }
                Point temperaturePoint = createTemperaturePoint(switchTime, temperatureValue, value);
                WriteApiBlocking writeApiBlocking = influxdbClient.getWriteApiBlocking();
                writeApiBlocking.writePoint(dbName, dborg, temperaturePoint);
            } else if (value.toString().equals(DateEnum.Day.name())) {
                LocalDateTime dateTime1 = DateUtils.beginOfDay(dateTime);
                String s = DateUtils.formatDate(dateTime1, DEFAULT_DATE_FORMAT) + "T00:00:00Z";
                String s1 = DateUtils.formatDate(dateTime, DEFAULT_DATE_FORMAT) + "T00:00:00Z";
                flux = "from(bucket: \"energy\")\n" +
                       "  |> range(start: " + s + ", stop: " + s1 + ")\n" +
                       "  |> filter(fn: (r) => r[\"_measurement\"] == \"temperatureHour\" and r[\"_field\"] == \"value\")\n" +
                       "  |> mean()";
                List<DataVo> query = queryApi.query(flux, DataVo.class);
                if (query.size() > 0) {
                    temperatureValue = temperatureValue.add(query.get(0).getValue());
                }
                Point temperaturePoint = createTemperaturePoint(switchTime, temperatureValue, value);
                WriteApiBlocking writeApiBlocking = influxdbClient.getWriteApiBlocking();
                writeApiBlocking.writePoint(dbName, dborg, temperaturePoint);
            } else if (value.toString().equals(DateEnum.Hour.name())) {
                Point temperaturePoint = createTemperaturePoint(switchTime, bigDecimal, value);
                WriteApiBlocking writeApiBlocking = influxdbClient.getWriteApiBlocking();
                writeApiBlocking.writePoint(dbName, dborg, temperaturePoint);
            }
        }

    }

    private Point createTemperaturePoint(LocalDateTime dateTime, BigDecimal value, DateEnum dateEnum) {
        //拼表
        String measurement = "temperature" + dateEnum;
        //查询采集记录数据
        return Point.measurement(measurement)
                .addField("value", value.setScale(2, RoundingMode.HALF_UP))
                .time(dateTime.atZone(ZoneId.systemDefault()).toInstant(), WritePrecision.S);
    }

    private Point createDevicePoint(MeterDevice device, LocalDateTime switchTime, DateEnum value, DeviceCollectDataTmpVO tmp) {
        //拼表
        String measurement = "device" + value;
        //查询采集记录数据
        return Point.measurement(measurement)
                .addTag("deviceId", device.getDeviceId())//设备id
                .addTag("tenant", device.getTenant().toString())
                .addTag("instrumentType", device.getInstrumentType())
                .addField("deviceName", device.getDeviceName())//设备名称
                .addField("readeValue", tmp.getReadeValue().setScale(2, RoundingMode.HALF_UP))
                .addField("value", tmp.getCollectionNum().setScale(2, RoundingMode.HALF_UP))
                .addField("pvalue", tmp.getPcollectionNum().setScale(2, RoundingMode.HALF_UP))
                .addField("fvalue", tmp.getFcollectionNum().setScale(2, RoundingMode.HALF_UP))
                .addField("gvalue", tmp.getGcollectionNum().setScale(2, RoundingMode.HALF_UP))
                .addField("jvalue", tmp.getJcollectionNum().setScale(2, RoundingMode.HALF_UP))
                .addField("totalValue", tmp.getTotalValue().setScale(2, RoundingMode.HALF_UP))
                .time(switchTime.atZone(ZoneId.systemDefault()).toInstant(), WritePrecision.S);
    }

    //分项数据创建
    private Point createProjectPoint(DeviceProjectVo device,
                                     LocalDateTime switchTime,
                                     DateEnum value,
                                     DeviceCollectDataTmpVO tmp
                                    ) {

        //拼表
        String measurement = "project" + value;
        //计算分摊值
        BigDecimal shareValue = tmp.getCollectionNum();
        if (device.getShareNum() != null) {
            shareValue = shareValue.multiply(device.getShareNum().divide(BigDecimal.valueOf(100)));
        }
        //查询采集记录数据
        return Point.measurement(measurement)
                .addTag("deviceId", device.getDeviceId().toString())//设备id
                .addTag("projectId", device.getProjectId().toString())
                .addTag("tenant", device.getTenant().toString())
                .addTag("instrumentType", device.getInstrumentType())
                .addField("projectName", device.getProjectName())
                .addField("value", tmp.getCollectionNum().setScale(2, RoundingMode.HALF_UP))
                .addField("shareValue", shareValue.setScale(2, RoundingMode.HALF_UP))//分摊值
                .addField("readeValue", tmp.getReadeValue().setScale(2, RoundingMode.HALF_UP))
                .addField("totalValue", tmp.getTotalValue().setScale(2, RoundingMode.HALF_UP))
                .time(switchTime.atZone(ZoneId.systemDefault()).toInstant(), WritePrecision.S);
    }

    //区域数据创建
    private Point createAreaPoint(DeviceAreaVo device, LocalDateTime switchTime,
                                  DateEnum value, DeviceCollectDataTmpVO tmp
                                 ) {
        //拼表
        String measurement = "area" + value;
        //计算分摊值
        BigDecimal shareValue = tmp.getCollectionNum();
        if (device.getShareNum() != null) {
            shareValue = shareValue.multiply(device.getShareNum().divide(BigDecimal.valueOf(100)));
        }
        //查询采集记录数据
        return Point.measurement(measurement)
                .addTag("deviceId", device.getDeviceId().toString())//设备id
                .addTag("sequence", device.getSequence())
                .addTag("areaId", device.getAreaId().toString())
                .addTag("tenant", device.getTenant().toString())
                .addTag("instrumentType", device.getInstrumentType())
                .addField("areaName", device.getAreaName())
                .addField("value", tmp.getCollectionNum().setScale(2, RoundingMode.HALF_UP))
                .addField("shareValue", shareValue.setScale(2, RoundingMode.HALF_UP))
                .addField("readeValue", tmp.getReadeValue().setScale(2, RoundingMode.HALF_UP))
                .addField("fvalue", tmp.getFcollectionNum().setScale(2, RoundingMode.HALF_UP))
                .addField("pvalue", tmp.getPcollectionNum().setScale(2, RoundingMode.HALF_UP))
                .addField("gvalue", tmp.getGcollectionNum().setScale(2, RoundingMode.HALF_UP))
                .addField("jvalue", tmp.getJcollectionNum().setScale(2, RoundingMode.HALF_UP))
                .addField("totalValue", tmp.getTotalValue().setScale(2, RoundingMode.HALF_UP))
                .time(switchTime.atZone(ZoneId.systemDefault()).toInstant(), WritePrecision.S);
    }

    //支路数据创建
    private Point createRoutePoint(RouteDeviceVo device, LocalDateTime switchTime, DateEnum value, DeviceCollectDataTmpVO tmp) {
        //拼表
        String measurement = "route" + value;
        //计算分摊值
        BigDecimal shareValue = tmp.getCollectionNum();
        if (device.getShareNum() != null) {
            shareValue = shareValue.multiply(device.getShareNum().divide(BigDecimal.valueOf(100)));
        }
        //查询采集记录数据
        return Point.measurement(measurement)
                .addTag("deviceId", device.getDeviceId())//设备id
                .addTag("sequence", device.getSequence())
                .addTag("routeId", device.getRouteId())
                .addTag("tenant", device.getTenant().toString())
                .addTag("instrumentType", device.getInstrumentType())
                .addField("routeName", device.getRouteName())
                .addField("value", tmp.getCollectionNum().setScale(2, RoundingMode.HALF_UP))
                .addField("shareValue", shareValue.setScale(2, RoundingMode.HALF_UP))
                .addField("readeValue", tmp.getReadeValue().setScale(2, RoundingMode.HALF_UP))
                .addField("fvalue", tmp.getFcollectionNum().setScale(2, RoundingMode.HALF_UP))
                .addField("pvalue", tmp.getPcollectionNum().setScale(2, RoundingMode.HALF_UP))
                .addField("gvalue", tmp.getGcollectionNum().setScale(2, RoundingMode.HALF_UP))
                .addField("jvalue", tmp.getJcollectionNum().setScale(2, RoundingMode.HALF_UP))
                .addField("totalValue", tmp.getTotalValue().setScale(2, RoundingMode.HALF_UP))
                .time(switchTime.atZone(ZoneId.systemDefault()).toInstant(), WritePrecision.S);
    }

    /**
     * 获取仪表对应的物联网属性
     *
     * @param instrumentType
     *
     * @return
     */
    public static String getProperties(String instrumentType) {
        return AttributeTypeEnum.getByDesc(instrumentType);
    }
}
