package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.application.commandgateway;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileOperateClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.OrganizationQueryParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.application.message.AttendanceClockInMessage;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.enumeration.ClockInAuthorityEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.enumeration.ClockInEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.enumeration.ClockInErrorEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.enumeration.IdentityVerifyEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.factory.FaceRecognitionFactory;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository.AttApproveLogRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository.AttClockInRecordsRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.valueobject.AttClockWayEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.mapper.AttClockInLedgerDetailsMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.mapper.AttClockInRecordsMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.mapper.AttClockRulesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockInLedgerDetailsPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockInRecordsPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param.AddClockInParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param.ClockInLogDateParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param.ClockInLogPageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param.ClockInMachineParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param.ClockInParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.AttClassesRecordResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.AttClockInRecordsResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.AttClockInRecordslogResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.AttClockRuleResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.ClockInInLedgerByUserResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.ClockInInfoClassesResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.ClockInInfoResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.ClockInLogResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.ClockInRecordsResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.application.querygateway.AttClassesQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttClassesRecordMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttClassesRecordPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.dto.AttClockSchedulingClassesDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.dto.AttSchedulingClassesDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.dto.AttSchedulingDateDOT;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.querygateway.AttSchedulingQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.mapper.AttSchedulingMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po.AttSchedulingPO;
import com.guhecloud.platform.base.constants.DateFormat;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.IdUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.cloud.util.ExcelUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 员工打卡服务
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttStaffClockApplication {
    private final AttClockInRecordsRepository clockInRecordsRepository;
    private final AttSchedulingQueryService schedulingQueryService;
    private final AttClassesRecordMapper classesRecordMapper;
    private final AttClassesQueryService classesQueryService;
    private final AttClockRulesMapper clockRulesMapper;
    private final OrganizationClient organizationClient;
    private final FileClient fileClient;
    private final UserClient userClient;
    private final AttClockInLedgerApplication clockInLedgerApplication;
    private final AttClockInLedgerDetailsMapper clockInLedgerDetailsMapper;
    private final AttClockInRecordsMapper clockInRecordsMapper;
    private final AttSchedulingMapper schedulingMapper;
    private final AttApproveLogRepository approveLogRepository;
    private final FaceRecognitionFactory faceRecognitionFactory;
    private final FileOperateClient fileOperateClient;
    private static final String fileSceneCode = "ATTENDANCE_MANAGEMENT::CLOCK_IN";

    public Pagination<ClockInLogResult> getClockInLog(ClockInLogPageParam param) {
        Pagination<ClockInLogResult> pagination = clockInRecordsRepository.getClockInLog(param);
        Set<Long> orgIds = pagination.getRecords().stream().map(ClockInLogResult::getOrgId).collect(Collectors.toSet());
        OrganizationQueryParam organizationQueryParam = new OrganizationQueryParam();
        organizationQueryParam.setIds(orgIds);
        List<OrganizationDTO> organizationDTOList = organizationClient.getOrganizations(organizationQueryParam);
        Map<Long, OrganizationDTO> orgMap = organizationDTOList.stream().collect(Collectors.toMap(OrganizationDTO::getId, v -> v));
        pagination.getRecords().forEach(ex -> {
            if (ObjectUtils.isNotEmpty(ex.getOrgId())) {
                OrganizationDTO organization = orgMap.get(ex.getOrgId());
                ex.setOrgName(Optional.ofNullable(organization).orElse(new OrganizationDTO()).getName());
            }
            if (ObjectUtils.isNotEmpty(ex.getPhotoFileId())) {
                try {
                    FileVO file = fileClient.getById(ex.getPhotoFileId());
                    ex.setImg(file);
                } catch (Exception e) {
                    log.error("获取打卡照片失败", e);
                }
            }
        });
        return pagination;
    }

    public List<ClockInLogResult> getClockInLogByDate(ClockInLogDateParam param) {
        List<AttClockInRecordsPO> clock = clockInRecordsRepository.getClockInLogByDate(param);
        return ObjectUtils.copying(clock, ClockInLogResult.class);
    }

    public ClockInInfoResult getClockInInof() {
        return this.getClockInInof(LocalDateTime.now());
    }

    public ClockInInfoResult getClockInInof(LocalDateTime clockInTime) {
        UserDTO user = userClient.getUser(SecurityContextHolder.getPrincipal().getId());
        Assert.isParameterError(Objects.isNull(user), "用户信息不存在");
        ClockInInfoResult result = new ClockInInfoResult();
        // 获取打卡规则
        AttClockRuleResult clockRule = clockRulesMapper.getClockRuleByUserId(SecurityContextHolder.getPrincipal().getId());
        Assert.isParameterError(Objects.isNull(clockRule), "您还没加入考勤组，请联系管理员");
        result.setClockRule(clockRule);
        // 获取当前班次
        List<AttClockSchedulingClassesDTO> attSchedulingList = schedulingQueryService.getStaffClockClass(clockInTime);
        if (ObjectUtils.isEmpty(attSchedulingList)) {
            log.error("{}:当前时间未获取到班次：{}", user.getId(), clockInTime);
            return result;
        }
        log.error("{}:当前时间所有班次：{}", user.getId(), attSchedulingList.size());
        List<AttClassesRecordResult> classesRecordList = new ArrayList<>();
        attSchedulingList.forEach(ex -> {
            log.error("{}:当前时间班次：{}", user.getId(), JsonUtils.serialize(ex));
            AttClassesRecordPO classesversion = classesRecordMapper.selectOne(
                    new LambdaQueryWrapper<AttClassesRecordPO>().eq(AttClassesRecordPO::getClassesId, ex.getId())
                            .eq(ObjectUtils.isNotEmpty(ex.getVersion()),
                                AttClassesRecordPO::getVersion, ex.getVersion())
                            .orderByDesc(AttClassesRecordPO::getVersion).last("limit 1"));
            AttClassesRecordResult classesRecord = AttClassesRecordResult.getAttClassesRecordResult(classesversion,
                                                                                                    ex.getClassDate());
            // 当前时间在上班最早打卡时间内并且在下班打卡最晚时间内才需要展示
            if (DateUtils.isIn(clockInTime, classesRecord.getStartClockBeforeTime(), classesRecord.getEndClockAfterTime())) {
                // 获取打卡信息
                List<AttClockInRecordsPO> clock = clockInRecordsRepository.getClockInInfo(user, classesversion, null, ex);
                classesRecord.setClockInfo(ObjectUtils.copying(clock, AttClockInRecordsResult.class));
                classesRecord.getClockInfo().forEach(clockInInof -> {
                    if (ObjectUtils.isNotEmpty(clockInInof.getActualClockInTime())) {
                        clockInInof.setActualClockInTime(clockInInof.getActualClockInTime().substring(11, 16));
                    }
                });
                classesRecordList.add(classesRecord);
            }
        });
        result.setClassesInfo(classesRecordList);
        clockInRecordsRepository.updateClockInRecords(user.getId());
        return result;
    }

    /**
     * app打卡
     *
     * @author: lijie
     * @date: 2023/9/21 19:00
     * @param: clockInParam 打卡信息
     * @return: 打卡结果
     **/
    public void appClockIn(ClockInParam clockInParam) {
        if (Objects.nonNull(clockInParam.getPhotoFileId())) {
            fileOperateClient.fileInduceSingle(fileSceneCode, null, clockInParam.getPhotoFileId());
        }
        clockInParam.setActualClockInTime(LocalDateTime.now());
        clockInParam.setUserId(SecurityContextHolder.getPrincipal().getId());
        try {
            UserDTO user = userClient.getUser(SecurityContextHolder.getPrincipal().getId());
            AttClockRuleResult clockRule = clockRulesMapper.getClockRuleByUserId(user.getId());
            Assert.isParameterError(Objects.isNull(clockRule), "未找到考勤组,请联系管理员");
            // 人脸识别
            if (clockRule.getFaceRecognition()) {
                // 人脸识别结果
                String faceRecognitionResult = faceRecognitionFactory.faceRecognition(clockInParam.getPhotoFileId(),
                                                                                      clockInParam.getPhotoFileUrl(), user);
                if (!IdentityVerifyEnum.SUCCESS.getRemarks().equals(faceRecognitionResult)) {
                    clockInParam.setFailReason(faceRecognitionResult);
                    clockInParam.setFaceRecognitionResult(IdentityVerifyEnum.FACE_DB_COMPARE_FAILED.getRemarks());
                    throw new ServiceException("人脸识别失败,请重试或联系管理员");
                }
                clockInParam.setFaceRecognitionResult(IdentityVerifyEnum.SUCCESS.getRemarks());
            }
            // 无班次只需要记录打卡日志
            if (ObjectUtils.isEmpty(clockInParam.getUuid())) {
                AttClockInRecordsPO clockInRecords = ObjectUtils.copying(clockInParam, AttClockInRecordsPO.class);
                this.getAttClockInRecordsPO(clockInRecords, LocalDateTime.now());
            }
            // 打卡
            this.clockIn(clockInParam, user);
        } catch (Exception e) {
            log.error("{}：app打卡失败：", clockInParam.getUserId(), e);
            AttClockInRecordsPO clockInRecords = ObjectUtils.copying(clockInParam, AttClockInRecordsPO.class);
            if (ObjectUtils.isEmpty(clockInRecords.getFailReason())) {
                try {
                    int length = e.getMessage().length();
                    clockInRecords.setFailReason(length > 200 ? e.getMessage().substring(0, 200) : e.getMessage());
                } catch (Exception ex) {
                    clockInRecords.setFailReason(ClockInErrorEnum.ERROR_MESSAGE.getRemarks());
                }
            }
            clockInRecords.setState(ClockInEnum.LOSE_CHECKED.name());
            this.getAttClockInRecordsPO(clockInRecords, LocalDateTime.now());
            throw new ServiceException(50 > e.getMessage().length() ? e.getMessage() : "打卡失败请联系管理员");
        }
    }

    /**
     * @param clockInParam 打卡信息
     *
     * @description:
     * @author: lijie
     * @date: 2023/10/12 13:43
     * @return: 打卡结果
     **/
    public Boolean clockIn(ClockInParam clockInParam, UserDTO user) {
        AttClockRuleResult clockRule = clockRulesMapper.getClockRuleByUserId(SecurityContextHolder.getPrincipal().getId());
        Assert.isParameterError(Objects.isNull(clockRule), "未找到打卡规则");
        Assert.isUnsupported(!clockRule.getWays().contains(clockInParam.getWays()), "暂不支持该打卡方式");
        // 根据唯一标识获取打卡记录
        AttClockInRecordsPO clockInRecords = clockInRecordsRepository.getClockInInfo(clockInParam.getUuid(),
                                                                                     clockInParam.getActualClockInTime());
        clockInRecords.setActualClockInTime(Optional.ofNullable(clockInParam.getActualClockInTime()).orElse(LocalDateTime.now()));
        // 获取班次
        AttClassesRecordPO classesRecord = classesRecordMapper.selectOne(
                new LambdaQueryWrapper<AttClassesRecordPO>().eq(AttClassesRecordPO::getClassesId, clockInRecords.getClassesId())
                        .eq(ObjectUtils.isNotEmpty(clockInRecords.getClassesVersion()),
                            AttClassesRecordPO::getVersion,
                            clockInRecords.getClassesVersion())
                        .orderByDesc(AttClassesRecordPO::getVersion).last("limit 1"));
        Assert.isParameterError(Objects.isNull(clockInRecords), "未找到班次信息");
        boolean bool = DateUtils.isIn(clockInRecords.getActualClockInTime(), clockInRecords.getClockBeforeTime(),
                                      clockInRecords.getClockAfterTime()) ||
                       clockInRecords.getActualClockInTime().equals(clockInRecords.getClockBeforeTime()) ||
                       clockInRecords.getActualClockInTime().equals(clockInRecords.getClockAfterTime());
        Assert.isNotAcceptable(!bool, "未到打卡时间，无效打卡");
        boolean start = ClockInEnum.START.name().equals(clockInRecords.getClockType());
        clockInRecords.setPastDataType(null);
        clockInRecords.setOutClock(clockInParam.getOutClock());
        clockInRecords.setWays(clockInParam.getWays());
        clockInRecords.setName(clockInParam.getName());
        clockInRecords.setPhotoFileId(clockInParam.getPhotoFileId());
        clockInRecords.setPhotoFileUrl(clockInParam.getPhotoFileUrl());
        clockInRecords.setFaceRecognitionResult(clockInParam.getFaceRecognitionResult());
        clockInRecords.setClockInAuthority(ClockInAuthorityEnum.MY_CLOCK.name());
        if (start) {
            clockInRecords.setClassesVersion(classesRecord.getVersion());
            clockInRecords.setOrgId(Optional.ofNullable(user.getOrganization()).orElse(new UserDTO.Organization()).getId());
        }
        AttClockInRecordsPO clock = start ?
                                    // 上班打卡
                                    clockInRecordsRepository.clockWork(clockInRecords) :
                                    // 下班打卡
                                    clockInRecordsRepository.afterAlockWork(clockInRecords);
        clockInLedgerApplication.updateLedgerDetails(clock);
        return ObjectUtils.isNotEmpty(clock);
    }

    /**
     * 考勤机打卡
     *
     * @author: lijie
     * @date: 2023/10/12 15:00
     * @param: clockInParam 打卡信息
     * @return: 打卡结果
     **/
    public Boolean machineClockIn(ClockInMachineParam machineParam) {
        UserDTO user = userClient.getUser(machineParam.getUserId());
        if (ObjectUtils.isEmpty(user)) {
            return false;
        }
        AttClockInRecordsPO clockInParam = ClockInParam.getClockInParam(machineParam, user.getName());
        try {
            ClockInInfoResult clock = this.getClockInInof(machineParam.getClockInTime());
            // 无班次打卡
            if (ObjectUtils.isEmpty(clock) || ObjectUtils.isEmpty(clock.getClassesInfo())) {
                log.error("无班次打卡");
                AttClockInRecordsPO clockInRecords = ClockInParam.getClockInParam(machineParam, user.getName());
                clockInRecords.setFailReason(ClockInErrorEnum.NO_CLASS.getRemarks());
                clockInRecords.setState(ClockInEnum.PROCESSED.name());
                this.getAttClockInRecordsPO(clockInRecords, LocalDateTime.now());
                return true;
            }
            // 只有一个班次直接打卡
            if (clock.getClassesInfo().size() == 1) {
                log.error("一个班次打卡");
                return this.clockIn(ClockInParam.getClockInParam(machineParam, clock.getClassesInfo().get(0).getClassesId(),
                                                                 clock.getClassesInfo().get(0).getClockInfo().get(0).getUuid()),
                                    user);
            } else {
                log.error("多班次打卡");
                // 多班次需要判断是打那个班次的卡
                boolean clockIn = true;
                // 打卡结果
                boolean clockInResult = false;
                bool:
                for (AttClassesRecordResult result : clock.getClassesInfo()) {
                    for (AttClockInRecordsResult obj : result.getClockInfo()) {
                        try {
                            if (ClockInEnum.NOT_CHECKED.name().equals(obj.getState())) {
                                clockInResult = this.clockIn(
                                        ClockInParam.getClockInParam(machineParam, result.getClassesId(), obj.getUuid()), user);
                                clockIn = false;
                                break bool;
                            }
                        } catch (Exception e) {
                            log.error("考勤机打卡异常：", e);
                        }
                    }
                }
                // 如果没有班次未打卡则更新最后一个班次
                if (clockIn) {
                    int i = clock.getClassesInfo().size() - 1;
                    this.clockIn(ClockInParam.getClockInParam(machineParam, clock.getClassesInfo().get(i).getClassesId(),
                                                              clock.getClassesInfo().get(i).getClockInfo().getFirst().getUuid()),
                                 user);
                    clockInResult = true;
                }
                Assert.isUnknownError(!clockInResult, "打卡失败，记录日志");
            }
            return true;
        } catch (Exception e) {
            log.error("考勤机打卡异常记录日志：", e);
            try {
                int length = e.getMessage().length();
                clockInParam.setFailReason(length > 100 ? e.getMessage().substring(0, 100) : e.getMessage());
            } catch (Exception ex) {
                clockInParam.setFailReason(ClockInErrorEnum.ERROR_MESSAGE.getRemarks());
            }
            clockInParam.setState(ClockInEnum.LOSE_CHECKED.name());
            this.getAttClockInRecordsPO(clockInParam, machineParam.getClockInTime());
            return true;
        }
    }

    public Boolean addClockIn(AddClockInParam param) {
        AttClockInRecordsPO clockInRecords = clockInRecordsRepository.getById(param.getId());
        clockInRecordsRepository.updateClockInRecords(clockInRecords.getId(), ClockInEnum.REVISE.name());
        // 新增打卡记录
        AttClockInRecordsPO records = clockInRecords.convertAttClockInRecordsPO();
        records.setActualClockInTime(param.getActualClockInTime());
        records.setClockInAuthority(ClockInAuthorityEnum.PC_CLOCK.name());
        if (ClockInEnum.START.name().equals(records.getClockType())) {
            records.setState(DateUtils.compare(param.getActualClockInTime(), clockInRecords.getRequireClockInTime()) > 0
                             ? ClockInEnum.LATE_CHECKED.name() : ClockInEnum.NORMAL.name());
            if (ClockInEnum.LATE_CHECKED.name().equals(records.getState())) {
                long minute = DateUtils.between(records.getRequireClockInTime(), records.getActualClockInTime(), ChronoUnit.MINUTES,
                                                true);
                records.setDeletionDuration(minute);
            }
        } else {
            records.setState(DateUtils.compare(param.getActualClockInTime(), clockInRecords.getRequireClockInTime()) < 0
                             ? ClockInEnum.EARLY_CHECKED.name() : ClockInEnum.NORMAL.name());
            if (ClockInEnum.EARLY_CHECKED.name().equals(records.getState())) {
                long minute = DateUtils.between(records.getActualClockInTime(), records.getRequireClockInTime(), ChronoUnit.MINUTES,
                                                true);
                records.setDeletionDuration(minute);
            }
        }
        Boolean bool = clockInRecordsRepository.addClockLog(records);
        // 更新台账
        clockInLedgerApplication.updateLedgerDetails(records);
        return bool;
    }

    /**
     * 生成打卡任务
     */
    public Boolean addClockIn() {
        // 每分钟最多生成300条，数据量太大会导致执行时间过长
        List<AttSchedulingPO> schedulingList = schedulingMapper.selectList(
                new LambdaQueryWrapper<AttSchedulingPO>().eq(AttSchedulingPO::getClockRead, Boolean.FALSE)
                        .le(AttSchedulingPO::getSchedulingDate, LocalDateTime.now())
                        .orderByDesc(AttSchedulingPO::getSchedulingDate).last("limit 300"));
        log.info("共{}条班次需要生成任务", Optional.ofNullable(schedulingList).orElse(new ArrayList<>()).size());
        if (CollectionUtils.isEmpty(schedulingList)) {
            return true;
        }
        schedulingList.parallelStream().forEach(ex -> {
            log.info("{}:开始生成任务", JsonUtils.serialize(ex));
            try {
                SessionContextHolder.requestTenant(ex.getTenant());
                if (ObjectUtils.isNotEmpty(ex.getClassesId())) {
                    Principal principal = Principal.builder().id(ex.getStaffId()).name(ex.getStaffName()).type(Principal.Type.USER).build();
                    SecurityContextHolder.setPrincipal(principal);
                    AttClassesRecordPO classesversion = classesQueryService.getClassesRecordById(ex.getClassesId(),
                                                                                                 ex.getClassesVersion());
                    if (ObjectUtils.isNotEmpty(classesversion)) {
                        UserDTO.Organization organization = new UserDTO.Organization();
                        if (Objects.isNull(ex.getStaffDeptId())) {
                            UserDTO user = userClient.getUser(ex.getStaffId());
                            organization = user != null ? user.getOrganization() : null;
                        } else {
                            organization.setId(ex.getStaffDeptId());
                            organization.setName(ex.getStaffDeptName());
                        }
                        UserDTO user = new UserDTO();
                        user.setId(ex.getStaffId());
                        user.setOrganization(organization);
                        clockInRecordsRepository.addClock(user, classesversion, ex.getSchedulingDate(), ex.getId());
                    } else {
                        log.error("班次：{}，生成打卡任务失败,未找到班次：{}", classesversion, ex.getClassesName());
                    }
                } else {
                    ex.setClockRead(Boolean.TRUE);
                    schedulingMapper.updateById(ex);
                }
            } catch (Exception e) {
                log.error("{}:生成打卡任务失败", ex.getStaffName(), e);
            }
        });
        return true;
    }

    /**
     * pc打卡记录修改
     *
     * @author: lijie
     * @date: 2023/10/10 15:50
     * @param:
     * @return: 修改结果
     **/
    public Boolean updateClockIn(Long id) {
        AttClockInRecordsPO attClockInRecords = clockInRecordsRepository.updateClockIn(id);
        if (ObjectUtils.isNotEmpty(attClockInRecords)) {
            if (ClockInAuthorityEnum.DISCARD_CLOCK.name().equals(attClockInRecords.getClockInAuthority())) {
                this.updateClockIn(ClockInEnum.START.name().equals(attClockInRecords.getClockType()), attClockInRecords);
            }
            clockInLedgerApplication.updateLedgerDetails(attClockInRecords);
        }
        return true;
    }

    /**
     * 作废打卡记录
     */
    public Boolean updateClockIn(Boolean start, AttClockInRecordsPO clock) {
        Integer today = clock.getClockInDate().getDayOfMonth();
        String month = DateUtils.format(clock.getClockInDate(), "yyyy-MM");
        AttClockInLedgerDetailsPO details = clockInLedgerDetailsMapper.getLedgerDetails(clock, month, today);
        Assert.isParameterError(Objects.isNull(details), "未获取到台账");
        if (start) {
            details.setStartWorkState(null);
            details.setStartWorkDetails(null);
            details.setStartWorkOmit(Boolean.FALSE);
            details.setStartWorkTime(null);
            details.setStartWorkOut(Boolean.FALSE);
        } else {
            details.setEndWorkState(null);
            details.setEndWorkDetails(null);
            details.setEndWorkOmit(Boolean.FALSE);
            details.setEndWorkTime(null);
            details.setEndWorkOut(Boolean.FALSE);
        }
        // 上班缺卡+下班缺卡=旷工
        // 上班正常+下班正常=正常
        // 上班正常+下班未打卡=正常
        // 其他情况都为异常
        if (ClockInEnum.LACK_CHECKED.name().equals(details.getStartWorkState()) &&
            ClockInEnum.LACK_CHECKED.name().equals(details.getEndWorkState())) {
            details.setState(ClockInEnum.NEGLECT_WORK.name());
        } else if (ClockInEnum.NORMAL.name().equals(details.getStartWorkState()) ||
                   ClockInEnum.NORMAL.name().equals(details.getEndWorkState())) {
            details.setState(ClockInEnum.NORMAL.name());
        } else if (ClockInEnum.NORMAL.name().equals(details.getStartWorkState()) &&
                   ObjectUtils.isEmpty(details.getEndWorkState())) {
            details.setState(ClockInEnum.NORMAL.name());
        } else {
            details.setState(ClockInEnum.ABNORMAL_WORK.name());
        }
        return clockInLedgerDetailsMapper.updateById(details) > 0;
    }

    public AttClockInRecordslogResult getClockInInfoByUser(String clockInDate) {
        AttClockInRecordslogResult logResult = new AttClockInRecordslogResult();
        logResult.setApplyDetails(approveLogRepository.getApproveLogAllByDate(clockInDate));
        // 打卡信息
        List<ClockInInfoClassesResult> clock = clockInRecordsMapper.getClockInInfoByUser(clockInDate,
                                                                                         SecurityContextHolder.getPrincipal()
                                                                                                 .getId());
        List<ClockInInfoClassesResult> futureClock = new ArrayList<>();
        clock.forEach(ex -> ex.getRecords()
                .forEach(obj -> obj.setActualClockInTime(DateUtils.format(DateUtils.parse(obj.getActualClockInTime()), DateFormat.DEFAULT_TIME_FORMAT))));
        // 查询未来时间的班次
        if (DateUtils.compare(DateUtils.parseDate(clockInDate), DateUtils.beginOfDay(LocalDateTime.now())) >= 0) {
            List<AttSchedulingClassesDTO> classesResult = schedulingQueryService.getStaffDayScheduling(
                    SecurityContextHolder.getPrincipal().getId(), clockInDate);
            for (AttSchedulingClassesDTO ex : classesResult) {
                ClockInInfoClassesResult clockInInfoClassesResult = new ClockInInfoClassesResult();
                clockInInfoClassesResult.setId(ex.getId());
                clockInInfoClassesResult.setName(ex.getName());
                clockInInfoClassesResult.setStartWorkTime(DateUtils.format(ex.getStartWorkTime()).substring(0, 5));
                clockInInfoClassesResult.setEndWorkTime(DateUtils.format(ex.getEndWorkTime()).substring(0, 5));
                List<ClockInRecordsResult> resultList = new ArrayList<>();
                ClockInRecordsResult result = new ClockInRecordsResult();
                result.setClockType(ClockInEnum.START.name());
                result.setState(ClockInEnum.NOT_CHECKED.name());
                resultList.add(result);
                result = new ClockInRecordsResult();
                result.setClockType(ClockInEnum.END.name());
                result.setState(ClockInEnum.NOT_CHECKED.name());
                resultList.add(result);
                clockInInfoClassesResult.setRecords(resultList);
                futureClock.add(clockInInfoClassesResult);
            }
        }
        if (ObjectUtils.isNotEmpty(clock) && ObjectUtils.isNotEmpty(futureClock) && clock.size() < futureClock.size()) {
            futureClock.forEach(ex -> clock.forEach(in -> {
                if (ex.getId().equals(in.getId())) {
                    ex.setRecords(in.getRecords());
                }
            }));
            logResult.setClassesDetails(futureClock);
        } else {
            logResult.setClassesDetails(ObjectUtils.isNotEmpty(clock) ? clock : futureClock);
        }
        return logResult;
    }

    public List<ClockInInLedgerByUserResult> getClockInInLedgerByUser(String ledgerDate) {
        Assert.isParameterError(StringUtils.isBlank(ledgerDate), "时间不能为空");
        Long userId = SecurityContextHolder.getPrincipal().getId();
        List<ClockInInLedgerByUserResult> resultList = new ArrayList<>();
        // 本月所有排班
        List<AttSchedulingDateDOT> schedulingDate = schedulingQueryService.getScheduling(userId, ledgerDate);
        schedulingDate.forEach(ex -> {
            int today = ex.getDate().getDayOfMonth();
            ClockInInLedgerByUserResult result = new ClockInInLedgerByUserResult();
            result.setToday(today < 10 ? ledgerDate + "-0" + today : ledgerDate + "-" + today);
            // 当天是否有班次
            if (ObjectUtils.isNotEmpty(ex.getClassesDTOList())) {
                result.setClasses(ex.getClassesDTOList().size() > 1 ||
                                  ObjectUtils.isNotEmpty(ex.getClassesDTOList().getFirst().getStartWorkTime()));
            }
            if (result.getClasses()) {
                // 是否存在打卡异常
                AttClockInRecordsPO record = clockInRecordsMapper.getClockInInAbnormalfoByUser(result.getToday(), userId);
                if (ObjectUtils.isNotEmpty(record)) {
                    result.setNormal(ClockInEnum.ABNORMAL_WORK.name());
                } else {
                    record = clockInRecordsMapper.getClockInInNormalByUser(result.getToday(), userId);
                    if (ObjectUtils.isNotEmpty(record)) {
                        result.setNormal(ClockInEnum.NORMAL.name());
                    }
                }
            }
            result.setApply(approveLogRepository.getApproveLogIsData(result.getToday()));
            resultList.add(result);
        });
        return resultList;
    }

    public void getClockInLogExcel(HttpServletRequest request, HttpServletResponse response, ClockInLogPageParam param) {
        List<AttClockInRecordsPO> clock = clockInRecordsMapper.selectList(
                new LambdaQueryWrapper<AttClockInRecordsPO>().isNotNull(AttClockInRecordsPO::getActualClockInTime)
                        .eq(ObjectUtils.isNotEmpty(param.getOrgId()),
                            AttClockInRecordsPO::getOrgId, param.getOrgId())
                        .eq(ObjectUtils.isNotEmpty(param.getWays()),
                            AttClockInRecordsPO::getWays, param.getWays())
                        .between(AttClockInRecordsPO::getActualClockInTime,
                                 param.getPeriod().getBegin(), param.getPeriod().getEnd())
                        .like(ObjectUtils.isNotEmpty(param.getUserName()),
                              AttClockInRecordsPO::getUserName, param.getUserName())
                        .orderByDesc(AttClockInRecordsPO::getActualClockInTime));
        Assert.isParameterError(CollectionUtils.isEmpty(clock), "暂无数据，无需导出");
        List<ClockInLogResult> clockInLog = ObjectUtils.copying(clock, ClockInLogResult.class);
        List<OrganizationDTO> organizationDTOList = organizationClient.getOrganizations(new OrganizationQueryParam());
        Map<Long, OrganizationDTO> orgMap = organizationDTOList.stream().collect(Collectors.toMap(OrganizationDTO::getId, v -> v));
        clockInLog.forEach(ex -> {
            if (ObjectUtils.isNotEmpty(ex.getOrgId())) {
                OrganizationDTO organization = orgMap.get(ex.getOrgId());
                ex.setOrgName(Optional.ofNullable(organization).orElse(new OrganizationDTO()).getName());
            }
            ex.setWays(AttClockWayEnum.getValueByName(ex.getWays()));
            ex.setExcelDate(DateUtils.formatDate(ex.getActualClockInTime()));
            ex.setExcelDateTime(DateUtils.format(ex.getActualClockInTime(), "HH:mm:ss"));
        });
        //导出excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("excelDate", "日期");
        writer.addHeaderAlias("userName", "姓名");
        writer.addHeaderAlias("orgName", "部门");
        writer.addHeaderAlias("excelDateTime", "打卡时间");
        writer.addHeaderAlias("ways", "打卡方式");
        writer.addHeaderAlias("name", "打卡地点");
        writer.setOnlyAlias(true);
        //默认配置
        writer.write(clockInLog, true);
        writer.setColumnWidth(-1, 25);
        String fileName = "打卡日志";
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    /**
     * 打卡失败发送消息
     */
    public void sendMessage(AttClockInRecordsPO clockInRecords) {
        RocketMQContextHolder.send(AttendanceClockInMessage.TOPIC,
                                   AttendanceClockInMessage.builder().clockInfo(clockInRecords).build());
    }

    public void getAttClockInRecordsPO(AttClockInRecordsPO clockInRecords, LocalDateTime clockInTime) {
        clockInRecords.setActualClockInTime(clockInTime);
        clockInRecords.setUserId(clockInRecords.getUserId());
        UserDTO user = userClient.getUser(clockInRecords.getUserId());
        if (ObjectUtils.isNotEmpty(user)) {
            clockInRecords.setUserName(user.getName());
            if (ObjectUtils.isNotEmpty(user.getOrganization())) {
                clockInRecords.setOrgId(user.getOrganization().getId());
            }
        }
        clockInRecords.setUuid(IdUtils.fastUUIDString());
        clockInRecords.setClockType(ClockInEnum.LOG.name());
        if (ObjectUtils.isEmpty(clockInRecords.getState())) {
            clockInRecords.setState(ClockInEnum.LOSE_CHECKED.name());
        }
        clockInRecords.setTenant(SessionContextHolder.getRequester().getTenant());
        this.sendMessage(clockInRecords);
    }
}
