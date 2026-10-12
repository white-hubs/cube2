package com.guhecloud.cube.customization.xialing.server.liangcang.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.guhecloud.cube.base.alarm.client.AlarmRecordClient;
import com.guhecloud.cube.base.alarm.client.param.AlarmRecordQueryParam;
import com.guhecloud.cube.base.alarm.client.vo.AlarmRecordPageVO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseDetailsDTO;
import com.guhecloud.cube.base.device.center.metadata.std.param.DevicePostByIdSetParam;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.customization.xialing.server.liangcang.client.WarehousePageApiClient;
import com.guhecloud.cube.customization.xialing.server.liangcang.config.LiangcangLocationCardConfigProvider;
import com.guhecloud.cube.customization.xialing.server.liangcang.dto.LiangcangLocationCardConfigDTO;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseDeviceBinding;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.GasArchiveRecordPageParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.GasArchiveTrendParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.GasArchiveService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseDeviceBindingService;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.GasArchiveBasicInfoVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.GasArchiveRecordExportVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.GasArchiveRecordVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.GasArchiveTrendVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.LiangcangWarehousePageItemVO;
import com.guhecloud.cube.iot.north.bridge.client.IotDevicePropClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.DeviceTSDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.KeyValueWithTsDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.PropertyTsDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.MultiDevicePropParam;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.MultiHistorySampleParam;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.SetUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/** 气体档案服务实现。 */
@Service
@RequiredArgsConstructor
public class GasArchiveServiceImpl implements GasArchiveService {

    private static final String GAS_MONITORING_APPLICATION_CODE = "GAS_MONITORING";
    private static final String STATUS_ALARM = "ALARM";
    private static final int SAMPLE_INTERVAL_MINUTES = 30;
    private static final int MAX_NEAREST_MINUTES = 15;
    private static final int EXPORT_PAGE_SIZE = 1000;
    private static final BigDecimal PPM_TO_PERCENT_VOL = BigDecimal.valueOf(10000);

    private final WarehouseDeviceBindingService warehouseDeviceBindingService;
    private final WarehousePageApiClient warehousePageApiClient;
    private final DeviceCenterStdClient deviceCenterStdClient;
    private final IotDevicePropClient iotDevicePropClient;
    private final LiangcangLocationCardConfigProvider locationCardConfigProvider;
    private final AlarmRecordClient alarmRecordClient;

    @Override
    public GasArchiveBasicInfoVO basicInfo(Long warehouseId) {
        List<DeviceRef> devices = resolveDevices(warehouseId);
        GasArchiveBasicInfoVO result = new GasArchiveBasicInfoVO();
        result.setWarehouseId(warehouseId);
        result.setWarehouseName(resolveWarehouseName(warehouseId));
        if (devices.isEmpty()) {
            return result;
        }
        LiangcangLocationCardConfigDTO config = locationCardConfigProvider.resolve();
        String propCode = trim(config.getGasRealtimePropCode());
        if (propCode.isEmpty()) {
            return result;
        }
        Map<Long, CurrentSample> latestByDevice = loadLatestValues(devices, propCode);
        Map<Long, List<LocalDateTime>> alarmTimes = loadAlarmTimes(devices, new Period(LocalDateTime.now().minusDays(1), LocalDateTime.now()));
        for (GasType type : GasType.values()) {
            CurrentSample sample = devices.stream()
                    .filter(device -> device.type() == type)
                    .map(device -> latestByDevice.get(device.deviceId()))
                    .filter(Objects::nonNull)
                    .max(Comparator.comparing(CurrentSample::time))
                    .orElse(null);
            if (sample == null) {
                continue;
            }
            BigDecimal value = toPercentVol(type, sample.value());
            boolean alarm = isAlarm(sample.deviceId(), sample.time(), alarmTimes);
            if (type == GasType.CO2) {
                result.setCo2(value);
                result.setCo2Alarm(alarm);
            } else if (type == GasType.O2) {
                result.setO2(value);
                result.setO2Alarm(alarm);
            } else if (type == GasType.PH3) {
                result.setPh3(value);
                result.setPh3Alarm(alarm);
            }
        }
        return result;
    }

