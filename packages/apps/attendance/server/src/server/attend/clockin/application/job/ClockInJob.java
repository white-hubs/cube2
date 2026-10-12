package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.application.job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guhecloud.cube.base.bracing.api.AssemblyNoticeClient;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendConfig;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendParam;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendSceneQueryPram;
import com.guhecloud.cube.base.message.sdk.api.response.MessageSendResponse;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.application.commandgateway.AttClockInLedgerApplication;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.application.commandgateway.AttStaffClockApplication;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.enumeration.ClockInEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.enumeration.IdentityVerifyEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.factory.FaceRecognitionFactory;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository.AttApproveLogRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository.AttClockInLedgerDetailsRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository.AttClockInRecordsRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.mapper.AttClockInLedgerDetailsMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.mapper.AttClockInRecordsMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockInLedgerDetailsPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockInRecordsPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttClassesRecordMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttGroupStaffMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttClassesRecordPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupStaffPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.querygateway.AttSchedulingQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.mapper.AttSchedulingMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po.AttSchedulingPO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClockInJob {
    private final AttStaffClockApplication staffClockApplication;
    private final AttClockInLedgerApplication ledgerApplication;
    private final AttClockInLedgerDetailsMapper ledgerDetailsMapper;
    private final AttClockInLedgerDetailsRepository ledgerDetailsRepository;
    private final AttClockInRecordsMapper clockInRecordsMapper;
    private final AttClockInRecordsRepository clockInRecordsRepository;
    private final AssemblyNoticeClient assemblyNoticeClient;
    private final AttSchedulingMapper schedulingMapper;
    private final AttClassesRecordMapper classesRecordMapper;
    private final AttApproveLogRepository approveLogRepository;
    private final AttSchedulingQueryService attSchedulingQueryService;
    private final AttGroupStaffMapper groupStaffMapper;
    private final FaceRecognitionFactory faceRecognitionFactory;

    /**
     * 每月1号生成台账数据
     */
    @JobAnnotation(name = "addClockInLedger", cron = "0 0 0 1,20 * ?", desc = "考勤-每月1号生成考勤台账数据")
    public void addClockInLedger(String param) {
        SessionContextHolder.clearSessionContext();
        log.info("att:定时任务开始生成台账");
        var offsetMonth = 0;
        ledgerApplication.addClockInLedger(offsetMonth);
        log.info("att:定时任务生成台账结束");
        SessionContextHolder.clearSessionContext();
    }

    /**
     * 每天凌晨更新休息的台账
     */
    @JobAnnotation(name = "updateClockInLedger", cron = "0 30 0 * * ?", desc = "考勤-考勤每天凌晨更新休息的台账")
    public void updateClockInLedger(String param) {
        SessionContextHolder.clearSessionContext();
        // 更新休息
        ledgerDetailsRepository.updateRest();
        // 校验异常打卡数据
        SessionContextHolder.clearSessionContext();
    }

    /**
     * 生成打卡任务
     */
    @JobAnnotation(name = "addClockIn", cron = "0 0/5 * * * ?", desc = "考勤-生成打卡任务")
    public void addClockIn(String param) {
        SessionContextHolder.clearSessionContext();
        log.info("开始生成打卡任务:{}", param);
        staffClockApplication.addClockIn();
        log.info("生成打卡任务,执行结束");
        this.clockInMessage();
        log.info("发送打卡消息提醒结束");
        try {
            log.info("开始检查需要重新人脸识别数据");
            var clock = clockInRecordsMapper.selectList(new LambdaQueryWrapper<AttClockInRecordsPO>()
                                                                .eq(AttClockInRecordsPO::getFaceRecognitionResult,
                                                                    IdentityVerifyEnum.FAIL.getRemarks())
                                                                .in(AttClockInRecordsPO::getClockType,
                                                                    ClockInEnum.clockType));
            clock.parallelStream().forEach(ex -> {
                SessionContextHolder.requestTenant(ex.getTenant());
                var userDTO = new UserDTO();
                userDTO.setId(ex.getUserId());
                userDTO.setName(ex.getUserName());
                var faceRecognitionResult = faceRecognitionFactory.faceRecognition(ex.getPhotoFileId(), ex.getPhotoFileUrl(),
                                                                                   userDTO);
                // 人脸识别失败异常提示
                if (ObjectUtils.isNotEmpty(faceRecognitionResult)) {
                    ex.setFaceRecognitionResult(faceRecognitionResult);
                    clockInRecordsRepository.revalidation(ex);
                }
            });
        } catch (Exception e) {
            log.error("人脸验证失败:", e);
        }
        SessionContextHolder.clearSessionContext();
    }

    /**
     * 检查打卡状态、缺卡，并同步更新台账
     */
    @JobAnnotation(name = "updateClockInState", cron = "0 0/3 * * * ?", desc = "考勤-获取考勤机打卡数据、更新打卡状态")
    public void updateClockInState(String param) {
        SessionContextHolder.clearSessionContext();
        var date = LocalDateTime.now();
        log.info("开始检查打卡任务:{}", date);
        var clock = clockInRecordsMapper.selectList(new LambdaQueryWrapper<AttClockInRecordsPO>()
                                                            .lt(AttClockInRecordsPO::getClockAfterTime,
                                                                LocalDateTime.now())
                                                            .in(AttClockInRecordsPO::getClockType,
                                                                ClockInEnum.START.name(),
                                                                ClockInEnum.END.name())
                                                            .last("and (sync_time is NULL)"));
        clock.parallelStream().forEach(ex -> {
            try {
                SessionContextHolder.requestTenant(ex.getTenant());
                var principal = Principal.builder()
                        .id(ex.getUserId())
                        .name(ex.getUserName())
                        .type(Principal.Type.USER)
                        .build();
                SecurityContextHolder.setPrincipal(principal);
                if (ClockInEnum.NOT_CHECKED.name().equals(ex.getState())) {
                    ex.setState(ClockInEnum.LACK_CHECKED.name());
                }
                // 更新台账
                ledgerApplication.updateLedgerDetails(ex);
                ex.setSyncTime(LocalDateTime.now());
                clockInRecordsRepository.updateById(ex);
            } catch (Exception e) {
                log.error("检查任务失败", e);
            } finally {
                SessionContextHolder.clearSessionContext();
            }
        });
        try {
            // 更新打卡记录为缺卡
            clockInRecordsRepository.updateClockInRecords(null);
        } catch (Exception e) {
            log.error("更新缺卡状态失败", e);
        }
        log.info("检查打卡任务,执行结束");
    }

    /**
     * 通过打卡日志修复打卡信息
     */
    @JobAnnotation(name = "logRepairData", cron = "0 * * * * ?", desc = "考勤-根据日志修复打卡信息", enabled = false)
    public void logRepairData(String param) {
        SessionContextHolder.clearSessionContext();
        var logClockList = clockInRecordsMapper.selectList(new LambdaQueryWrapper<AttClockInRecordsPO>()
                                                                   .eq(AttClockInRecordsPO::getClockType,
                                                                       ClockInEnum.LOG.name())
                                                                   .eq(AttClockInRecordsPO::getState,
                                                                       ClockInEnum.LOSE_CHECKED.name())
                                                                   .orderByAsc(
                                                                           AttClockInRecordsPO::getActualClockInTime));
        var stateList = ClockInEnum.clockAbnormalState;
        stateList.add(ClockInEnum.NOT_CHECKED.name());
        logClockList.forEach(ex -> {
            SessionContextHolder.requestTenant(ex.getTenant());
            var clock = clockInRecordsMapper.selectOne(new LambdaQueryWrapper<AttClockInRecordsPO>()
                                                               .eq(AttClockInRecordsPO::getUserId, ex.getUserId())
                                                               .le(AttClockInRecordsPO::getClockBeforeTime,
                                                                   ex.getActualClockInTime())
                                                               .ge(AttClockInRecordsPO::getClockAfterTime,
                                                                   ex.getActualClockInTime())
                                                               .in(AttClockInRecordsPO::getClockType,
                                                                   ClockInEnum.clockType)
                                                               .last("limit 1"));
            // 没有找到打卡任务，保留一天，超过一天后标记为过期数据，不在处理
            if (ObjectUtils.isEmpty(clock)) {
                if (DateUtils.between(ex.getActualClockInTime(), LocalDateTime.now(), ChronoUnit.DAYS) > 1) {
                    ex.setState(ClockInEnum.EXPIRED.name());
                }
                ex.setState(ClockInEnum.EXPIRED.name());
                clockInRecordsMapper.updateById(ex);
                return;
            }
            // 上班状态正常，不需要更新则标记为已处理
            if (ClockInEnum.START.name().equals(clock.getClockType()) && ClockInEnum.NORMAL.name().equals(clock.getState())) {
                ex.setState(ClockInEnum.PROCESSED.name());
                clockInRecordsMapper.updateById(ex);
            } else if (ClockInEnum.END.name().equals(clock.getClockType()) &&
                       ClockInEnum.NORMAL.name().equals(clock.getState())) {
                // 如果下班正常且打卡时间小于最后一条打卡记录，则更新为最后一条打卡记录
                if (DateUtils.compare(ex.getActualClockInTime(), clock.getActualClockInTime()) > 0) {
                    clock.getAttClockInRecordsPO(ex);
                    var bool = clockInRecordsMapper.updateById(clock) > 0;
                    if (bool) {
                        ex.setDeleted(true);
                        // 修正后的打卡记录需要逻辑删除
                        clockInRecordsMapper.updateById(ex);
                    }
                } else {
                    // 如果下班正常且打卡时间大于最后一条打卡记录，则标记日志为已处理
                    ex.setState(ClockInEnum.PROCESSED.name());
                    clockInRecordsMapper.updateById(ex);
                }
            } else {
                // 修正打卡任务状态
                clock.getAttClockInRecordsPO(ex);
                var bool = clockInRecordsMapper.updateById(clock) > 0;
                if (bool) {
                    ex.setDeleted(true);
                    // 修正后的打卡记录需要逻辑删除
                    clockInRecordsMapper.updateById(ex);
                }
            }
        });
        SessionContextHolder.clearSessionContext();
    }

    /**
     * 处理同天同班次多个打卡任务
     */
    public void updateMultitask(String param) {
        SessionContextHolder.clearSessionContext();
        var clock = clockInRecordsMapper.getAllByCorrect();
        for (AttClockInRecordsPO ex : clock) {
            clockInRecordsRepository.update().set("mark", "yes")
                    .eq("user_id", ex.getUserId())
                    .eq("clock_in_date", ex.getClockInDate())
                    .update();
            var clockInRecords = clockInRecordsMapper.selectList(
                    new LambdaQueryWrapper<AttClockInRecordsPO>()
                            .eq(AttClockInRecordsPO::getUserId, ex.getUserId())
                            .eq(AttClockInRecordsPO::getClockType, ClockInEnum.START.name())
                            .eq(AttClockInRecordsPO::getClockInDate, ex.getClockInDate()));
            for (AttClockInRecordsPO clockIn : clockInRecords) {
                if (ObjectUtils.isNotEmpty(clockIn.getActualClockInTime())) {
                    var end = clockInRecordsMapper.selectOne(new LambdaQueryWrapper<AttClockInRecordsPO>()
                                                                     .isNotNull(
                                                                             AttClockInRecordsPO::getActualClockInTime)
                                                                     .eq(AttClockInRecordsPO::getClockType,
                                                                         ClockInEnum.END.name())
                                                                     .eq(AttClockInRecordsPO::getUserId,
                                                                         clockIn.getUserId())
                                                                     .eq(AttClockInRecordsPO::getClockInDate,
                                                                         clockIn.getClockInDate())
                                                                     .orderByDesc(
                                                                             AttClockInRecordsPO::getActualClockInTime)
                                                                     .last("limit 1"));
                    if (ObjectUtils.isNotEmpty(end) && ObjectUtils.isNotEmpty(end.getActualClockInTime())) {
                        clockInRecordsRepository.update().set("ways", end.getWays())
                                .set("actual_clock_in_time", end.getActualClockInTime())
                                .set("state", end.getState())
                                .set("deletion_duration", end.getDeletionDuration())
                                .set("name", end.getName())
                                .set("sync_time", null)
                                .eq("uuid", clockIn.getUuid())
                                .eq("clock_type", ClockInEnum.END.name())
                                .update();
                    }
                    clockInRecordsRepository.update().set("mark", null)
                            .eq("uuid", clockIn.getUuid())
                            .update();
                    break;
                }
            }
        }
        var markList = clockInRecordsMapper.selectList(new LambdaQueryWrapper<AttClockInRecordsPO>()
                                                               .eq(AttClockInRecordsPO::getMark, "yes"));
        for (AttClockInRecordsPO ex : markList) {
            clockInRecordsMapper.delete(new LambdaQueryWrapper<AttClockInRecordsPO>()
                                                .eq(AttClockInRecordsPO::getUuid, ex.getUuid()));
            ledgerDetailsMapper.delete(new LambdaQueryWrapper<AttClockInLedgerDetailsPO>()
                                               .eq(AttClockInLedgerDetailsPO::getClockInUuid, ex.getUuid()));
        }
        SessionContextHolder.clearSessionContext();
    }

    /**
     * 清洗考勤表数据
     */
    public void updateLedger(String param) {
        SessionContextHolder.clearSessionContext();
        try {
            var clock = ledgerDetailsMapper.getAll(param);
            clock.parallelStream().forEach(ex -> {
                SessionContextHolder.requestTenant(ex.getTenant());
                if (ex.getToday() < 10) {
                    ex.setLedgerBelongDate(DateUtils.parse(param + "-0" + ex.getToday()));
                } else {
                    ex.setLedgerBelongDate(DateUtils.parse(param + "-" + ex.getToday()));
                }
                if (ObjectUtils.isNotEmpty(ex.getClassesId())) {
                    var classesRecordPO = classesRecordMapper.selectOne(
                            new LambdaQueryWrapper<AttClassesRecordPO>()
                                    .eq(AttClassesRecordPO::getClassesId, ex.getClassesId())
                                    .orderByDesc(AttClassesRecordPO::getVersion)
                                    .last("limit 1"));
                    if (ObjectUtils.isNotEmpty(classesRecordPO)) {
                        ex.setClassesDetails(classesRecordPO.getNickName() + "(" + classesRecordPO.getStartWorkTime() + "-" +
                                             classesRecordPO.getEndWorkTime() + ")");
                    }
                } else {
                    if (DateUtils.compare(ex.getLedgerBelongDate(), LocalDateTime.now()) < 1) {
                        ex.setClassesDetails(ClockInEnum.REST.getRemarks());
                        ex.setState(ClockInEnum.REST.name());
                        ex.setStartClockInTime(ex.getLedgerBelongDate());
                        ex.setEndClockInTime(ex.getLedgerBelongDate());
                    }
                }
                if (ObjectUtils.isNotEmpty(ex.getClockInUuid())) {
                    var clockInRecordsPO = clockInRecordsMapper.selectOne(
                            new LambdaQueryWrapper<AttClockInRecordsPO>()
                                    .eq(AttClockInRecordsPO::getUuid, ex.getClockInUuid())
                                    .eq(AttClockInRecordsPO::getClockType, ClockInEnum.START.name())
                                    .last("limit 1"));
                    if (ObjectUtils.isNotEmpty(clockInRecordsPO)) {
                        ex.setActualStartClockInTime(clockInRecordsPO.getActualClockInTime());
                    }
                    var endClockInRecordsPO = clockInRecordsMapper.selectOne(
                            new LambdaQueryWrapper<AttClockInRecordsPO>()
                                    .eq(AttClockInRecordsPO::getUuid, ex.getClockInUuid())
                                    .eq(AttClockInRecordsPO::getClockType, ClockInEnum.END.name())
                                    .last("limit 1"));
                    if (ObjectUtils.isNotEmpty(endClockInRecordsPO)) {
                        ex.setEndClockInTime(endClockInRecordsPO.getRequireClockInTime());
                        ex.setActualEndClockInTime(endClockInRecordsPO.getActualClockInTime());
                    }
                }
                log.error("台账日期：{}", param + "-" + ex.getToday());
                ex.setWeek(DateUtils.dayOfWeekFormat(ex.getLedgerBelongDate()));
                ledgerDetailsMapper.updateById(ex);
            });
        } catch (Exception e) {
            log.error("数据处理异常", e);
        }
        SessionContextHolder.clearSessionContext();
    }

    /**
     * 上班场景code
     */
    private final String startSceneCode = "START_CLOCK";

    /**
     * 下班场景code
     */
    private final String endSceneCode = "END_CLOCK";

    /**
     * 打卡发送消息
     */
    private void clockInMessage() {
        SessionContextHolder.clearSessionContext();

        List<AttSchedulingPO> schedulingList = schedulingMapper.selectList(new LambdaQueryWrapper<AttSchedulingPO>()
                                                                                   .groupBy(AttSchedulingPO::getTenant));
        schedulingList.parallelStream().forEach(scheduling -> {
            try {
                SessionContextHolder.requestTenant(scheduling.getTenant());
                MessageSendSceneQueryPram queryPram = new MessageSendSceneQueryPram();
                queryPram.setBusinessType("ATTENDANCE_MANAGEMENT");
                queryPram.setSceneCodes(List.of(startSceneCode, endSceneCode));
                List<MessageSendConfig> messageSendConfigs = assemblyNoticeClient.getSendMsgConfig(queryPram);
                var staffList = groupStaffMapper.selectList(new LambdaQueryWrapper<AttGroupStaffPO>()
                                                                    .groupBy(
                                                                            AttGroupStaffPO::getStaffId));
                var staffIdMap = Optional.ofNullable(staffList).orElse(Collections.emptyList()).stream()
                        .collect(Collectors.toMap(AttGroupStaffPO::getStaffId, AttGroupStaffPO::getStaffId, (o, n) -> o));
                messageSendConfigs.forEach(ex -> {
                    String dateTime = DateUtils.format(LocalDateTime.now());
                    LocalDateTime startTime = DateUtils.offsetMinute(
                            DateUtils.parse(dateTime.substring(0, dateTime.length() - 2) + "00"),
                            ObjectUtils.isEmpty(ex.getValue()) ? 0 : startSceneCode.equals(ex.getSceneCode()) ? ex.getValue()
                                                                                                              : -ex.getValue());
                    LocalDateTime endTime = DateUtils.offsetMinute(startTime, 1);
                    if (startSceneCode.equals(ex.getSceneCode())) {
                        // 上班发送消息
                        staffList.forEach(staffPO -> {
                            AttSchedulingPO attSchedulingPO = attSchedulingQueryService.getStaffStartTimeScheduling(
                                    staffPO.getStaffId(), startTime);
                            if (ObjectUtils.isNotEmpty(attSchedulingPO.getClassesId())) {
                                if (!approveLogRepository.getApproveLogByUser(attSchedulingPO.getClassesRealStartTime(),
                                                                              staffPO.getStaffId())) {
                                    this.sendMessage(attSchedulingPO.getClassesId(), attSchedulingPO.getClassesVersion(),
                                                     staffPO.getStaffId(), ex.getNotifyId());
                                }
                            }
                        });
                    } else if (endSceneCode.equals(ex.getSceneCode())) {
                        List<AttClockInRecordsPO> clock = clockInRecordsMapper.selectList(
                                new LambdaQueryWrapper<AttClockInRecordsPO>()
                                        .ge(AttClockInRecordsPO::getRequireClockInTime, startTime)
                                        .lt(AttClockInRecordsPO::getRequireClockInTime, endTime)
                                        .eq(AttClockInRecordsPO::getClockType, ClockInEnum.END.name())
                                        .eq(AttClockInRecordsPO::getState, ClockInEnum.NOT_CHECKED.name()));
                        for (AttClockInRecordsPO clockInRecords : clock) {
                            if (!staffIdMap.containsKey(clockInRecords.getUserId())) {
                                // 不在考勤组中的人不提醒
                                continue;
                            }
                            // 请假、离岗、出差的人不提醒
                            if (!approveLogRepository.getApproveLogByUser(clockInRecords.getRequireClockInTime(),
                                                                          clockInRecords.getUserId())) {
                                this.sendMessage(clockInRecords.getClassesId(), clockInRecords.getClassesVersion(),
                                                 clockInRecords.getUserId(), ex.getNotifyId());
                            }
                        }
                    }
                });
            } catch (Exception e) {
                log.error("打卡发送消息通知结束失败", e);
            } finally {
                SessionContextHolder.clearSessionContext();
            }
        });
    }

    /**
     * 打卡发送消息通知
     */
    private void sendMessage(Long classesId, Integer version, Long userId, Long notifyId) {
        try {
            AttClassesRecordPO classesRecord = classesRecordMapper.selectOne(new LambdaQueryWrapper<AttClassesRecordPO>()
                                                                                     .eq(AttClassesRecordPO::getClassesId,
                                                                                         classesId)
                                                                                     .eq(ObjectUtils.isNotEmpty(version),
                                                                                         AttClassesRecordPO::getVersion, version)
                                                                                     .last("limit 1"));
            Map<String, String> map = new HashMap<>();
            map.put("classesName",
                    classesRecord.getName() + "(" + classesRecord.getStartWorkTime() + "-" + classesRecord.getEndWorkTime() +
                    ")");
            Set<Long> userIds = new HashSet<>();
            userIds.add(userId);
            MessageSendParam sendParam = MessageSendParam.builder()
                    .notifyId(notifyId)
                    .tempParams(map)
                    .notifyUsers(userIds)
                    .build();
            MessageSendResponse messageSendResponse = assemblyNoticeClient.sendMsg(sendParam);
            log.info("发送消息通知结果{}", JsonUtils.serialize(messageSendResponse));
        } catch (Exception e) {
            log.error("打卡发送消息通知失败", e);
        }
    }
}
