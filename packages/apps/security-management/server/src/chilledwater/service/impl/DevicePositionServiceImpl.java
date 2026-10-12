package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.lang.Pair;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.iot.north.bridge.client.IotDeviceClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.DeviceDetailDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.KeyValueWithTsDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.ProductPropDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.DeviceDetailParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.dao.ColdWholesalerMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.dao.DevicePositionMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.dao.GearPositionMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.ColdWholesaler;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.DevicePosition;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.GearPosition;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.WaterDeviceMonitorParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.WholesalerDetailParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.DevicePositionService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.DeviceAttributeCodeVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.DeviceAttributeVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.DeviceDetailVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.DevicePositionPageVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.PositionInfoVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.WholesalerDeviceVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.dao.SecurityCommonMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity.SecurityDevice;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DevicePositionServiceImpl extends ServiceImpl<DevicePositionMapper, DevicePosition> implements DevicePositionService {

    private final DevicePositionMapper devicePositionMapper;
    private final ColdWholesalerMapper coldWholesalerMapper;
    private final GearPositionMapper gearPositionMapper;
    private final UserClient userClient;
    private final SecurityCommonMapper securityCommonMapper;
    private final FileClient fileClient;
    private final IotDeviceClient iotDeviceClient;
    private final DeviceCenterStdClient deviceCenterStdClient;
    private final AreaClient areaClient;

    @Override
    public Pagination<DevicePositionPageVo> getDeviceMonitor(WaterDeviceMonitorParam param) {
        Page<DevicePositionPageVo> deviceMonitor = devicePositionMapper.getDeviceMonitor(PagingUtils.convert(param), param);
        if (CollectionUtils.isNotEmpty(deviceMonitor.getRecords())) {
            for (DevicePositionPageVo record : deviceMonitor.getRecords()) {
                if (StringUtils.isNotBlank(record.getMapCode()) && record.getMapCode().equals("chilled-water-timer")) {
                    Long deviceId = record.getDeviceId();
                    DeviceDetailParam deviceAttributeParam = new DeviceDetailParam();
                    deviceAttributeParam.setDeviceId(deviceId.toString());
                    deviceAttributeParam.setWithSnapShot(true);
                    DeviceDetailDTO deviceDetailDTO = null;
                    try {
                        deviceDetailDTO = iotDeviceClient.detail(deviceAttributeParam);
                    } catch (Exception e) {
                        log.info("设备问题=={}", e);
                    }
                    if (Objects.nonNull(deviceDetailDTO)) {
                        List<KeyValueWithTsDTO> snapshots = deviceDetailDTO.getSnapshots();
                        for (KeyValueWithTsDTO deviceAttribute : snapshots) {
                            if (deviceAttribute.getKey().equals("ddc_timer")) {
                                String status = Objects.isNull(deviceAttribute.getValue()) ? "0" : deviceAttribute.getValue().toString();
                                Integer integer = Integer.valueOf(status);
                                record.setDeviceStatus(integer.equals(1));
                            } else if (deviceAttribute.getKey().equals("re_frigeration")) {
                                String status = Objects.isNull(deviceAttribute.getValue()) ? "0" : deviceAttribute.getValue().toString();
                                Integer integer = Integer.valueOf(status);
                                record.setSolenoidValve(integer.equals(1) ? "开" : "关");
                            }

                        }
                    }

                    List<FileVO> byBusinessKey = fileClient.getByBusinessKey(record.getDeviceId());
                    record.setImages(byBusinessKey);
                }

            }
        }
        return PagingUtils.convert(deviceMonitor);
    }

    @Override
    public DeviceDetailVo getDetail(Long id) {
        SecurityDevice securityDevice = securityCommonMapper.selectOne(Wrappers.<SecurityDevice>lambdaQuery()
                                                                               .eq(SecurityDevice::getDeviceId, id)
                                                                               .eq(SecurityDevice::getApplicationCode, "COOLINGWATER"));
        DeviceDetailVo deviceDetailVo = ObjectUtils.copying(securityDevice, DeviceDetailVo.class);
        DevicePosition devicePosition = devicePositionMapper.selectOne(Wrappers.<DevicePosition>lambdaQuery()
                                                                               .eq(DevicePosition::getColdWaterDevice, id));
        if (Objects.nonNull(devicePosition)) {
            deviceDetailVo.setUnitTraffic(devicePosition.getUnitTraffic());
            List<GearPosition> gearPositions = gearPositionMapper.selectList(Wrappers.<GearPosition>lambdaQuery()
                                                                                     .eq(GearPosition::getGearPositionId, devicePosition.getGearPositionId()));
            if (CollectionUtils.isNotEmpty(gearPositions)) {
                List<ColdWholesaler> coldWholesalers = coldWholesalerMapper.selectList(Wrappers.<ColdWholesaler>lambdaQuery()
                                                                                               .eq(ColdWholesaler::getWholesalerId, gearPositions
                                                                                                       .get(0).getWholesaler()));
                if (CollectionUtils.isNotEmpty(coldWholesalers)) {
                    deviceDetailVo.setShiftMaster(coldWholesalers.get(0).getShiftMaster());
                }
            }
            deviceDetailVo.setGearPositionCode(devicePosition.getGearPositionCode());
        }
        //图片
        com.guhecloud.cube.base.device.center.metadata.dto.DeviceDetailDTO deviceById = deviceCenterStdClient.getDeviceById(id);
        DeviceProductInfoDTO productInfo = deviceById.getProductInfo();
        if (Objects.nonNull(productInfo.getPicture())) {
            FileVO byId = fileClient.getById(productInfo.getPicture());
            if (Objects.nonNull(byId)) {
                List<FileVO> fileVOS = new ArrayList<>();
                fileVOS.add(byId);
                deviceDetailVo.setImages(fileVOS);
            }
        }
        //品类
        if (Objects.nonNull(productInfo.getProductCategory())) {
            deviceDetailVo.setGenreName(productInfo.getProductCategory().getName());
        }
        //区域
        DeviceBaseArchivesInfoDTO baseArchivesInfo = deviceById.getBaseArchivesInfo();
        Pair<Long, String> area = baseArchivesInfo.getArea();
        if (Objects.nonNull(area)) {
            Long areaId = area.getKey();
            AreaDTO areaById = areaClient.getAreaById(areaId);
            if (Objects.nonNull(areaById)) {
                String areaNamesById = areaClient.getAreaNamesById(areaId);
                String[] split = areaNamesById.split("/");
                List<String> strings = Arrays.asList(split);
                String areaName = strings.stream().collect(Collectors.joining("-"));
                deviceDetailVo.setAreaName(areaName);
            }
        }

        return deviceDetailVo;
    }

    @Override
    public Pagination<WholesalerDeviceVo> getWholesalerDetail(WholesalerDetailParam param) {
        Page<WholesalerDeviceVo> wholesalerDetails = devicePositionMapper.getWholesalerDetail(PagingUtils.convert(param), param);
        if (CollectionUtils.isNotEmpty(wholesalerDetails.getRecords())) {
            for (WholesalerDeviceVo record : wholesalerDetails.getRecords()) {
                if (StringUtils.isNotEmpty(record.getUpdateBy())) {
                    UserDTO userByUserName = userClient.getUserByUserName(record.getUpdateBy());
                    record.setUpdateBy(userByUserName.getName());
                }
            }
        }

        return PagingUtils.convert(wholesalerDetails);
    }

    @Override
    public List<WholesalerDeviceVo> wholesalerDetailExcel(WholesalerDetailParam param) {
        List<WholesalerDeviceVo> wholesalerDetails = devicePositionMapper.wholesalerDetailExcel(param);
        Integer num = 0;
        if (CollectionUtils.isNotEmpty(wholesalerDetails)) {
            for (WholesalerDeviceVo wholesalerDetail : wholesalerDetails) {
                num += 1;
                wholesalerDetail.setNum(num);
                if (StringUtils.isNotEmpty(wholesalerDetail.getUpdateBy())) {
                    UserDTO userByUserName = userClient.getUserByUserName(wholesalerDetail.getUpdateBy());
                    wholesalerDetail.setUpdateBy(userByUserName.getName());
                }
            }
        }
        return wholesalerDetails;
    }

    @Override
    public DeviceAttributeVo getAttribute(Long id) {
        DeviceDetailParam deviceDetailParam = new DeviceDetailParam();
        deviceDetailParam.setDeviceId(id.toString());
        deviceDetailParam.setWithSnapShot(true);
        DeviceDetailDTO detail = iotDeviceClient.detail(deviceDetailParam);
        List<KeyValueWithTsDTO> snapshots = detail.getSnapshots();
        DeviceAttributeVo deviceAttributeVo = new DeviceAttributeVo();
        if (CollectionUtils.isNotEmpty(snapshots)) {
            Map<String, KeyValueWithTsDTO> collect = snapshots.stream().collect(Collectors.toMap(KeyValueWithTsDTO::getKey, s -> s));

            KeyValueWithTsDTO control_box = collect.get("control_box");
            if (Objects.nonNull(control_box)) {
                Integer integer = Objects.isNull(control_box.getValue()) ? 0 : Integer.valueOf(control_box.getValue().toString());
                deviceAttributeVo.setControlBox(integer.equals(1) ? "开" : "关");
            }

            KeyValueWithTsDTO water_flow = collect.get("water_flow");
            if (Objects.nonNull(water_flow)) {
                Integer integer1 = Integer.valueOf(Objects.isNull(water_flow.getValue()) ? "0" : water_flow.getValue().toString());
                deviceAttributeVo.setWaterFlow(integer1.equals(1) ? "报警" : "正常");
            }

            KeyValueWithTsDTO control_box_temperture = collect.get("control_box_temperture");
            if (Objects.nonNull(control_box_temperture)) {
                deviceAttributeVo.setControlBoxTemperture(
                        Objects.isNull(control_box_temperture.getValue()) ? "0" : control_box_temperture.getValue().toString());

            }

            KeyValueWithTsDTO re_frigeration = collect.get("re_frigeration");
            if (Objects.nonNull(re_frigeration)) {
                Integer integer2 = Integer.valueOf(Objects.isNull(re_frigeration.getValue()) ? "0" : re_frigeration.getValue().toString());
                deviceAttributeVo.setReFrigeration(integer2.equals(1) ? "打开" : "关闭");
            }

            KeyValueWithTsDTO o_ti_bubbles_temperature = collect.get("o_ti_bubbles_temperature");
            if (Objects.nonNull(o_ti_bubbles_temperature)) {
                deviceAttributeVo.setOTiBubblesTemperature(o_ti_bubbles_temperature.getValue().toString());
            }

            KeyValueWithTsDTO ti_bubbles_temperature = collect.get("ti_bubbles_temperature");
            if (Objects.nonNull(ti_bubbles_temperature)) {
                deviceAttributeVo.setTiBubblesTemperature(ti_bubbles_temperature.getValue().toString());
            }

            KeyValueWithTsDTO o_water_temperature = collect.get("o_water_temperature");
            if (Objects.nonNull(o_water_temperature)) {
                deviceAttributeVo.setOWaterTemperature(o_water_temperature.getValue().toString());
            }

            KeyValueWithTsDTO water_temperature = collect.get("water_temperature");
            if (Objects.nonNull(water_temperature)) {
                deviceAttributeVo.setWaterTemperature(water_temperature.getValue().toString());
            }

            KeyValueWithTsDTO day_time = collect.get("day_time");
            if (Objects.nonNull(day_time)) {
                BigDecimal bigDecimal = new BigDecimal(Objects.isNull(day_time.getValue()) ? "0" : day_time.getValue().toString());
                BigDecimal divide = bigDecimal.divide(new BigDecimal(60), 2, RoundingMode.UP);
                deviceAttributeVo.setDayTime(divide.setScale(2, RoundingMode.UP).toString());
            }

            KeyValueWithTsDTO month_time = collect.get("month_time");
            if (Objects.nonNull(month_time)) {
                BigDecimal bigDecimal = new BigDecimal(Objects.isNull(month_time.getValue()) ? "0" : month_time.getValue().toString());
                BigDecimal divide = bigDecimal.divide(new BigDecimal(60), 2, RoundingMode.UP);
                deviceAttributeVo.setMonthTime(divide.setScale(2, RoundingMode.UP).toString());
            }

            KeyValueWithTsDTO ti_bubbles_lower = collect.get("ti_bubbles_lower");
            if (Objects.nonNull(ti_bubbles_lower)) {
                deviceAttributeVo.setTiBubblesLower(ti_bubbles_lower.getValue().toString());
            }

            KeyValueWithTsDTO a_alarm_water = collect.get("a_alarm_water");
            if (Objects.nonNull(a_alarm_water)) {
                String originalValue = a_alarm_water.getValue().toString();
                Integer integer3 = Integer.valueOf(StringUtils.isEmpty(originalValue) ? "0" : originalValue);
                deviceAttributeVo.setAAlarmWater(integer3.equals(1) ? "报警" : "正常");
            }

            KeyValueWithTsDTO a_alarm_temperature2 = collect.get("a_alarm_temperature2");
            Integer integer4 = 0;
            if (Objects.nonNull(a_alarm_temperature2)) {
                integer4 = Integer.valueOf(
                        Objects.isNull(a_alarm_temperature2.getValue()) ? "0" : a_alarm_temperature2.getValue().toString());
            }

            KeyValueWithTsDTO a_alarm_temperature13 = collect.get("a_alarm_temperature13");
            Integer integer5 = 0;
            if (Objects.nonNull(a_alarm_temperature13)) {
                integer5 = Integer.valueOf(
                        Objects.isNull(a_alarm_temperature13.getValue()) ? "0" : a_alarm_temperature13.getValue().toString());
            }

            KeyValueWithTsDTO a_alarm_temperature14 = collect.get("a_alarm_temperature14");
            Integer integer6 = 0;
            if (Objects.nonNull(a_alarm_temperature14)) {
                integer6 = Integer.valueOf(
                        Objects.isNull(a_alarm_temperature14.getValue()) ? "0" : a_alarm_temperature14.getValue().toString());
            }

            KeyValueWithTsDTO a_alarm_temperature15 = collect.get("a_alarm_temperature15");
            Integer integer7 = 0;
            if (Objects.nonNull(a_alarm_temperature15)) {
                integer7 = Integer.valueOf(
                        Objects.isNull(a_alarm_temperature15.getValue()) ? "0" : a_alarm_temperature15.getValue().toString());
            }

            KeyValueWithTsDTO a_alarm_temperature16 = collect.get("a_alarm_temperature16");
            Integer integer8 = 0;
            if (Objects.nonNull(a_alarm_temperature16)) {
                integer8 = Integer.valueOf(
                        Objects.isNull(a_alarm_temperature16.getValue()) ? "0" : a_alarm_temperature16.getValue().toString());

            }
            List<String> objects = new ArrayList<>();
            if (integer4.equals(1)) {
                objects.add(a_alarm_temperature2.getValue().toString());
            }
            if (integer5.equals(1)) {
                objects.add(a_alarm_temperature13.getValue().toString());
            }
            if (integer6.equals(1)) {
                objects.add(a_alarm_temperature14.getValue().toString());
            }
            if (integer7.equals(1)) {
                objects.add(a_alarm_temperature15.getValue().toString());
            }
            if (integer8.equals(1)) {
                objects.add(a_alarm_temperature16.getValue().toString());
            }
            String collect1 = objects.stream().collect(Collectors.joining("/"));
            deviceAttributeVo.setAlarm(collect1);
            KeyValueWithTsDTO ddc_timer = collect.get("ddc_timer");
            if (Objects.nonNull(ddc_timer)) {
                String originalValue1 = ddc_timer.getValue().toString();
                Integer integer9 = Integer.valueOf(StringUtils.isEmpty(originalValue1) ? "0" : originalValue1);
                deviceAttributeVo.setStatus(integer9.equals(1) ? "开" : "关");
            }

        }

        return deviceAttributeVo;
    }

    @Override
    public List<Long> getDeviceIds(String keyword) {
        List<Long> deviceIds = devicePositionMapper.getDeviceIds(keyword);
        return deviceIds;
    }

    @Override
    public List<PositionInfoVo> getPositionInfo(List<Long> ids) {

        return devicePositionMapper.getPositionInfo(ids);
    }

    @Override
    public List<DeviceAttributeCodeVo> getDeviceAttributeCode(Long deviceId) {
        DeviceDetailParam deviceDetailParam = new DeviceDetailParam();
        deviceDetailParam.setDeviceId(deviceId.toString());
        deviceDetailParam.setWithPropertySpec(true);
        DeviceDetailDTO detail = iotDeviceClient.detail(deviceDetailParam);
        List<DeviceAttributeCodeVo> objects = new ArrayList<>();
        if (Objects.nonNull(detail)) {
            List<ProductPropDTO> productPropVoList = detail.getProductPropVoList();

            if (CollectionUtils.isNotEmpty(productPropVoList)) {
                for (ProductPropDTO productPropDTO : productPropVoList) {
                    DeviceAttributeCodeVo deviceAttributeCodeVo = new DeviceAttributeCodeVo();
                    deviceAttributeCodeVo.setAttributeName(productPropDTO.getPropName());
                    deviceAttributeCodeVo.setAttributeCode(productPropDTO.getPropCode());
                    deviceAttributeCodeVo.setDataType(productPropDTO.getDataType());
                    objects.add(deviceAttributeCodeVo);
                }
            }
        }

        return objects;
    }

    @Override
    public List<Long> getGearPositionIds() {
        List<DevicePosition> devicePositions = devicePositionMapper.selectList(null);
        List<Long> collect = devicePositions.stream().map(DevicePosition::getGearPositionId).distinct().collect(Collectors.toList());
        return collect;
    }
}