    @Override
    public GasArchiveTrendVO trend(GasArchiveTrendParam param) {
        LocalDate date = param == null || param.getDate() == null ? LocalDate.now() : param.getDate();
        List<GasArchiveRecordVO> rows = buildRecords(param == null ? null : param.getWarehouseId(),
                                                      date.atStartOfDay(), date.atTime(23, 59, 59));
        var result = new GasArchiveTrendVO();
        result.setDate(date);
        result.setItems(rows.stream().map(this::toTrendItem).toList());
        return result;
    }

    @Override
    public Pagination<GasArchiveRecordVO> recordPage(GasArchiveRecordPageParam param) {
        if (param == null || param.getWarehouseId() == null) {
            return Pagination.empty();
        }
        Period period = normalizePeriod(param.getPeriod());
        List<GasArchiveRecordVO> all = buildRecords(param.getWarehouseId(), period.getBegin(), period.getEnd())
                .stream()
                .filter(GasArchiveServiceImpl::hasGasValue)
                .sorted(Comparator.comparing(GasArchiveRecordVO::getAcquisitionTime).reversed())
                .toList();
        int pageNumber = param.getPageNumber() == null ? 1 : param.getPageNumber();
        int pageSize = param.getPageSize() == null ? 10 : param.getPageSize();
        List<GasArchiveRecordVO> records = com.guhecloud.platform.base.model.Paging.list(all, pageNumber, pageSize);
        long total = all.size();
        return Pagination.<GasArchiveRecordVO>builder()
                .total(total)
                .pages(total == 0 ? 0 : (total + pageSize - 1) / pageSize)
                .records(records)
                .build();
    }

    @Override
    public void exportRecords(HttpServletResponse response, GasArchiveRecordPageParam param) throws IOException {
        if (param == null || param.getWarehouseId() == null) {
            return;
        }
        Period period = normalizePeriod(param.getPeriod());
        List<GasArchiveRecordExportVO> rows = buildRecords(param.getWarehouseId(), period.getBegin(), period.getEnd())
                .stream()
                .filter(GasArchiveServiceImpl::hasGasValue)
                .sorted(Comparator.comparing(GasArchiveRecordVO::getAcquisitionTime).reversed())
                .map(this::toExport)
                .toList();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String fileName = URLEncoder.encode("气体采集记录", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ExcelTypeEnum.XLSX.getValue());
        EasyExcel.write(response.getOutputStream(), GasArchiveRecordExportVO.class)
                .sheet("气体采集记录")
                .doWrite(rows);
    }

