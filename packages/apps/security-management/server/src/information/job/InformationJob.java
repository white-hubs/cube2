package com.guhecloud.cube.srv.gen.securitymanagement.server.information.job;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseDetailsDTO;
import com.guhecloud.cube.base.device.center.metadata.std.param.DevicePropWriteCmd;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterAttrCtlStdClient;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterTagStdClient;
import com.guhecloud.cube.base.dictionary.client.SysDictionaryClient;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.dao.InfoScreenScheduleAssocMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.dao.InformationPlanMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.dao.TimeRecordMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.entity.PlayRecord;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.entity.ProgramEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.entity.ProgramScheduleEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.entity.TimeRecord;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.param.InformationPlanParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.service.InformationPlanService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.service.PlayRecordService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.service.ProgramScheduleService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.service.ProgramService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemEndpointConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import com.guhecloud.subsystem.requester.sdk.client.InformationClient;
import com.guhecloud.subsystem.requester.sdk.param.information.InformationParam;
import com.guhecloud.subsystem.requester.sdk.param.information.ProgramScheduleParam;
import com.guhecloud.subsystem.requester.sdk.vo.information.ProgramScheduleVo;
import com.guhecloud.subsystem.requester.sdk.vo.information.ProgramVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InformationJob {
    private final InformationClient informationClient;

    private final TimeRecordMapper timeRecordMapper;

    private final PlayRecordService playRecordService;

    private final ProgramScheduleService programScheduleService;

    private final ProgramService programService;

    private final SubsystemRedisUtils subsystemRedisUtils;

    private final StringRedisTemplate stringRedisTemplate;

    private final DeviceCenterTagStdClient deviceCenterTagStdClient;

    private final DeviceCenterAttrCtlStdClient deviceCenterAttrCtlStdClient;

    private final InfoScreenScheduleAssocMapper infoScreenScheduleAssocMapper;

    private final InformationPlanMapper informationPlanMapper;

    private final SysDictionaryClient sysDictionaryClient;

    private final InformationPlanService informationPlanService;

    private static final String BUILTIN_APP = "builtin.app::";

    private static final String INFORMATION_PUBLISH = "INFORMATION_PUBLISH";

    private static final String SCREEN_CONTROL_CODE = "screen_control";

    /**
     * 信息发布播放记录同步
     *
     * @param param
     *
     * @return
     */
    @JobAnnotation(name = "executeInformationPlayRecordJob", cron = "0 0 * * * ?", desc = "信息发布播放记录同步")
    public void executePlayRecordJob(String param) {
        SessionContextHolder.requestTenant(Integer.valueOf(param));
        var timeRecord = timeRecordMapper.selectOne(null);
        var date = LocalDateTime.now();
        if (Objects.isNull(timeRecord)) {
            var informationParam = new InformationParam();
            var offset = DateUtils.offset(date, ChronoUnit.DAYS, -30);
            informationParam.setStartTime(DateUtils.format(offset, "yyyy-MM-dd HH:mm:ss"));
            informationParam.setEndTime(DateUtils.format(date, "yyyy-MM-dd HH:mm:ss"));
            var payLogs = informationClient.getPayLogs(informationParam);
            var copying = ObjectUtils.copying(payLogs, PlayRecord.class);
            playRecordService.saveBatch(copying);
            var timeRecord1 = new TimeRecord();
            timeRecord1.setRecordTime(date);
            timeRecordMapper.insert(timeRecord1);
        } else {
            var informationParam = new InformationParam();
            informationParam.setStartTime(DateUtils.format(timeRecord.getRecordTime(), "yyyy-MM-dd HH:mm:ss"));
            informationParam.setEndTime(DateUtils.format(date, "yyyy-MM-dd HH:mm:ss"));
            var payLogs = informationClient.getPayLogs(informationParam);
            var copying = ObjectUtils.copying(payLogs, PlayRecord.class);
            playRecordService.saveBatch(copying);
            timeRecord.setRecordTime(date);
            timeRecordMapper.updateById(timeRecord);
        }
    }

    /**
     * 采集节目库列表信息
     *
     * @return
     */
    @JobAnnotation(name = "fetchingProgramHandler", cron = "0 0 * * * ?", desc = "采集节目库列表信息")
    public void fetchingProgramHandler(String param) {
        var subEndpointRefreshConfigs = subsystemRedisUtils.getSubEndpointRefreshConfigs(SubsystemTypeConstants.INFORMATION_SERVER);
        subEndpointRefreshConfigs.forEach(config -> {
            SessionContextHolder.requestTenant(config.getTenant());

            var programScheduleTimeKey =
                    "securitymanagement:information:" + config.getServiceAddrSuffix() + ":programTime:" + config.getTenant();
            var programScheduleTime = stringRedisTemplate.opsForValue().get(programScheduleTimeKey);
            var date = DateUtils.now();
            var endTime = DateUtils.format(date, "yyyy-MM-dd");
            if (Objects.isNull(programScheduleTime)) {
                var offset = DateUtils.offset(date, ChronoUnit.DAYS, -30);
                programScheduleTime = DateUtils.format(offset, "yyyy-MM-dd");
            }
            //采集节目库列表信息
            var programScheduleParam = ProgramScheduleParam.builder()
                    .startTime(programScheduleTime.toString())
                    .endTime(endTime)
                    .build();
            var programVos = informationClient.getProgramList(programScheduleParam);
            var programEntities = copyToProgram(programVos, config);

            programService.insertOrUpdateProgram(programEntities);
            stringRedisTemplate.opsForValue().set(programScheduleTimeKey, endTime);

        });
    }

    private List<ProgramEntity> copyToProgram(List<ProgramVo> programVos, SubsystemEndpointConfig config) {
        return programVos.stream().map(vo -> {
            Long id = IdWorker.getId();
            ProgramEntity programEntity = ProgramEntity.builder().id(id)
                    .thirdProgramId(vo.getName())
                    .programName(vo.getName())
                    .categoryName(vo.getCategoryName())
                    .build();
            programEntity.setTenant(config.getTenant());
            programEntity.setDeleted(Boolean.FALSE);
            EntityAuditor.populateCreateAudit(programEntity);
            EntityAuditor.populateUpdateAudit(programEntity);
            return programEntity;
        }).collect(Collectors.toList());

    }

    /**
     * 采集每个终端的节目列表信息
     *
     * @param param
     *
     * @return
     */
    @JobAnnotation(name = "fetchingProgramScheduleHandler", cron = "0 0 * * * ?", desc = "采集每个终端的节目列表信息")
    public void fetchingProgramScheduleHandler(String param) {
        var subEndpointRefreshConfigs = subsystemRedisUtils.getSubEndpointRefreshConfigs(SubsystemTypeConstants.INFORMATION_SERVER);
        subEndpointRefreshConfigs.forEach(config -> {
            SessionContextHolder.requestTenant(config.getTenant());

            var programScheduleTimeKey =
                    "securitymanagement:information:" + config.getServiceAddrSuffix() + ":programScheduleTime:" + config.getTenant();
            var programScheduleTime = stringRedisTemplate.opsForValue().get(programScheduleTimeKey);
            var date = DateUtils.now();
            var endTime = DateUtils.format(date, "yyyy-MM-dd");
            if (Objects.isNull(programScheduleTime)) {
                var offset = DateUtils.offset(date, ChronoUnit.DAYS, -30);
                programScheduleTime = DateUtils.format(offset, "yyyy-MM-dd");
            }
            //根据标签获取第三方设备id集合
            Map<String, Object> tagRefDeviceMap = new HashMap<>();
            tagRefDeviceMap.put("tagCode", BUILTIN_APP + INFORMATION_PUBLISH);
            var thirdPartyIdListByTagCode = deviceCenterTagStdClient.getThirdPartyIdListByTagCode(tagRefDeviceMap);
            //根据设备id获取设备名称
            var deviceMap = thirdPartyIdListByTagCode.stream()
                    .filter(deviceBaseDetailsDTO -> Objects.nonNull(deviceBaseDetailsDTO.getThirdPartyId()))
                    .collect(Collectors.toMap(
                            DeviceBaseDetailsDTO::getThirdPartyId,
                            device -> device,
                            (existing, replacement) -> existing
                                             ));
            var keys = deviceMap.keySet();
            var programScheduleParam = ProgramScheduleParam.builder()
                    .startTime(programScheduleTime.toString())
                    .endTime(endTime)
                    .clientIds(new ArrayList<>(keys))
                    .build();
            var programScheduleList = informationClient.getProgramScheduleList(programScheduleParam);
            var programScheduleEntities = copyToProgramSchedule(programScheduleList, config, deviceMap);

            programScheduleService.insertOrUpdateProgramSchedule(programScheduleEntities);
            stringRedisTemplate.opsForValue().set(programScheduleTimeKey, endTime);

        });
    }

    /**
     * 转换实体
     *
     * @param vos
     * @param config
     * @param deviceMap
     *
     * @return
     */
    private List<ProgramScheduleEntity> copyToProgramSchedule(List<ProgramScheduleVo> vos, SubsystemEndpointConfig config,
                                                              Map<String, DeviceBaseDetailsDTO> deviceMap
                                                             ) {
        return vos.stream().map(vo -> {
            Long id = IdWorker.getId();
            ProgramScheduleEntity programScheduleEntity = ProgramScheduleEntity.builder().id(id)
                    .clientId(vo.getClientId())
                    .clientName(Objects.isNull(deviceMap.get(vo.getClientId())) ? "" : deviceMap.get(vo.getClientId()).getDeviceName())
                    .thirdPartyId(vo.getId())
                    .thirdProgramId(vo.getProgramId())
                    .name(vo.getName())
                    .type(vo.getType())
                    .beginTime(vo.getBeginTime())
                    .endTime(vo.getEndTime())
                    .weeks(vo.getWeeks())
                    .priority(vo.getPriority())
                    .state(vo.getState())
                    .build();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            LocalDate beginTime = LocalDate.parse(vo.getBeginDate(), formatter);
            var beginTimeInstant = beginTime.atStartOfDay(ZoneId.systemDefault()).toLocalDate();
            programScheduleEntity.setBeginDate(beginTimeInstant);

            LocalDate endTime = LocalDate.parse(vo.getBeginDate(), formatter);
            var endTimeInstant = endTime.atStartOfDay(ZoneId.systemDefault()).toLocalDate();
            programScheduleEntity.setEndDate(endTimeInstant);

            programScheduleEntity.setTenant(config.getTenant());
            programScheduleEntity.setDeleted(Boolean.FALSE);
            EntityAuditor.populateCreateAudit(programScheduleEntity);
            EntityAuditor.populateUpdateAudit(programScheduleEntity);
            return programScheduleEntity;
        }).collect(Collectors.toList());

    }

    /**
     * 开关屏配置的计划handler
     *
     * @param param 开关屏配置项的id和控制下发的命令
     *
     * @return
     */
    public void informationPlanHandler(String param) throws JsonProcessingException, NoSuchFieldException {
        SessionContextHolder.clearSessionContext();
        // 获取信息发布在字典库中配置的下发的属性code
        var sysDictionaryByCode = sysDictionaryClient.getSysDictionaryByCode(SCREEN_CONTROL_CODE);
        if (Objects.isNull(sysDictionaryByCode)) {
            throw new NoSuchFieldException("未获取到字典库中配置的需要下发的属性code");
        }
        var variableCode = sysDictionaryByCode.getVariable();
        var mapper = new ObjectMapper();
        // 使用TypeFactory来指定Map的类型
        Map<String, Object> paramMap = mapper.readValue(param, TypeFactory.defaultInstance()
                .constructMapType(Map.class, String.class, Object.class));
        // 获取需下发的设备列表
        var infoScreenScheduleAssocEntities = infoScreenScheduleAssocMapper.getByInformationPlanId(Long.parseLong(paramMap
                                                                                                                          .get("id")
                                                                                                                          .toString()));
        // 定时下发
        infoScreenScheduleAssocEntities.forEach(infoScreenScheduleAssocEntity -> {
            SessionContextHolder.requestTenant(infoScreenScheduleAssocEntity.getTenant());
            //属性写值对象
            var propWriteValue = new DevicePropWriteCmd.PropWriteValue();
            propWriteValue.setCode(variableCode);
            propWriteValue.setExpectedValue(paramMap.get("screenControl").toString());
            Set<DevicePropWriteCmd.PropWriteValue> propWriteValues = new HashSet<>();
            propWriteValues.add(propWriteValue);

            var deviceAttrCtlCommand = new DevicePropWriteCmd();
            deviceAttrCtlCommand.setDeviceId(infoScreenScheduleAssocEntity.getDeviceId());
            deviceAttrCtlCommand.setPropWriteValues(propWriteValues);
            deviceCenterAttrCtlStdClient.controlAttribute(deviceAttrCtlCommand);
        });
    }

    /**
     * 启用or停用 开关屏配置的计划handler
     *
     * @return
     */
    @JobAnnotation(name = "checkInformationPlanHandler", cron = "0 0/5 * * * ?", desc = "信息发布-启用or停用开关屏配置的计划handler")
    public void checkInformationPlanHandler(String param) {
        var now = LocalDateTime.now();
        var planParam = new InformationPlanParam();
        planParam.setIsEnable(1);
        //获取需要启用的计划
        informationPlanMapper.getInformationPlanListNeedStart(planParam, now).forEach(informationPlanNeedStartVO -> {
            if (!Objects.isNull(informationPlanNeedStartVO.getOpenJobTaskId())) {
                informationPlanService.startPlan(informationPlanNeedStartVO.getOpenJobTaskId());
            }
            if (!Objects.isNull(informationPlanNeedStartVO.getCloseJobTaskId())) {
                informationPlanService.startPlan(informationPlanNeedStartVO.getCloseJobTaskId());
            }
        });
        //获取需要停止的计划
        informationPlanMapper.getInformationPlanListNeedStop(planParam, now).forEach(informationPlanNeedStartVO -> {
            if (!Objects.isNull(informationPlanNeedStartVO.getOpenJobTaskId())) {
                informationPlanService.stopPlan(informationPlanNeedStartVO.getOpenJobTaskId());
            }
            if (!Objects.isNull(informationPlanNeedStartVO.getCloseJobTaskId())) {
                informationPlanService.stopPlan(informationPlanNeedStartVO.getCloseJobTaskId());
            }
        });
    }
}
