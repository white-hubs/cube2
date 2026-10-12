package com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.job;

import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.querygateway.ParkingAccessRecordService;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.querygateway.ParkingPayRecordService;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.enumeration.ParkingAccessTypeEnum;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.po.ParkingAccessRecordPO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.po.ParkingInfoPO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.po.ParkingPayRecordPO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.repository.ParkingInfoRepository;
import com.guhecloud.platform.base.context.ExecutorContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemEndpointConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import com.guhecloud.subsystem.requester.sdk.client.ParkingClient;
import com.guhecloud.subsystem.requester.sdk.param.parking.ParkingInRecordQueryParam;
import com.guhecloud.subsystem.requester.sdk.param.parking.ParkingOutRecordQueryParam;
import com.guhecloud.subsystem.requester.sdk.param.parking.ParkingPayRecordQueryParam;
import com.guhecloud.subsystem.requester.sdk.vo.parking.ParkingInRecordVO;
import com.guhecloud.subsystem.requester.sdk.vo.parking.ParkingInfoVO;
import com.guhecloud.subsystem.requester.sdk.vo.parking.ParkingOutRecordVO;
import com.guhecloud.subsystem.requester.sdk.vo.parking.ParkingPayRecordVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ParkingJob {

    private static final String OASIS = "oasis";
    private static final DateTimeFormatter QUERY_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final ParkingClient parkingClient;
    private final SubsystemRedisUtils subsystemRedisUtils;
    private final ParkingInfoRepository parkingInfoRepository;
    private final ParkingAccessRecordService parkingAccessRecordService;
    private final ParkingPayRecordService parkingPayRecordService;

    @JobAnnotation(name = "executeParkingInfo", cron = "0 0/30 * * * ?", desc = "停车管理-三方停车场信息同步")
    public void executeParkingInfo(String param) {
        oasisEndpoints().forEach(config -> executeForTenant(config, this::getParkingInfo));
    }

    @JobAnnotation(name = "executeParkingRecord", cron = "0 0/1 * * * ?", desc = "停车管理-三方停车记录采集")
    public void executeParkingRecord(String param) {
        oasisEndpoints().forEach(config -> executeForTenant(config, () -> collectRecords(config.getTenant())));
    }

    private List<SubsystemEndpointConfig> oasisEndpoints() {
        return subsystemRedisUtils.getSubEndpointRefreshConfigs(SubsystemTypeConstants.PARKING_SERVER).stream()
                .filter(config -> OASIS.equalsIgnoreCase(config.getManufacturer()))
                .toList();
    }

    private void executeForTenant(SubsystemEndpointConfig config, Runnable task) {
        ExecutorContextHolder.run(() -> {
            SessionContextHolder.requestTenant(config.getTenant());
            try {
                task.run();
            } catch (Exception exception) {
                log.error("绿洲停车数据采集失败，tenant={}", config.getTenant(), exception);
            }
        });
    }

    private void getParkingInfo() {
        List<ParkingInfoVO> records = parkingClient.getParkingInfo();
        if (CollectionUtils.isNotEmpty(records)) {
            parkingInfoRepository.delAndSaveBatchThirdParking(ObjectUtils.copying(records, ParkingInfoPO.class));
        }
    }

    private void collectRecords(Integer tenant) {
        collectSafely(tenant, "入场", this::collectInRecords);
        collectSafely(tenant, "出场", this::collectOutRecords);
        collectSafely(tenant, "缴费", this::collectPayRecords);
    }

    private void collectSafely(Integer tenant, String type, Runnable collector) {
        try {
            collector.run();
        } catch (Exception exception) {
            log.error("绿洲停车{}记录采集失败，tenant={}", type, tenant, exception);
        }
    }

    private void collectInRecords() {
        ParkingInRecordQueryParam param = new ParkingInRecordQueryParam();
        LocalDateTime latestTime = latestAccessTime(ParkingAccessTypeEnum.ACCESS_IN);
        if (latestTime != null) {
            param.setInBeginTime(formatQueryTime(latestTime));
            param.setInEndTime(formatQueryTime(LocalDateTime.now()));
        }
        List<ParkingInRecordVO> records = parkingClient.getParkingInRecords(param).stream()
                .sorted(Comparator.comparing(ParkingInRecordVO::getInTime,
                                             Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();
        saveInRecords(records);
    }

    private void collectOutRecords() {
        ParkingOutRecordQueryParam param = new ParkingOutRecordQueryParam();
        LocalDateTime latestTime = latestAccessTime(ParkingAccessTypeEnum.ACCESS_OUT);
        if (latestTime != null) {
            param.setOutBeginTime(formatQueryTime(latestTime));
            param.setOutEndTime(formatQueryTime(LocalDateTime.now()));
        }
        List<ParkingOutRecordVO> records = parkingClient.getParkingOutRecords(param).stream()
                .sorted(Comparator.comparing(ParkingOutRecordVO::getOutTime,
                                             Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();
        saveOutRecords(records);
    }

    private void collectPayRecords() {
        ParkingPayRecordQueryParam param = new ParkingPayRecordQueryParam();
        LocalDateTime latestTime = latestPayTime();
        if (latestTime != null) {
            param.setBeginTime(formatQueryTime(latestTime));
            param.setBndTime(formatQueryTime(LocalDateTime.now()));
        }
        List<ParkingPayRecordVO> records = parkingClient.getParkingPayRecords(param).stream()
                .sorted(Comparator.comparing(ParkingPayRecordVO::getPayTime,
                                             Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();
        savePayRecords(records);
    }

    private LocalDateTime latestAccessTime(ParkingAccessTypeEnum accessType) {
        ParkingAccessRecordPO latest = parkingAccessRecordService.lambdaQuery()
                .select(ParkingAccessRecordPO::getAccessTime)
                .eq(ParkingAccessRecordPO::getAccessType, accessType)
                .isNotNull(ParkingAccessRecordPO::getAccessTime)
                .orderByDesc(ParkingAccessRecordPO::getAccessTime)
                .last("LIMIT 1")
                .one();
        return latest == null ? null : latest.getAccessTime();
    }

    private LocalDateTime latestPayTime() {
        ParkingPayRecordPO latest = parkingPayRecordService.lambdaQuery()
                .select(ParkingPayRecordPO::getPayTime)
                .isNotNull(ParkingPayRecordPO::getPayTime)
                .orderByDesc(ParkingPayRecordPO::getPayTime)
                .last("LIMIT 1")
                .one();
        return latest == null ? null : latest.getPayTime();
    }

    private void saveInRecords(List<ParkingInRecordVO> records) {
        LinkedHashMap<String, ParkingInRecordVO> candidates = records.stream()
                .filter(record -> StringUtils.isNotBlank(record.getIdentifyId()) && record.getInTime() != null)
                .collect(Collectors.toMap(ParkingInRecordVO::getIdentifyId, Function.identity(),
                                          (first, ignored) -> first, LinkedHashMap::new));
        Set<String> existingIds = existingAccessRecordIds(candidates.keySet(), ParkingAccessTypeEnum.ACCESS_IN);
        List<ParkingAccessRecordPO> targets = candidates.values().stream()
                .filter(record -> !existingIds.contains(record.getIdentifyId()))
                .map(record -> toAccessRecord(record, ParkingAccessTypeEnum.ACCESS_IN))
                .toList();
        if (CollectionUtils.isNotEmpty(targets)) {
            parkingAccessRecordService.saveBatch(targets);
        }
    }

    private void saveOutRecords(List<ParkingOutRecordVO> records) {
        LinkedHashMap<String, ParkingOutRecordVO> candidates = records.stream()
                .filter(record -> StringUtils.isNotBlank(record.getIdentifyId()) && record.getOutTime() != null)
                .collect(Collectors.toMap(ParkingOutRecordVO::getIdentifyId, Function.identity(),
                                          (first, ignored) -> first, LinkedHashMap::new));
        Set<String> existingIds = existingAccessRecordIds(candidates.keySet(), ParkingAccessTypeEnum.ACCESS_OUT);
        List<ParkingAccessRecordPO> targets = candidates.values().stream()
                .filter(record -> !existingIds.contains(record.getIdentifyId()))
                .map(record -> toAccessRecord(record, ParkingAccessTypeEnum.ACCESS_OUT))
                .toList();
        if (CollectionUtils.isNotEmpty(targets)) {
            parkingAccessRecordService.saveBatch(targets);
        }
    }

    private ParkingAccessRecordPO toAccessRecord(ParkingInRecordVO record, ParkingAccessTypeEnum accessType) {
        ParkingAccessRecordPO target = ObjectUtils.copying(record, ParkingAccessRecordPO.class);
        target.setId(null);
        target.setAccessType(accessType);
        target.setAccessTime(record.getInTime());
        target.setImageUrl(record.getInImageUrl());
        target.setImageBase(record.getInImageBase());
        return target;
    }

    private ParkingAccessRecordPO toAccessRecord(ParkingOutRecordVO record, ParkingAccessTypeEnum accessType) {
        ParkingAccessRecordPO target = ObjectUtils.copying(record, ParkingAccessRecordPO.class);
        target.setId(null);
        target.setAccessType(accessType);
        target.setAccessTime(record.getOutTime());
        target.setImageUrl(record.getOutImageUrl());
        target.setImageBase(record.getOutImageBase());
        return target;
    }

    private Set<String> existingAccessRecordIds(Set<String> identifyIds, ParkingAccessTypeEnum accessType) {
        if (identifyIds.isEmpty()) {
            return Set.of();
        }
        return parkingAccessRecordService.lambdaQuery()
                .select(ParkingAccessRecordPO::getIdentifyId)
                .eq(ParkingAccessRecordPO::getAccessType, accessType)
                .in(ParkingAccessRecordPO::getIdentifyId, identifyIds)
                .list().stream()
                .map(ParkingAccessRecordPO::getIdentifyId)
                .collect(Collectors.toCollection(HashSet::new));
    }

    private void savePayRecords(List<ParkingPayRecordVO> records) {
        LinkedHashMap<String, ParkingPayRecordVO> candidates = records.stream()
                .filter(record -> StringUtils.isNotBlank(record.getIdentifyId()) && record.getPayTime() != null)
                .collect(Collectors.toMap(ParkingPayRecordVO::getIdentifyId, Function.identity(),
                                          (first, ignored) -> first, LinkedHashMap::new));
        if (candidates.isEmpty()) {
            return;
        }
        Set<String> existingIds = parkingPayRecordService.lambdaQuery()
                .select(ParkingPayRecordPO::getIdentifyId)
                .in(ParkingPayRecordPO::getIdentifyId, candidates.keySet())
                .list().stream()
                .map(ParkingPayRecordPO::getIdentifyId)
                .collect(Collectors.toCollection(HashSet::new));
        List<ParkingPayRecordPO> targets = candidates.values().stream()
                .filter(record -> !existingIds.contains(record.getIdentifyId()))
                .map(record -> {
                    ParkingPayRecordPO target = ObjectUtils.copying(record, ParkingPayRecordPO.class);
                    target.setId(null);
                    return target;
                })
                .toList();
        if (CollectionUtils.isNotEmpty(targets)) {
            parkingPayRecordService.saveBatch(targets);
        }
    }

    private static String formatQueryTime(LocalDateTime value) {
        return QUERY_TIME_FORMATTER.format(value);
    }

}