    private List<GasArchiveRecordVO> buildRecords(Long warehouseId, LocalDateTime begin, LocalDateTime end) {
        if (warehouseId == null || begin == null || end == null || begin.isAfter(end)) {
            return List.of();
        }
        List<DeviceRef> devices = resolveDevices(warehouseId);
        LiangcangLocationCardConfigDTO config = locationCardConfigProvider.resolve();
        String propCode = trim(config.getGasRealtimePropCode());
        if (devices.isEmpty() || propCode.isEmpty()) {
            return List.of();
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime effectiveEnd = end.isAfter(now) ? now : end;
        LocalDateTime targetBegin = begin.toLocalDate().atStartOfDay();
        if (targetBegin.isAfter(effectiveEnd)) {
            return List.of();
        }
        LocalDateTime dayLastTarget = end.toLocalDate().atTime(23, 30);
        LocalDateTime targetEnd = dayLastTarget.isAfter(effectiveEnd) ? effectiveEnd : dayLastTarget;
        LocalDateTime queryEnd = targetEnd.plusMinutes(MAX_NEAREST_MINUTES);
        if (queryEnd.isAfter(effectiveEnd)) {
            queryEnd = effectiveEnd;
        }
        Period queryPeriod = new Period(targetBegin.minusMinutes(MAX_NEAREST_MINUTES),
                                         queryEnd);
        List<Sample> samples = loadHistory(devices, propCode, queryPeriod);
        Map<Long, List<LocalDateTime>> alarmTimes = loadAlarmTimes(devices, queryPeriod);
        List<GasArchiveRecordVO> records = new ArrayList<>();
        for (LocalDateTime target = targetBegin; !target.isAfter(targetEnd); target = target.plusMinutes(SAMPLE_INTERVAL_MINUTES)) {
            var row = new GasArchiveRecordVO();
            row.setId(warehouseId + "-" + target);
            row.setWarehouseId(warehouseId);
            row.setAcquisitionTime(target);
            for (GasType type : GasType.values()) {
                Sample sample = nearest(samples.stream().filter(item -> item.type() == type).toList(), target);
                if (sample == null) {
                    continue;
                }
                BigDecimal value = toPercentVol(type, sample.value());
                boolean alarm = isAlarm(sample.deviceId(), sample.time(), alarmTimes);
                if (type == GasType.CO2) {
                    row.setCo2(value);
                    row.setCo2Alarm(alarm);
                } else if (type == GasType.O2) {
                    row.setO2(value);
                    row.setO2Alarm(alarm);
                } else if (type == GasType.PH3) {
                    row.setPh3(value);
                    row.setPh3Alarm(alarm);
                }
            }
            records.add(row);
        }
        return records;
    }

    private List<DeviceRef> resolveDevices(Long warehouseId) {
        if (warehouseId == null) {
            return List.of();
        }
        LiangcangLocationCardConfigDTO config = locationCardConfigProvider.resolve();
        if (config == null || ListUtils.isEmpty(config.getGasProductCodes())) {
            return List.of();
        }
        List<WarehouseDeviceBinding> bindings = warehouseDeviceBindingService.lambdaQuery()
                .eq(WarehouseDeviceBinding::getWarehouseId, warehouseId)
                .list();
        if (ListUtils.isEmpty(bindings)) {
            return List.of();
        }
        Set<Long> ids = bindings.stream().map(WarehouseDeviceBinding::getDeviceId)
                .filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, DeviceBaseDetailsDTO> details = loadDeviceDetails(ids);
        return bindings.stream()
                .map(binding -> binding == null ? null : details.get(binding.getDeviceId()))
                .filter(detail -> detail != null && deviceMatches(detail, config.getGasProductCodes()))
                .map(detail -> new DeviceRef(detail.getId(), resolveGasType(detail)))
                .filter(device -> device.type() != null)
                .distinct()
                .toList();
    }

    private Map<Long, DeviceBaseDetailsDTO> loadDeviceDetails(Set<Long> ids) {
        if (SetUtils.isEmpty(ids)) {
            return Map.of();
        }
        var param = new DevicePostByIdSetParam();
        param.setDeviceIdSet(ids);
        List<DeviceBaseDetailsDTO> details = deviceCenterStdClient.postDetailsListByDeviceId(param);
        return ListUtils.isEmpty(details) ? Map.of() : details.stream()
                .filter(item -> item != null && item.getId() != null)
                .collect(Collectors.toMap(DeviceBaseDetailsDTO::getId, item -> item, (first, ignored) -> first));
    }

    private Map<Long, CurrentSample> loadLatestValues(List<DeviceRef> devices, String propCode) {
        String deviceProps = devices.stream().map(device -> device.deviceId() + "." + propCode).collect(Collectors.joining(","));
        var param = new MultiDevicePropParam();
        param.setDevIdAndPropCodes(deviceProps);
        Map<String, List<KeyValueWithTsDTO>> values = iotDevicePropClient.multiProp(param);
        if (MapUtils.isEmpty(values)) {
            return Map.of();
        }
        Map<Long, CurrentSample> result = new HashMap<>();
        for (DeviceRef device : devices) {
            List<KeyValueWithTsDTO> rows = values.get(String.valueOf(device.deviceId()));
            if (ListUtils.isEmpty(rows)) {
                continue;
            }
            rows.stream().filter(item -> item != null && propCode.equals(item.getKey()))
                    .map(item -> toCurrentSample(device.deviceId(), item))
                    .filter(Objects::nonNull)
                    .max(Comparator.comparing(CurrentSample::time))
                    .ifPresent(item -> result.put(device.deviceId(), item));
        }
        return result;
    }

    private List<Sample> loadHistory(List<DeviceRef> devices, String propCode, Period period) {
        var query = new MultiHistorySampleParam();
        query.setDevIdAndPropCodes(devices.stream().map(device -> device.deviceId() + "." + propCode).collect(Collectors.joining(",")));
        query.setStartTs(DateUtils.toTimestamp(period.getBegin()));
        query.setEndTs(DateUtils.toTimestamp(period.getEnd()));
        query.setStep(1);
        query.setAggType("raw");
        query.setMaxRecords(Math.max(1000, devices.size() * 2000));
        Map<String, DeviceTSDTO> history = iotDevicePropClient.multiHistorySampleProp(query);
        if (MapUtils.isEmpty(history)) {
            return List.of();
        }
        Map<Long, GasType> typeByDevice = devices.stream().collect(Collectors.toMap(DeviceRef::deviceId, DeviceRef::type));
        List<Sample> result = new ArrayList<>();
        history.forEach((key, deviceTs) -> {
            Long deviceId = parseLong(deviceTs == null ? null : deviceTs.getDeviceId());
            if (deviceId == null) {
                deviceId = parseLong(key);
            }
            GasType type = typeByDevice.get(deviceId);
            if (deviceId == null || type == null || deviceTs == null || ListUtils.isEmpty(deviceTs.getPropertyTsBoList())) {
                return;
            }
            for (PropertyTsDTO property : deviceTs.getPropertyTsBoList()) {
                if (property == null || !propCode.equals(trim(property.getPropertyCode())) || ListUtils.isEmpty(property.getValueTsBoList())) {
                    continue;
                }
                for (KeyValueWithTsDTO value : property.getValueTsBoList()) {
                    BigDecimal number = toBigDecimal(value == null ? null : value.getValue());
                    LocalDateTime time = toLocalDateTime(value == null ? null : value.getTs());
                    if (number != null && time != null) {
                        result.add(new Sample(deviceId, type, time, number));
                    }
                }
            }
        });
        return result;
    }

    private Map<Long, List<LocalDateTime>> loadAlarmTimes(List<DeviceRef> devices, Period period) {
        var query = new AlarmRecordQueryParam();
        query.setPageNumber(1);
        query.setPageSize(EXPORT_PAGE_SIZE);
        query.setApplicationCode(GAS_MONITORING_APPLICATION_CODE);
        query.setDeviceIds(devices.stream().map(DeviceRef::deviceId).toList());
        query.setPeriod(period);
        Pagination<AlarmRecordPageVO> page = alarmRecordClient.page(query);
        if (page == null || ListUtils.isEmpty(page.getRecords())) {
            return Map.of();
        }
        Map<Long, List<LocalDateTime>> result = new HashMap<>();
        for (AlarmRecordPageVO alarm : page.getRecords()) {
            if (alarm == null || alarm.getAlarmTargetKey() == null || alarm.getAlarmTime() == null) {
                continue;
            }
            result.computeIfAbsent(alarm.getAlarmTargetKey(), ignored -> new ArrayList<>()).add(alarm.getAlarmTime());
        }
        return result;
    }

    private static Sample nearest(List<Sample> samples, LocalDateTime target) {
        return samples.stream()
                .filter(item -> Math.abs(Duration.between(item.time(), target).toMinutes()) <= MAX_NEAREST_MINUTES)
                .min(Comparator.<Sample>comparingLong(item -> Math.abs(Duration.between(item.time(), target).toMillis()))
                        .thenComparing(Sample::time, Comparator.reverseOrder()))
                .orElse(null);
    }

    private static boolean isAlarm(Long deviceId, LocalDateTime sampleTime, Map<Long, List<LocalDateTime>> alarmTimes) {
        List<LocalDateTime> times = alarmTimes.get(deviceId);
        return times != null && times.stream().anyMatch(time -> Math.abs(Duration.between(time, sampleTime).toMinutes()) <= MAX_NEAREST_MINUTES);
    }

    private static boolean hasGasValue(GasArchiveRecordVO record) {
        return record != null && (record.getCo2() != null || record.getO2() != null || record.getPh3() != null);
    }

    private String resolveWarehouseName(Long warehouseId) {
        return warehousePageApiClient.fetchPageItemById(warehouseId).map(LiangcangWarehousePageItemVO::getName).orElse(null);
    }

    private static boolean deviceMatches(DeviceBaseDetailsDTO detail, List<String> productCodes) {
        return detail != null && detail.getDeviceIotProdId() != null && productCodes.stream().filter(Objects::nonNull)
                .anyMatch(code -> code.trim().equals(String.valueOf(detail.getDeviceIotProdId())));
    }

    private static GasType resolveGasType(DeviceBaseDetailsDTO detail) {
        String name = detail == null ? "" : trim(detail.getDeviceName()).toLowerCase();
        if (name.contains("二氧化碳") || name.contains("co2")) {
            return GasType.CO2;
        }
        if (name.contains("氧气") || name.contains("o2")) {
            return GasType.O2;
        }
        if (name.contains("磷化氢") || name.contains("ph3") || name.contains("ph₃")) {
            return GasType.PH3;
        }
        return null;
    }

    private static BigDecimal toPercentVol(GasType type, BigDecimal value) {
        if (value == null) {
            return null;
        }
        return type == GasType.O2 ? value : value.divide(PPM_TO_PERCENT_VOL, 6, RoundingMode.HALF_UP);
    }

    private static CurrentSample toCurrentSample(Long deviceId, KeyValueWithTsDTO value) {
        BigDecimal number = toBigDecimal(value.getValue());
        LocalDateTime time = toLocalDateTime(value.getTs());
        return number == null || time == null ? null : new CurrentSample(deviceId, time, number);
    }

    private static BigDecimal toBigDecimal(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return new BigDecimal(String.valueOf(value));
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private static LocalDateTime toLocalDateTime(Long timestamp) {
        if (timestamp == null) {
            return null;
        }
        Instant instant = timestamp > 100_000_000_000L ? Instant.ofEpochMilli(timestamp) : Instant.ofEpochSecond(timestamp);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    private static Long parseLong(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Long.valueOf(value.trim());
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private static String trim(String value) {
        return value == null ? "" : value.trim();
    }

    private static Period normalizePeriod(Period period) {
        LocalDateTime begin = period == null || period.getBegin() == null ? LocalDate.now().atStartOfDay() : period.getBegin();
        LocalDateTime end = period == null || period.getEnd() == null ? LocalDate.now().atTime(23, 59, 59) : period.getEnd();
        return new Period(begin, end);
    }

    private GasArchiveTrendVO.Item toTrendItem(GasArchiveRecordVO record) {
        var item = new GasArchiveTrendVO.Item();
        item.setSampleTime(record.getAcquisitionTime());
        item.setCo2(record.getCo2());
        item.setO2(record.getO2());
        item.setPh3(record.getPh3());
        item.setCo2Alarm(record.getCo2Alarm());
        item.setO2Alarm(record.getO2Alarm());
        item.setPh3Alarm(record.getPh3Alarm());
        return item;
    }

    private GasArchiveRecordExportVO toExport(GasArchiveRecordVO record) {
        var item = new GasArchiveRecordExportVO();
        item.setAcquisitionTime(record.getAcquisitionTime());
        item.setCo2(record.getCo2());
        item.setO2(record.getO2());
        item.setPh3(record.getPh3());
        return item;
    }

    private enum GasType {
        CO2, O2, PH3
    }

    private record DeviceRef(Long deviceId, GasType type) {
    }

    private record Sample(Long deviceId, GasType type, LocalDateTime time, BigDecimal value) {
    }

    private record CurrentSample(Long deviceId, LocalDateTime time, BigDecimal value) {
    }
}
