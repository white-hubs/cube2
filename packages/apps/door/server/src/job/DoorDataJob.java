package com.guhecloud.cube.biz.door.server.job;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.door.server.dao.TypeManageMapper;
import com.guhecloud.cube.biz.door.server.entity.DoorDevice;
import com.guhecloud.cube.biz.door.server.entity.EventRecord;
import com.guhecloud.cube.biz.door.server.entity.RecordTime;
import com.guhecloud.cube.biz.door.server.entity.TypeManage;
import com.guhecloud.cube.biz.door.server.enumeration.EventResultEnum;
import com.guhecloud.cube.biz.door.server.service.DoorDeviceService;
import com.guhecloud.cube.biz.door.server.service.EventRecordService;
import com.guhecloud.cube.biz.door.server.service.RecordTimeService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import com.guhecloud.subsystem.requester.sdk.client.AccessControlClient;
import com.guhecloud.subsystem.requester.sdk.param.accesscontrol.DoorEventsParam;
import com.guhecloud.subsystem.requester.sdk.vo.accesscontrol.AuthDoorStatusVo;
import com.guhecloud.subsystem.requester.sdk.vo.accesscontrol.DoorEventsVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DoorDataJob {

    private final RecordTimeService recordTimeService;

    private final AccessControlClient accessControlClient;

    private final SubsystemRedisUtils subsystemRedisUtils;

    private final EventRecordService eventRecordService;
    private final DoorDeviceService doorDeviceService;

    private final TypeManageMapper typeManageMapper;

    private Integer pageNo = 1;

    private Integer pageSize = 1000;

    /**
     * @Description 门禁数据采集
     * @Author lrj
     */
    @JobAnnotation(name = "doorDataJob", cron = "0 0 0/1 * * ?", desc = "门禁数据采集")
    public void execute(String param) throws ParseException {
        try {
            log.info("门禁记录采集开始：==================================================================================");
            var date = DateUtils.now();
            var subEndpointRefreshConfigs = subsystemRedisUtils.getSubEndpointRefreshConfigs(SubsystemTypeConstants.DOOR_SERVER);
            subEndpointRefreshConfigs.forEach(s -> {
                SessionContextHolder.requestTenant(s.getTenant());
                var list = recordTimeService.list();
                var startTime = "";
                var endTime = "";
                if (!list.isEmpty()) {
                    var recordTime = list.getFirst();
                    startTime = DateUtils.format(recordTime.getEndTime());
                    endTime = DateUtils.format(date);
                } else {
                    endTime = DateUtils.format(date);
                    var offset = DateUtils.offset(date, ChronoUnit.MONTHS, -3);
                    startTime = DateUtils.format(offset);
                }
                //事件记录采取
                var maxDateInRecord = saveDoorEventsRecordAndReturnMaxDate(startTime, endTime);

                //采取时间修改
                if (!list.isEmpty()) {
                    var recordTime = list.getFirst();
                    recordTime.setEndTime(maxDateInRecord);
                    recordTimeService.updateById(recordTime);
                } else {
                    var recordTime = new RecordTime();
                    recordTime.setEndTime(maxDateInRecord);
                    recordTimeService.save(recordTime);
                }

            });
            log.info("门禁记录采集结束：==================================================================================");
        } catch (Exception e) {
            log.error("门禁记录采集,失败原因:", e);
        }
    }

    /**
     * @Description 门禁点状态更新
     * @Author lrj
     */
    public void executeUpdateStatus(String param) throws ParseException {
        try {
            System.out.println("门禁点状态更新开始：==================================================================================");
            var subEndpointRefreshConfigs = subsystemRedisUtils.getSubEndpointRefreshConfigs(SubsystemTypeConstants.DOOR_SERVER);
            subEndpointRefreshConfigs.forEach(s -> {
                SessionContextHolder.requestTenant(s.getTenant());
                var list = doorDeviceService.list();
                if (CollectionUtils.isNotEmpty(list)) {
                    var collect = list.stream().map(DoorDevice::getOutBusinessId).filter(Objects::nonNull)
                            .collect(Collectors.toList());
                    updateDoorStatus(collect);
                }
            });
            System.out.println("门禁点状态更新结束：==================================================================================");
        } catch (Exception e) {
            log.error("门禁点状态更新,失败原因:====", e);
        }
    }

    /**
     * @Description 门禁事件记录采集
     * @Author lrj
     */
    private void getHikEventsRecord(String startTime, String endTime) {
        List<TypeManage> typeManages = typeManageMapper.selectList(null);
        Map<String, TypeManage> collect = typeManages.stream()
                .collect(Collectors.toMap(typeManage -> typeManage.getTypeCode().toString(), typeManage -> typeManage, (o, n) -> n));
        //获取设备
        List<DoorDevice> doorDevices = doorDeviceService.list();
        if (CollectionUtils.isNotEmpty(doorDevices)) {
            List<String> deviceIds = doorDevices.stream().map(DoorDevice::getOutBusinessId).collect(Collectors.toList());
            int x = deviceIds.size();
            int y = 50;
            int a = x / y + (x % y != 0 ? 1 : 0);
            for (int i = 0; i < a; i++) {
                DoorEventsParam doorEventsParam = new DoorEventsParam();
                doorEventsParam.setStartTime(startTime);
                doorEventsParam.setEndTime(endTime);
                doorEventsParam.setPageNo(pageNo);
                doorEventsParam.setPageSize(pageSize);
                if (a == 1) {
                    doorEventsParam.setDoorIndexCodes(deviceIds);
                } else {
                    if ((i + 1) == a) {
                        doorEventsParam.setDoorIndexCodes(deviceIds.subList(i * y, deviceIds.size() - 1));
                    } else {
                        doorEventsParam.setDoorIndexCodes(deviceIds.subList(i * y, (i + 1) * y - 1));
                    }
                }

                Page<DoorEventsVo> doorEvents = accessControlClient.getDoorEvents(doorEventsParam);
                List<DoorEventsVo> records = doorEvents.getRecords();

                if (CollectionUtils.isNotEmpty(records)) {
                    List<String> recordCollect = records.stream().map(DoorEventsVo::getEventId).toList();
                    Map<String, DoorEventsVo> eventMap = records.stream()
                            .collect(Collectors.toMap(DoorEventsVo::getEventId, record -> record, (key, o) -> o));
                    if (CollectionUtils.isNotEmpty(recordCollect)) {
                        for (String recordKey : recordCollect) {
                            DoorEventsVo doorEventsVo = eventMap.get(recordKey);
                            try {
                                TypeManage typeManage = collect.get(doorEventsVo.getEventType());
                                String result = "";
                                if (Objects.nonNull(typeManage)) {
                                    result = typeManage.getTypeResult();
                                } else {
                                    result = EventResultEnum.DO_REFUSE.name();
                                }

                                LocalDateTime parse = DateUtils.parse(doorEventsVo.getEventTime());
                                EventRecord build = EventRecord.builder()
                                        .eventId(doorEventsVo.getEventId())
                                        .doorIndexCode(doorEventsVo.getDoorIndexCode())
                                        .doorName(doorEventsVo.getDoorName())
                                        .eventType(typeManage == null ? "" : typeManage.getName())
                                        .personId(doorEventsVo.getPersonId())
                                        .personName(doorEventsVo.getPersonName())
                                        .inAndOutType(String.valueOf(doorEventsVo.getInAndOutType()))
                                        .eventName(typeManage == null ? "" : typeManage.getName())
                                        .eventTime(parse)
                                        .eventWay(typeManage == null ? "" : typeManage.getName())
                                        .eventResult(result)
                                        .pictureFileId(doorEventsVo.getPictureFileId())
                                        .build();
                                build.setDeleted(false);
                                build.setTenant(SessionContextHolder.getRequester().getTenant());
                                boolean exists = eventRecordService.lambdaQuery().eq(EventRecord::getEventId, build.getEventId()).exists();
                                if (exists) {
                                    eventRecordService.lambdaUpdate()
                                            .set(EventRecord::getEventId, build.getEventId())
                                            .set(EventRecord::getDoorIndexCode, build.getDoorIndexCode())
                                            .set(EventRecord::getDoorName, build.getDoorName())
                                            .set(EventRecord::getEventType, build.getEventType())
                                            .set(EventRecord::getPersonId, build.getPersonId())
                                            .set(EventRecord::getPersonName, build.getPersonName())
                                            .set(EventRecord::getInAndOutType, build.getInAndOutType())
                                            .set(EventRecord::getEventName, build.getEventName())
                                            .set(EventRecord::getEventTime, build.getEventTime())
                                            .set(EventRecord::getEventWay, build.getEventWay())
                                            .set(EventRecord::getEventResult, build.getEventResult())
                                            .set(EventRecord::getPictureFileId, build.getPictureFileId())
                                            .eq(EventRecord::getDoorIndexCode, build.getDoorIndexCode())
                                            .update();
                                    return;
                                }
                                eventRecordService.save(build);
                            } catch (Exception e) {
                                log.info("门禁记录异常===", e);
                            }
                        }
                    }

                }
            }

        }

    }

    /**
     * 门禁事件
     *
     * @param startTime
     * @param endTime
     */
    private LocalDateTime saveDoorEventsRecordAndReturnMaxDate(String startTime, String endTime) {
        DoorEventsParam doorEventsParam = new DoorEventsParam();
        doorEventsParam.setStartTime(startTime);
        doorEventsParam.setEndTime(endTime);
        doorEventsParam.setPageNo(pageNo);
        doorEventsParam.setPageSize(pageSize);

        Page<DoorEventsVo> doorEvents = accessControlClient.getDoorEvents(doorEventsParam);
        List<DoorEventsVo> records = doorEvents.getRecords();

        if (CollectionUtils.isNotEmpty(records)) {
            List<EventRecord> eventRecords = records.stream().map(r -> {
                LocalDateTime eventTime = DateUtils.parse(r.getEventTime());
                EventRecord build = EventRecord.builder()
                        .id(IdWorker.getId())
                        .eventId(r.getEventId())
                        .doorIndexCode(r.getDoorIndexCode())
                        .doorName(r.getDoorName())
                        .eventType(r.getEventType())
                        .personId(r.getPersonId())
                        .personName(r.getPersonName())
                        .inAndOutType(String.valueOf(r.getInAndOutType()))
                        .eventName(r.getEventName())
                        .eventTime(eventTime)
                        .eventWay(r.getEventWay())
                        .eventResult(r.getEventResult())
                        .cardNo(r.getCardNo())
                        .pictureFileId(r.getPictureFileId())
                        .build();
                build.setDeleted(false);
                EntityAuditor.populateInnerAudit(build);
                eventRecordService.insertOrUpdate(build);
                return build;
            }).toList();
            //获取最大的一条数据
            EventRecord maxEventRecord = Collections.max(eventRecords, Comparator.comparing(EventRecord::getEventTime));
            return maxEventRecord.getEventTime();
        }
        return DateUtils.parse(endTime);
    }

    private void updateDoorStatus(List<String> list) {
        int x = list.size();
        int y = 200;
        int a = x / y + (x % y != 0 ? 1 : 0);
        if (CollectionUtils.isNotEmpty(list)) {
            if (a == 1) {
                List<AuthDoorStatusVo> doorStates = accessControlClient.getDoorStates(list);
                doorDeviceService.updateStatus(doorStates);
            } else {
                for (int i = 0; i < a; i++) {
                    if ((i + 1) == a) {
                        List<String> strings = list.subList(i * y, list.size() - 1);
                        List<AuthDoorStatusVo> doorStates = accessControlClient.getDoorStates(strings);
                        doorDeviceService.updateStatus(doorStates);
                    } else {
                        List<String> strings = list.subList(i * y, (i + 1) * y - 1);
                        List<AuthDoorStatusVo> doorStates = accessControlClient.getDoorStates(strings);
                        doorDeviceService.updateStatus(doorStates);
                    }

                }
            }

        }

    }

}
