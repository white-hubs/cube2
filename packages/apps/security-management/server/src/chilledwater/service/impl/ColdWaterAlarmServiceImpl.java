package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.alarm.client.AlarmRecordClient;
import com.guhecloud.cube.base.alarm.client.dto.AlarmDeviceStatusStatisticsPageDTO;
import com.guhecloud.cube.base.alarm.client.param.AlarmDeviceStatusTotalQueryParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.AlarmDeviceStatusParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.ColdWaterAlarmService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.DevicePositionService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.PositionInfoVo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ColdWaterAlarmServiceImpl implements ColdWaterAlarmService {
    private final AlarmRecordClient alarmRecordClient;

    private final DevicePositionService devicePositionService;

    @Override
    public Pagination<AlarmDeviceStatusStatisticsPageDTO> getDeviceAlarmStatusTotalPage(AlarmDeviceStatusParam param) {

        List<Long> deviceIds = devicePositionService.getDeviceIds(param.getKeyword());
        if (CollectionUtils.isNotEmpty(deviceIds)) {
            AlarmDeviceStatusTotalQueryParam alarmDeviceStatusTotalQueryParam = ObjectUtils.copying(param, AlarmDeviceStatusTotalQueryParam.class);
            alarmDeviceStatusTotalQueryParam.setApplicationCode("COOLINGWATER");
            alarmDeviceStatusTotalQueryParam.setIds(deviceIds);
            Pagination<AlarmDeviceStatusStatisticsPageDTO> deviceAlarmStatusTotalPage = alarmRecordClient.getDeviceAlarmStatusTotalPage(alarmDeviceStatusTotalQueryParam);
            if (CollectionUtils.isNotEmpty(deviceAlarmStatusTotalPage.getRecords())) {
                List<AlarmDeviceStatusStatisticsPageDTO> records = deviceAlarmStatusTotalPage.getRecords();
                if (CollectionUtils.isNotEmpty(records)) {
                    List<Long> collect = records.stream().map(AlarmDeviceStatusStatisticsPageDTO::getDeviceId).collect(Collectors.toList());
                    List<PositionInfoVo> positionInfo = devicePositionService.getPositionInfo(collect);
                    Map<Long, PositionInfoVo> positionInfoVoMap = positionInfo.stream()
                            .collect(Collectors.toMap(PositionInfoVo::getDeviceId, s -> s));
                    for (AlarmDeviceStatusStatisticsPageDTO record : records) {
                        PositionInfoVo positionInfoVo = positionInfoVoMap.get(record.getDeviceId());
                        record.setAreaName(positionInfoVo.getGearPositionArea());
                        record.setGearPositionCode(positionInfoVo.getGearPositionCode());
                    }
                }

            }
            return deviceAlarmStatusTotalPage;
        } else {
            return PagingUtils.convert(new Page<>(), AlarmDeviceStatusStatisticsPageDTO.class);
        }

    }

    @Override
    public List<AlarmDeviceStatusStatisticsPageDTO> getDeviceAlarmStatusTotalExcel(AlarmDeviceStatusParam param) {
        List<Long> deviceIds = devicePositionService.getDeviceIds(param.getKeyword());
        List<AlarmDeviceStatusStatisticsPageDTO> alarmDeviceStatusStatisticsPageDTOS = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(deviceIds)) {
            AlarmDeviceStatusTotalQueryParam alarmDeviceStatusTotalQueryParam = ObjectUtils.copying(param, AlarmDeviceStatusTotalQueryParam.class);
            alarmDeviceStatusTotalQueryParam.setApplicationCode("COOLINGWATER");
            alarmDeviceStatusTotalQueryParam.setIds(deviceIds);
            alarmDeviceStatusStatisticsPageDTOS = alarmRecordClient.exportDeviceAlarmStatusTotalList(alarmDeviceStatusTotalQueryParam);
            if (CollectionUtils.isNotEmpty(alarmDeviceStatusStatisticsPageDTOS)) {
                if (CollectionUtils.isNotEmpty(alarmDeviceStatusStatisticsPageDTOS)) {
                    List<Long> collect = alarmDeviceStatusStatisticsPageDTOS.stream().map(AlarmDeviceStatusStatisticsPageDTO::getDeviceId)
                            .collect(Collectors.toList());
                    List<PositionInfoVo> positionInfo = devicePositionService.getPositionInfo(collect);
                    Map<Long, PositionInfoVo> positionInfoVoMap = positionInfo.stream()
                            .collect(Collectors.toMap(PositionInfoVo::getDeviceId, s -> s));
                    for (AlarmDeviceStatusStatisticsPageDTO record : alarmDeviceStatusStatisticsPageDTOS) {
                        PositionInfoVo positionInfoVo = positionInfoVoMap.get(record.getDeviceId());
                        record.setAreaName(positionInfoVo.getGearPositionArea());
                        record.setGearPositionCode(positionInfoVo.getGearPositionCode());
                    }
                }

            }
        }
        return alarmDeviceStatusStatisticsPageDTOS;
    }
}
