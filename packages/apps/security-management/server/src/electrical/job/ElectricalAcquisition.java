package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.job;

import com.guhecloud.cube.iot.north.bridge.client.IotDevicePropClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.KeyValueWithTsDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.DevicePropParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.entity.PowerDistributionBox;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.entity.TransformerDistribution;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.entity.TransformerMeter;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.enumeration.StatisticsType;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.service.PowerDistributionBoxService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.service.TransformerDistributionService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.service.TransformerMeterService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ElectricalAcquisition {
    @Autowired
    private TransformerMeterService transformerMeterService;

    @Autowired
    private PowerDistributionBoxService powerDistributionBoxService;

    @Autowired
    private TransformerDistributionService transformerDistributionService;

    @Autowired
    private IotDevicePropClient iotDevicePropClient;

    /**
     * @Description 用电采集
     * @Author lrj
     */
    public void addElectricity(String param) {

        SessionContextHolder.requestTenant(Integer.valueOf(param));
        var date = LocalDateTime.now();
        var list = transformerDistributionService.list();
        var highDistributions = list.stream().map(TransformerDistribution::getHighDistribution).collect(Collectors.toSet());
        //变压器用电流读数采集
        list.forEach(s -> {
            var propParam = new DevicePropParam();
            propParam.setDeviceId(s.getTransformer());
            propParam.setPropertyCodes("ic,ib");
            var keyValueWithTsDTOS = iotDevicePropClient.deviceProp(propParam);

            if (CollectionUtils.isNotEmpty(keyValueWithTsDTOS)) {
                var transformerMeter = new TransformerMeter();
                transformerMeter.setStatisticalMethod(StatisticsType.ELECTRICITY.name());
                transformerMeter.setDeviceId(Long.valueOf(s.getTransformer()));
                transformerMeter.setDistributionDevice(s.getHighDistribution());
                //电流读数a
                var tElectricA = new BigDecimal("0.00");
                //电表读数b
                var tElectricB = new BigDecimal("0.00");

                for (KeyValueWithTsDTO a : keyValueWithTsDTOS) {
                    if (a.getKey().equals("ic")) {
                        tElectricA = tElectricA.add(
                                a.getValue().equals("empty") ? new BigDecimal("0.00") : new BigDecimal(a.getValue().toString()));
                    }
                    if (a.getKey().equals("ib")) {
                        tElectricB = tElectricB.add(
                                a.getValue().equals("empty") ? new BigDecimal("0.00") : new BigDecimal(a.getValue().toString()));
                    }
                }

                transformerMeter.setMeterA(tElectricA);
                transformerMeter.setMeterB(tElectricB);
                //电量计算
                var lastOne = transformerMeterService.getLastOne(s.getTransformer());
                if (Objects.isNull(lastOne)) {
                    transformerMeter.setCurrentQuantity(new BigDecimal("0.00"));
                } else {
                    var totalA = tElectricA.add(tElectricB);
                    var totalB = lastOne.getMeterA().add(lastOne.getMeterB());
                    if (totalA.compareTo(totalB) == 1 || totalA.compareTo(totalB) == 0) {
                        transformerMeter.setCurrentQuantity(totalA.subtract(totalB));
                    }
                }
                transformerMeter.setCollectTime(date);
                transformerMeterService.save(transformerMeter);
            }
        });

        //配电箱用电采集
        highDistributions.forEach(b -> {
            var propParam = new DevicePropParam();
            propParam.setDeviceId(b);
            propParam.setPropertyCodes("ic,ib,EPi");
            var keyValueWithTsDTOS = iotDevicePropClient.deviceProp(propParam);

            if (CollectionUtils.isNotEmpty(keyValueWithTsDTOS)) {
                var powerDistributionBox = new PowerDistributionBox();
                powerDistributionBox.setStatisticalMethod(StatisticsType.ELECTRICITY.name());
                powerDistributionBox.setDeviceId(Long.valueOf(b));

                //电流读数a
                var boxElectricA = new BigDecimal("0.00");
                //电表读数b
                var boxElectricB = new BigDecimal("0.00");
                //电表读数b
                var boxElectricTotal = new BigDecimal("0.00");
                for (KeyValueWithTsDTO a : keyValueWithTsDTOS) {
                    if (a.getKey().equals("ic")) {
                        boxElectricA = boxElectricA.add(
                                a.getValue().equals("empty") ? new BigDecimal("0.00") : new BigDecimal(a.getValue().toString()));
                    }
                    if (a.getKey().equals("ib")) {
                        boxElectricB = boxElectricB.add(
                                a.getValue().equals("empty") ? new BigDecimal("0.00") : new BigDecimal(a.getValue().toString()));
                    }
                    if (a.getKey().equals("EPi")) {
                        boxElectricTotal = boxElectricTotal.add(
                                a.getValue().equals("empty") ? new BigDecimal("0.00") : new BigDecimal(a.getValue().toString()));
                    }
                }

                powerDistributionBox.setMeterA(boxElectricA);
                powerDistributionBox.setMeterB(boxElectricB);
                powerDistributionBox.setBoxElectricTotal(boxElectricTotal);
                //电量计算
                var lastOne = powerDistributionBoxService.getLastOne(b);
                if (Objects.isNull(lastOne)) {
                    powerDistributionBox.setCurrentQuantity(new BigDecimal("0.00"));
                } else {
                    var totalA = boxElectricA.add(boxElectricB);
                    var totalB = lastOne.getMeterA().add(lastOne.getMeterB());
                    if (totalA.compareTo(totalB) == 1 || totalA.compareTo(totalB) == 0) {
                        powerDistributionBox.setCurrentQuantity(totalA.subtract(totalB));
                    }
                }
                powerDistributionBox.setCollectTime(date);
                powerDistributionBoxService.save(powerDistributionBox);
            }
        });

    }

    /**
     * @Description 配电采集
     * @Author lrj
     */
    public void addDistribution(String param) {
        try {
            SessionContextHolder.requestTenant(Integer.valueOf(param));
            var date = LocalDateTime.now();
            var list = transformerDistributionService.list();
            var highDistributions = list.stream().map(TransformerDistribution::getHighDistribution).collect(Collectors.toSet());
            //变压器配电读数采集
            list.forEach(s -> {
                var propParam = new DevicePropParam();
                propParam.setDeviceId(s.getTransformer());
                propParam.setPropertyCodes("ic,ib,ia,temp_c,temp_b,temp_a,power_factor");
                var keyValueWithTsDTOS = iotDevicePropClient.deviceProp(propParam);

                if (CollectionUtils.isNotEmpty(keyValueWithTsDTOS)) {
                    var transformerMeter = new TransformerMeter();
                    transformerMeter.setStatisticalMethod(StatisticsType.DISTRIBUTION.name());
                    transformerMeter.setDeviceId(Long.valueOf(s.getTransformer()));
                    //a相电流
                    var tMutuallyElectricA = new BigDecimal("0.00");
                    //a相温升
                    var tMutuallyTemperatureA = new BigDecimal("0.00");
                    //b相电流
                    var tMutuallyElectricB = new BigDecimal("0.00");
                    //b相温升
                    var tMutuallyTemperatureB = new BigDecimal("0.00");
                    //c相电流
                    var tMutuallyElectricC = new BigDecimal("0.00");
                    //c相温升
                    var tMutuallyTemperatureC = new BigDecimal("0.00");
                    //功率因素
                    var power = new BigDecimal("0.00");
                    for (KeyValueWithTsDTO a : keyValueWithTsDTOS) {
                        if (a.getKey().equals("ic")) {
                            tMutuallyElectricA = tMutuallyElectricA.add(
                                    a.getValue().equals("empty") ? new BigDecimal("0.00") : new BigDecimal(a.getValue().toString()));
                        }
                        if (a.getKey().equals("temp_c")) {
                            tMutuallyTemperatureA = tMutuallyTemperatureA.add(
                                    a.getValue().equals("empty") ? new BigDecimal("0.00") : new BigDecimal(a.getValue().toString()));
                        }
                        if (a.getKey().equals("ib")) {
                            tMutuallyElectricB = tMutuallyElectricB.add(
                                    a.getValue().equals("empty") ? new BigDecimal("0.00") : new BigDecimal(a.getValue().toString()));
                        }
                        if (a.getKey().equals("temp_b")) {
                            tMutuallyTemperatureB = tMutuallyTemperatureB.add(
                                    a.getValue().equals("empty") ? new BigDecimal("0.00") : new BigDecimal(a.getValue().toString()));
                        }
                        if (a.getKey().equals("ia")) {
                            tMutuallyElectricC = tMutuallyElectricC.add(
                                    a.getValue().equals("empty") ? new BigDecimal("0.00") : new BigDecimal(a.getValue().toString()));
                        }
                        if (a.getKey().equals("temp_a")) {
                            tMutuallyTemperatureC = tMutuallyTemperatureC.add(
                                    a.getValue().equals("empty") ? new BigDecimal("0.00") : new BigDecimal(a.getValue().toString()));
                        }
                        if (a.getKey().equals("power_factor")) {
                            power = power.add(
                                    a.getValue().equals("empty") ? new BigDecimal("0.00") : new BigDecimal(a.getValue().toString()));
                        }
                    }

                    transformerMeter.setElectricA(tMutuallyElectricA);
                    transformerMeter.setTemperatureA(tMutuallyTemperatureA);
                    transformerMeter.setElectricB(tMutuallyElectricB);
                    transformerMeter.setTemperatureB(tMutuallyTemperatureB);
                    transformerMeter.setElectricC(tMutuallyElectricC);
                    transformerMeter.setTemperatureC(tMutuallyTemperatureC);
                    transformerMeter.setPowerFactor(power);
                    transformerMeter.setCollectTime(date);
                    transformerMeter.setDistributionDevice(s.getHighDistribution());
                    transformerMeterService.save(transformerMeter);
                }
            });

            //配电箱配电采集
            highDistributions.forEach(b -> {
                var propParam = new DevicePropParam();
                propParam.setDeviceId(b);
                propParam.setPropertyCodes("ua,ub,uc");
                var keyValueWithTsDTOS = iotDevicePropClient.deviceProp(propParam);

                if (CollectionUtils.isNotEmpty(keyValueWithTsDTOS)) {
                    var powerDistributionBox = new PowerDistributionBox();
                    powerDistributionBox.setStatisticalMethod(StatisticsType.DISTRIBUTION.name());
                    powerDistributionBox.setDeviceId(Long.valueOf(b));

                    //电压读数a
                    var boxVoltageA = new BigDecimal("0.00");
                    //电压读数b
                    var boxVoltageB = new BigDecimal("0.00");
                    //电压读数c
                    var boxVoltageC = new BigDecimal("0.00");
                    for (KeyValueWithTsDTO a : keyValueWithTsDTOS) {
                        if (a.getKey().equals("ua")) {
                            boxVoltageA = boxVoltageA.add(
                                    a.getValue().equals("empty") ? new BigDecimal("0.00") : new BigDecimal(a.getValue().toString()));
                        }
                        if (a.getKey().equals("ub")) {
                            boxVoltageB = boxVoltageB.add(
                                    a.getValue().equals("empty") ? new BigDecimal("0.00") : new BigDecimal(a.getValue().toString()));
                        }
                        if (a.getKey().equals("uc")) {
                            boxVoltageC = boxVoltageC.add(
                                    a.getValue().equals("empty") ? new BigDecimal("0.00") : new BigDecimal(a.getValue().toString()));
                        }
                    }

                    powerDistributionBox.setVoltageA(boxVoltageA);
                    powerDistributionBox.setVoltageB(boxVoltageB);
                    powerDistributionBox.setVoltageC(boxVoltageC);
                    powerDistributionBox.setCollectTime(date);
                    powerDistributionBoxService.save(powerDistributionBox);
                }
            });
        } catch (Exception e) {
            log.error("配电采集失败======：", e);
        }
    }

}
