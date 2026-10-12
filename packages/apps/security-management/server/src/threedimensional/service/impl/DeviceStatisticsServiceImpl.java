package com.guhecloud.cube.srv.gen.securitymanagement.server.threedimensional.service.impl;

import com.guhecloud.cube.base.alarm.client.AlarmBimStatisticsClient;
import com.guhecloud.cube.base.alarm.client.param.AlarmBimStatisticsQueryParam;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceBimSceneStdClient;
import com.guhecloud.cube.srv.gen.securitymanagement.server.threedimensional.param.DeviceStatisticsParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.threedimensional.service.DeviceStatisticsService;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceStatisticsServiceImpl implements DeviceStatisticsService {

    private final AlarmBimStatisticsClient alarmBimStatisticsClient;

    private final DeviceBimSceneStdClient deviceBimSceneStdClient;

    @Override
    public Map<String, Object> getEquipmentAlarmRate(DeviceStatisticsParam param) {
        var date = LocalDateTime.now();
        String startTime = "";
        String endTime = "";
        String startOldTime = "";
        String endOldTime = "";
        if (param.getType().equals("TODAY")) {
            var dateTime = DateUtils.beginOfDay(date);
            startTime = DateUtils.format(dateTime, "yyyy-MM-dd HH:mm:ss");
            endTime = DateUtils.format(date, "yyyy-MM-dd HH:mm:ss");
            var offset = DateUtils.offset(dateTime, ChronoUnit.HOURS, -24);
            startOldTime = DateUtils.format(offset, "yyyy-MM-dd HH:mm:ss");
            var offset1 = DateUtils.offset(date, ChronoUnit.HOURS, -24);
            endOldTime = DateUtils.format(offset1, "yyyy-MM-dd HH:mm:ss");
        } else if (param.getType().equals("THIS_WEEK")) {
            var dateTime = DateUtils.beginOfWeek(date);
            startTime = DateUtils.format(dateTime, "yyyy-MM-dd HH:mm:ss");
            endTime = DateUtils.format(date, "yyyy-MM-dd HH:mm:ss");
            var offset = DateUtils.offset(dateTime, ChronoUnit.WEEKS, -1);
            startOldTime = DateUtils.format(offset, "yyyy-MM-dd HH:mm:ss");
            var offset1 = DateUtils.offset(date, ChronoUnit.WEEKS, -1);
            endOldTime = DateUtils.format(offset1, "yyyy-MM-dd HH:mm:ss");
        } else if (param.getType().equals("THIS_MONTH")) {
            var dateTime = DateUtils.beginOfMonth(date);
            startTime = DateUtils.format(dateTime, "yyyy-MM-dd HH:mm:ss");
            endTime = DateUtils.format(date, "yyyy-MM-dd HH:mm:ss");
            var offset = DateUtils.offset(dateTime, ChronoUnit.MONTHS, -1);
            startOldTime = DateUtils.format(offset, "yyyy-MM-dd HH:mm:ss");
            var offset1 = DateUtils.offset(date, ChronoUnit.MONTHS, -1);
            endOldTime = DateUtils.format(offset1, "yyyy-MM-dd HH:mm:ss");
        } else if (param.getType().equals("THIS_YEAR")) {
            var dateTime = DateUtils.beginOfYear(date);
            startTime = DateUtils.format(dateTime, "yyyy-MM-dd HH:mm:ss");
            endTime = DateUtils.format(date, "yyyy-MM-dd HH:mm:ss");
            var offset = DateUtils.offset(dateTime, ChronoUnit.YEARS, -1);
            startOldTime = DateUtils.format(offset, "yyyy-MM-dd HH:mm:ss");
            var offset1 = DateUtils.offset(date, ChronoUnit.YEARS, -1);
            endOldTime = DateUtils.format(offset1, "yyyy-MM-dd HH:mm:ss");
        }
        //获取场景
        Set<Long> distinctIds = deviceBimSceneStdClient.getBimDeviceIds(param.getSceneType(), param.getAreaId());
        if (CollectionUtils.isEmpty(distinctIds)) {
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("deviceTotal", 0);
            hashMap.put("alarmNum", 0);
            hashMap.put("proportion", 0);
            hashMap.put("chainRatio", 0);
            return hashMap;
        }

        //报警数
        AlarmBimStatisticsQueryParam alarmBimStatisticsQueryParam = new AlarmBimStatisticsQueryParam();
        alarmBimStatisticsQueryParam.setAreaId(param.getAreaId());
        alarmBimStatisticsQueryParam.setSceneType(param.getSceneType());
        alarmBimStatisticsQueryParam.setPeriod(startTime + "," + endTime);
        //当前的数据
        Integer alarmTargetCount = alarmBimStatisticsClient.getAlarmTargetCount(alarmBimStatisticsQueryParam);
        //同比数据的数据
        AlarmBimStatisticsQueryParam alarmBimStatisticsQueryParam1 = new AlarmBimStatisticsQueryParam();
        alarmBimStatisticsQueryParam1.setAreaId(param.getAreaId());
        alarmBimStatisticsQueryParam1.setSceneType(param.getSceneType());
        alarmBimStatisticsQueryParam1.setPeriod(startOldTime + "," + endOldTime);
        Integer alarmTargetCount1 = alarmBimStatisticsClient.getAlarmTargetCount(alarmBimStatisticsQueryParam1);
        Map<String, Object> remap = new HashMap<>();
        remap.put("deviceTotal", distinctIds.size());
        remap.put("alarmNum", alarmTargetCount);
        BigDecimal newProportion = new BigDecimal(0);
        if (CollectionUtils.isNotEmpty(distinctIds) && distinctIds.size() > 0 && alarmTargetCount != null) {
            newProportion = BigDecimal.valueOf(alarmTargetCount * 1.0F / distinctIds.size());
        }
        remap.put("proportion", newProportion);
        BigDecimal oldProportion = new BigDecimal(0);
        if (CollectionUtils.isNotEmpty(distinctIds) && distinctIds.size() > 0 && alarmTargetCount1 != null) {
            oldProportion = BigDecimal.valueOf(alarmTargetCount1 * 1.0F / distinctIds.size());
        }
        String chainRatio = "";
        if (newProportion.compareTo(oldProportion) > 0) {
            chainRatio = newProportion.subtract(oldProportion).setScale(2, BigDecimal.ROUND_DOWN).toString();
        } else if (newProportion.compareTo(oldProportion) < 0) {
            chainRatio = oldProportion.subtract(newProportion).setScale(2, BigDecimal.ROUND_DOWN).toString();
        } else {
            chainRatio = "0";
        }
        remap.put("chainRatio", chainRatio);
        return remap;
    }

}
