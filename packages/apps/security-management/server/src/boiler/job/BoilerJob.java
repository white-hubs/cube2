package com.guhecloud.cube.srv.gen.securitymanagement.server.boiler.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.iot.north.bridge.client.IotDevicePropClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.DevicePropHistoryParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.boiler.dao.BoilerDataMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.boiler.entity.BoilerData;
import com.guhecloud.cube.srv.gen.securitymanagement.server.boiler.param.BoilerParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity.SecurityDevice;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.service.SecurityCommonService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.xxl.job.core.biz.model.ReturnT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class BoilerJob {
    @Autowired
    private SecurityCommonService securityCommonService;
    @Autowired
    private BoilerDataMapper boilerDataMapper;
    @Autowired
    private IotDevicePropClient iotDevicePropClient;

    /**
     * @Description 锅炉采集
     * @Author lrj
     */
    public ReturnT<String> boilerData(String param) {
        SessionContextHolder.requestTenant(Integer.valueOf(param));
        var boiler = securityCommonService.list(Wrappers.<SecurityDevice>lambdaQuery()
                                                        .eq(SecurityDevice::getApplicationCode, "BOLIER"));
        if (CollectionUtils.isNotEmpty(boiler)) {
            var collect = boiler.stream().map(SecurityDevice::getDeviceId).collect(Collectors.toList());
            var boilerParam = new BoilerParam();
            boilerParam.setDeviceIds(collect);
            var date = LocalDateTime.now();
            var stTime = DateUtils.offset(date, ChronoUnit.DAYS, -2);
            boilerParam.setStTime(stTime);
            var boilerData = boilerDataMapper.getBoilerData(boilerParam);
            if (CollectionUtils.isNotEmpty(boilerData)) {
                var boilerDataMap = boilerData.stream().collect(Collectors.toMap(BoilerData::getDeviceId, s -> s));
                for (SecurityDevice securityDevice : boiler) {
                    var boilerData1 = boilerDataMap.get(securityDevice.getDeviceId());
                    var boilerData2 = new BoilerData();
                    boilerData2.setDeviceId(securityDevice.getDeviceId());
                    if (Objects.nonNull(boilerData1)) {
                        var historicalAttributeChangeQuery = new DevicePropHistoryParam();
                        historicalAttributeChangeQuery.setDeviceId(securityDevice.getDeviceId());
                        historicalAttributeChangeQuery.setStartTs(DateUtils.toTimestamp(stTime));
                        historicalAttributeChangeQuery.setEndTs(DateUtils.toTimestamp(date));
                        historicalAttributeChangeQuery.setMaxRecords(30);
                        List<String> prop = new ArrayList<>();
                        prop.add("temperature");
                        historicalAttributeChangeQuery.setPropCodes(prop);
                        var keyValueWithTsDTOS = iotDevicePropClient.devicePropHistoryProp(historicalAttributeChangeQuery);
                        var historicalAttributeChangeTemperature = keyValueWithTsDTOS.stream().findFirst().orElse(null);
                        var temperature = BigDecimal.ZERO;
                        if (Objects.nonNull(historicalAttributeChangeTemperature)) {
                            var value = historicalAttributeChangeTemperature.getValue();
                            temperature = temperature.add(new BigDecimal(Objects.isNull(value) ? "0" : value.toString()));
                        }
                        boilerData2.setTemperature(temperature);
                        boilerData2.setCollectTime(date);
                        //水
                        List<String> propWater = new ArrayList<>();
                        propWater.add("sds");
                        historicalAttributeChangeQuery.setPropCodes(propWater);
                        var listWater = iotDevicePropClient.devicePropHistoryProp(historicalAttributeChangeQuery);
                        var historicalAttributeChangeWater = listWater.stream().findFirst().orElse(null);
                        if (Objects.nonNull(historicalAttributeChangeWater)) {
                            var value = historicalAttributeChangeWater.getValue();
                            if (Objects.nonNull(value)) {
                                if (new BigDecimal(value.toString()).compareTo(boilerData1.getWaterLastValue()) != -1) {
                                    var subtract = new BigDecimal(value.toString()).subtract(boilerData1.getWaterLastValue());
                                    boilerData2.setWaterLastValue(new BigDecimal(value.toString()));
                                    boilerData2.setWaterValue(subtract);
                                } else {
                                    boilerData2.setWaterLastValue(boilerData1.getWaterLastValue());
                                    boilerData2.setWaterValue(BigDecimal.ZERO);
                                }
                            } else {
                                boilerData2.setWaterLastValue(boilerData1.getWaterLastValue());
                                boilerData2.setWaterValue(BigDecimal.ZERO);
                            }
                        } else {
                            boilerData2.setWaterLastValue(boilerData1.getWaterLastValue());
                            boilerData2.setWaterValue(BigDecimal.ZERO);
                        }
                        //电
                        List<String> propElectricity = new ArrayList<>();
                        propElectricity.add("dds");
                        historicalAttributeChangeQuery.setPropCodes(propElectricity);
                        var listElectricity = iotDevicePropClient.devicePropHistoryProp(historicalAttributeChangeQuery);
                        var historicalAttributeChangeElectricity = listElectricity.stream().findFirst().orElse(null);
                        if (Objects.nonNull(historicalAttributeChangeElectricity)) {
                            var value = historicalAttributeChangeElectricity.getValue();
                            if (Objects.nonNull(value)) {
                                if (new BigDecimal(value.toString()).compareTo(boilerData1.getElectricityLastValue()) != -1) {
                                    var subtract = new BigDecimal(value.toString()).subtract(boilerData1.getElectricityLastValue());
                                    boilerData2.setElectricityLastValue(new BigDecimal(value.toString()));
                                    boilerData2.setElectricityValue(subtract);
                                } else {
                                    boilerData2.setElectricityLastValue(boilerData1.getElectricityLastValue());
                                    boilerData2.setElectricityValue(BigDecimal.ZERO);
                                }
                            } else {
                                boilerData2.setElectricityLastValue(boilerData1.getElectricityLastValue());
                                boilerData2.setElectricityValue(BigDecimal.ZERO);
                            }
                        } else {
                            boilerData2.setElectricityLastValue(boilerData1.getElectricityLastValue());
                            boilerData2.setElectricityValue(BigDecimal.ZERO);
                        }

                        boilerDataMapper.insert(boilerData2);
                    } else {

                        var historicalAttributeChangeQuery = new DevicePropHistoryParam();
                        historicalAttributeChangeQuery.setDeviceId(securityDevice.getDeviceId());
                        historicalAttributeChangeQuery.setDeviceId(securityDevice.getDeviceId());
                        historicalAttributeChangeQuery.setStartTs(DateUtils.toTimestamp(stTime));
                        historicalAttributeChangeQuery.setEndTs(DateUtils.toTimestamp(date));
                        historicalAttributeChangeQuery.setMaxRecords(30);
                        List<String> prop = new ArrayList<>();
                        prop.add("temperature");
                        historicalAttributeChangeQuery.setPropCodes(prop);
                        var list = iotDevicePropClient.devicePropHistoryProp(historicalAttributeChangeQuery);
                        var historicalAttributeChangeTemperature = list.stream().findFirst().orElse(null);
                        var temperature = BigDecimal.ZERO;
                        if (Objects.nonNull(historicalAttributeChangeTemperature)) {
                            var value = historicalAttributeChangeTemperature.getValue();
                            temperature = temperature.add(new BigDecimal(Objects.isNull(value) ? "0" : value.toString()));
                        }
                        boilerData2.setTemperature(temperature);
                        boilerData2.setDeviceId(securityDevice.getDeviceId());
                        boilerData2.setCollectTime(date);
                        //水
                        List<String> propWater = new ArrayList<>();
                        propWater.add("sds");
                        historicalAttributeChangeQuery.setPropCodes(prop);
                        var listWater = iotDevicePropClient.devicePropHistoryProp(historicalAttributeChangeQuery);
                        var historicalAttributeChangeWater = listWater.stream().findFirst().orElse(null);
                        if (Objects.nonNull(historicalAttributeChangeWater)) {
                            var value = historicalAttributeChangeWater.getValue();
                            if (Objects.nonNull(value)) {
                                boilerData2.setWaterLastValue(new BigDecimal(value.toString()));
                                boilerData2.setWaterValue(BigDecimal.ZERO);
                            } else {
                                boilerData2.setWaterLastValue(BigDecimal.ZERO);
                                boilerData2.setWaterValue(BigDecimal.ZERO);
                            }
                        } else {
                            boilerData2.setWaterLastValue(BigDecimal.ZERO);
                            boilerData2.setWaterValue(BigDecimal.ZERO);
                        }
                        //电
                        List<String> propElectricity = new ArrayList<>();
                        propWater.add("dds");
                        historicalAttributeChangeQuery.setPropCodes(propElectricity);
                        var listElectricity = iotDevicePropClient.devicePropHistoryProp(historicalAttributeChangeQuery);
                        var historicalAttributeChangeElectricity = listElectricity.stream().findFirst().orElse(null);
                        if (Objects.nonNull(historicalAttributeChangeElectricity)) {
                            var value = historicalAttributeChangeElectricity.getValue();
                            if (Objects.nonNull(value)) {
                                boilerData2.setElectricityLastValue(new BigDecimal(value.toString()));
                                boilerData2.setElectricityValue(BigDecimal.ZERO);
                            } else {
                                boilerData2.setElectricityLastValue(BigDecimal.ZERO);
                                boilerData2.setElectricityValue(BigDecimal.ZERO);
                            }
                        } else {
                            boilerData2.setElectricityLastValue(BigDecimal.ZERO);
                            boilerData2.setElectricityValue(BigDecimal.ZERO);
                        }

                        boilerDataMapper.insert(boilerData2);

                    }
                }

            } else {
                for (SecurityDevice securityDevice : boiler) {
                    var boilerData2 = new BoilerData();
                    var historicalAttributeChangeQuery = new DevicePropHistoryParam();
                    historicalAttributeChangeQuery.setDeviceId(securityDevice.getDeviceId());
                    historicalAttributeChangeQuery.setDeviceId(securityDevice.getDeviceId());
                    historicalAttributeChangeQuery.setStartTs(DateUtils.toTimestamp(stTime));
                    historicalAttributeChangeQuery.setEndTs(DateUtils.toTimestamp(date));
                    historicalAttributeChangeQuery.setMaxRecords(30);
                    List<String> prop = new ArrayList<>();
                    prop.add("temperature");
                    historicalAttributeChangeQuery.setPropCodes(prop);
                    var list = iotDevicePropClient.devicePropHistoryProp(historicalAttributeChangeQuery);
                    var historicalAttributeChangeTemperature = list.stream().findFirst().orElse(null);
                    var temperature = BigDecimal.ZERO;
                    if (Objects.nonNull(historicalAttributeChangeTemperature)) {
                        var value = historicalAttributeChangeTemperature.getValue();
                        temperature = temperature.add(new BigDecimal(Objects.isNull(value) ? "0" : value.toString()));
                    }
                    boilerData2.setTemperature(temperature);
                    boilerData2.setDeviceId(securityDevice.getDeviceId());
                    boilerData2.setCollectTime(date);
                    //水
                    List<String> propWater = new ArrayList<>();
                    propWater.add("sds");
                    historicalAttributeChangeQuery.setPropCodes(propWater);
                    var listWater = iotDevicePropClient.devicePropHistoryProp(historicalAttributeChangeQuery);
                    var historicalAttributeChangeWater = listWater.stream().findFirst().orElse(null);
                    if (Objects.nonNull(historicalAttributeChangeWater)) {
                        var value = historicalAttributeChangeWater.getValue();
                        if (Objects.nonNull(value)) {
                            boilerData2.setWaterLastValue(new BigDecimal(value.toString()));
                            boilerData2.setWaterValue(BigDecimal.ZERO);
                        } else {
                            boilerData2.setWaterLastValue(BigDecimal.ZERO);
                            boilerData2.setWaterValue(BigDecimal.ZERO);
                        }
                    } else {
                        boilerData2.setWaterLastValue(BigDecimal.ZERO);
                        boilerData2.setWaterValue(BigDecimal.ZERO);
                    }
                    //电
                    propWater.add("dds");
                    historicalAttributeChangeQuery.setPropCodes(propWater);
                    var listElectricity = iotDevicePropClient.devicePropHistoryProp(historicalAttributeChangeQuery);
                    var historicalAttributeChangeElectricity = listElectricity.stream().findFirst().orElse(null);
                    if (Objects.nonNull(historicalAttributeChangeElectricity)) {
                        var value = historicalAttributeChangeElectricity.getValue();
                        if (Objects.nonNull(value)) {
                            boilerData2.setElectricityLastValue(new BigDecimal(value.toString()));
                            boilerData2.setElectricityValue(BigDecimal.ZERO);
                        } else {
                            boilerData2.setElectricityLastValue(BigDecimal.ZERO);
                            boilerData2.setElectricityValue(BigDecimal.ZERO);
                        }
                    } else {
                        boilerData2.setElectricityLastValue(BigDecimal.ZERO);
                        boilerData2.setElectricityValue(BigDecimal.ZERO);
                    }

                    boilerDataMapper.insert(boilerData2);

                }
            }
        }
        return ReturnT.SUCCESS;
    }
}
