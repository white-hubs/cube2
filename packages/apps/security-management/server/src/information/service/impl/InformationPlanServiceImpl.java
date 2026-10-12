package com.guhecloud.cube.srv.gen.securitymanagement.server.information.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.dao.SecurityCommonMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity.SecurityDevice;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.param.DeviceFilterPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.SecurityDeviceInfoVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.dao.InfoScreenScheduleAssocMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.dao.InformationPlanMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.dto.InformationDeviceCountDTO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.dto.InformationPlanDTO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.entity.InfoScreenScheduleAssocEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.entity.InformationPlanEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.enums.CronEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.job.InformationJob;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.param.InformationPlanParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.service.InformationPlanService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.vo.InformationPlanVO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectIdUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.event.src.annotation.EventBusListen;
import com.guhecloud.platform.event.src.common.SysReadEvent;
import com.guhecloud.platform.job.jobtask.service.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@EventBusListen(order = 2)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InformationPlanServiceImpl extends ServiceImpl<InformationPlanMapper, InformationPlanEntity> implements InformationPlanService {

    private final InformationPlanMapper informationPlanMapper;
    private final InfoScreenScheduleAssocMapper infoScreenScheduleAssocMapper;
    private final SecurityCommonMapper securityCommonMapper;
    private final JobService jobService;
    private static final String JOB_NAME = "informationPlanHandler";
    private static final String CALL_BACK_METHOD = "informationPlanHandler";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveInformationPlan(InformationPlanDTO informationPlanDTO) {
        InformationPlanEntity infoPlanEntity = new InformationPlanEntity();
        long id = IdWorker.getId();
        if (Objects.isNull(informationPlanDTO.getId())) {
            infoPlanEntity = copyAddInformationPlanEntity(informationPlanDTO);
            //新增
            infoPlanEntity.setId(id);
            //推送开屏的job
            infoPlanEntity.setOpenJobTaskId(ObjectIdUtils.next());
            infoPlanEntity.setCloseJobTaskId(ObjectIdUtils.next());
            createJob(infoPlanEntity.getOpenJobTaskId(), infoPlanEntity, 1,
                      informationPlanDTO.getPlanName() + "_开屏", informationPlanDTO.getOpenTime());
            //推送关屏的job
            createJob(infoPlanEntity.getCloseJobTaskId(), infoPlanEntity, 0,
                      informationPlanDTO.getPlanName() + "_关屏", informationPlanDTO.getCloseTime());
            informationPlanMapper.insert(infoPlanEntity);
        } else {
            id = informationPlanDTO.getId();
            InformationPlanEntity informationPlanEntityOld = informationPlanMapper.selectById(informationPlanDTO.getId());
            infoPlanEntity = copyUpdateInformationPlanEntity(informationPlanEntityOld, informationPlanDTO);
            if (!ObjectUtils.isEmpty(informationPlanDTO.getOpenTime())
                && !ObjectUtils.isEmpty(informationPlanEntityOld.getOpenTime())
                && informationPlanDTO.getOpenTime().equals(informationPlanEntityOld.getOpenTime())) {
                //删除之前那开屏的job
                if (!ObjectUtils.isEmpty(informationPlanEntityOld.getOpenJobTaskId())) {
                    jobService.removeJob(getJobName(informationPlanEntityOld.getOpenJobTaskId()));
                } else {
                    infoPlanEntity.setOpenJobTaskId(ObjectIdUtils.next());
                }
                //推送新的开屏的job
                createJob(infoPlanEntity.getOpenJobTaskId(), infoPlanEntity, 1,
                          informationPlanDTO.getPlanName() + "_开屏", informationPlanDTO.getOpenTime());
            }
            if (!ObjectUtils.isEmpty(informationPlanDTO.getCloseTime())
                && !ObjectUtils.isEmpty(informationPlanEntityOld.getCloseTime())
                && informationPlanDTO.getCloseTime().equals(informationPlanEntityOld.getCloseTime())) {
                //删除之前那关屏的xxljob
                if (!ObjectUtils.isEmpty(informationPlanEntityOld.getCloseJobTaskId())) {
                    jobService.removeJob(getJobName(informationPlanEntityOld.getCloseJobTaskId()));
                } else {
                    infoPlanEntity.setCloseJobTaskId(ObjectIdUtils.next());
                }
                //推送新的关屏的xxljob
                createJob(infoPlanEntity.getCloseJobTaskId(), infoPlanEntity, 0,
                          informationPlanDTO.getPlanName() + "_关屏", informationPlanDTO.getCloseTime());
            }
            //先删除之前已加的关联表数据
            infoScreenScheduleAssocMapper.deleteByInformationPlanId(informationPlanDTO.getId());
            //修改
            informationPlanMapper.updateById(infoPlanEntity);
        }
        //保存关联表
        if (!CollectionUtils.isEmpty(informationPlanDTO.getSecurityDevices())) {
            infoScreenScheduleAssocMapper.saveAll(copyInfoScreenScheduleAssoc(informationPlanDTO.getSecurityDevices(), id));
        }
        //如果是在有效期内 则立即启动job
        startOnceJob(infoPlanEntity);
    }

    @Override
    public InformationPlanDTO getMaterialsByProgramIds(Long id) {
        InformationPlanEntity informationPlanEntity = informationPlanMapper.selectById(id);
        InformationPlanDTO informationPlanDTO = copyInformationPlanDTO(informationPlanEntity);

        List<InfoScreenScheduleAssocEntity> infoScreenScheduleAssocEntities = infoScreenScheduleAssocMapper.selectList(Wrappers
                                                                                                                               .<InfoScreenScheduleAssocEntity>lambdaQuery()
                                                                                                                               .eq(InfoScreenScheduleAssocEntity::getInformationPlanId, informationPlanEntity.getId()));
        List<Long> securityDeviceIds = infoScreenScheduleAssocEntities.stream().map(InfoScreenScheduleAssocEntity::getSecurityDeviceId)
                .collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(securityDeviceIds)) {
            DeviceFilterPageParam param = new DeviceFilterPageParam();
            param.setIds(securityDeviceIds);
            List<SecurityDevice> securityDevices = securityCommonMapper.selectSecurityDeviceByIds(param);
            informationPlanDTO.setSecurityDevices(copySecurityEntity2VOList(securityDevices));
        }

        return informationPlanDTO;
    }

    @Override
    public Pagination<InformationPlanVO> getInformationPlanPage(InformationPlanParam param) {
        Pagination<InformationPlanVO> page = PagingUtils.convert(informationPlanMapper.getInformationPlanPage(PagingUtils.convert(param), param));
        List<Long> infoPlanIds = page.getRecords().stream().map(InformationPlanVO::getId).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(infoPlanIds)) {
            List<InformationDeviceCountDTO> infoPlanCountList = infoScreenScheduleAssocMapper.countByInfoPlanIds(infoPlanIds);
            page.getRecords().forEach(index -> {
                infoPlanCountList.forEach(infoPlanCountDto -> {
                    if (infoPlanCountDto.getInformationPlanId().equals(index.getId())) {
                        Integer countId = infoPlanCountDto.getDeviceTotal();
                        if (!ObjectUtils.isEmpty(countId)) {
                            index.setDeviceTotal(countId);
                        }
                    }
                });
            });
        }
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteInformationPlan(Long id) {
        InformationPlanEntity informationPlanEntity = informationPlanMapper.selectById(id);
        //删除关联表
        infoScreenScheduleAssocMapper.deleteByInformationPlanId(id);
        //删除主表
        DatabaseContextHolder.softDelete(this, id);
        //删除job
        if (!ObjectUtils.isEmpty(informationPlanEntity)) {
            if (!ObjectUtils.isEmpty(informationPlanEntity.getOpenJobTaskId())) {
                jobService.removeJob(getJobName(informationPlanEntity.getOpenJobTaskId()));
            }
            if (!ObjectUtils.isEmpty(informationPlanEntity.getCloseJobTaskId())) {
                jobService.removeJob(getJobName(informationPlanEntity.getCloseJobTaskId()));
            }
        }
    }

    @Override
    public void updateInformationPlanStatus(Long id, Integer isEnable) {
        //获取主表数据
        InformationPlanEntity informationPlanEntity = informationPlanMapper.selectById(id);
        informationPlanEntity.setIsEnable(isEnable);
        //修改主表启动状态
        informationPlanMapper.updateById(informationPlanEntity);
        //如果是在有效期内 则立即启动job
        startOnceJob(informationPlanEntity);
    }

    @Override
    public Pagination<SecurityDeviceInfoVO> getUnusedDevicePage(InformationPlanParam param) {
        return PagingUtils.convert(informationPlanMapper.getUnusedDevicePage(PagingUtils.convert(param), param));
    }

    /**
     * 如果是在有效期内 则立即启动job
     */
    private void startOnceJob(InformationPlanEntity informationPlanEntity) {
        //启动后根据时间判断是否启动job
        var now = LocalDateTime.now();

        // 检查计划开始时间是否早于当前时间 && 检查计划结束时间是否晚于当前时间
        if (informationPlanEntity.getPlanStartTime().isBefore(now) && informationPlanEntity.getPlanEndTime().isAfter(now)) {
            jobService.startJob(getJobName(informationPlanEntity.getOpenJobTaskId()));
            jobService.startJob(getJobName(informationPlanEntity.getCloseJobTaskId()));
        }
    }

    private void createJob(String taskId, InformationPlanEntity po, Integer screenControl, String jobName, String jobTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", po.getId());
        params.put("screenControl", screenControl);
        jobService.addJob(getJobName(taskId),
                          ApplicationContextHolder.getBean(InformationJob.class),
                          CALL_BACK_METHOD,
                          getCronExpression(jobTime, po.getExecutionCycle()),
                          jobName,
                          JsonUtils.serialize(params),
                          false);
    }

    private String getCronExpression(String planTime, String executionCycle) {

        // 分解计划开始时间和执行周期
        String[] timeParts = planTime.split(":");
        List<String> daysOfWeek = Arrays.stream(executionCycle.split(","))
                .toList();

        // 根据输入构建Cron表达式
        // 秒数默认为0
        return "0" + " " +
               // 分钟
               timeParts[1] + " " +
               // 小时
               timeParts[0] + " " +
               // 日
               "? " + " " + // 使用问号表示不关心具体的日期
               // 月份
               "* " + " " + // 每个月
               // 星期
               daysOfWeek.stream()
                       .map(day -> ObjectUtils.isEmpty(day) ? CronEnum.EVERY_DAY_OF_WEEK.getValue()
                                                            : CronEnum.getByName(day).getValue()) // 0 表示 Sunday
                       .collect(Collectors.joining(","));
    }

    private InformationPlanEntity copyAddInformationPlanEntity(InformationPlanDTO dto) {
        InformationPlanEntity entity = new InformationPlanEntity();
        entity.setId(dto.getId());
        entity.setPlanName(dto.getPlanName());
        entity.setPlanStartTime(dto.getPlanStartTime());
        entity.setPlanEndTime(dto.getPlanEndTime());
        entity.setOpenTime(dto.getOpenTime());
        entity.setCloseTime(dto.getCloseTime());
        entity.setIsEnable(dto.getIsEnable());
        entity.setExecutionCycle(dto.getExecutionCycle());
        return entity;
    }

    private InformationPlanEntity copyUpdateInformationPlanEntity(InformationPlanEntity entity, InformationPlanDTO dto) {
        entity.setPlanName(dto.getPlanName());
        entity.setPlanStartTime(dto.getPlanStartTime());
        entity.setPlanEndTime(dto.getPlanEndTime());
        entity.setOpenTime(dto.getOpenTime());
        entity.setCloseTime(dto.getCloseTime());
        entity.setIsEnable(dto.getIsEnable());
        entity.setExecutionCycle(dto.getExecutionCycle());
        return entity;
    }

    private InformationPlanDTO copyInformationPlanDTO(InformationPlanEntity entity) {
        InformationPlanDTO dto = new InformationPlanDTO();
        dto.setId(entity.getId());
        dto.setPlanName(entity.getPlanName());
        dto.setPlanStartTime(entity.getPlanStartTime());
        dto.setPlanEndTime(entity.getPlanEndTime());
        dto.setOpenTime(entity.getOpenTime());
        dto.setCloseTime(entity.getCloseTime());
        dto.setIsEnable(entity.getIsEnable());
        dto.setExecutionCycle(entity.getExecutionCycle());
        return dto;
    }

    private List<SecurityDeviceInfoVO> copySecurityEntity2VOList(List<SecurityDevice> securityDevices) {
        return securityDevices.stream().map(securityDevice -> {
            SecurityDeviceInfoVO entity = new SecurityDeviceInfoVO();
            entity.setId(securityDevice.getId());
            entity.setDeviceName(securityDevice.getDeviceName());
            entity.setDeviceId(securityDevice.getDeviceId());
            entity.setCode(securityDevice.getCode());
            entity.setApplicationCode(securityDevice.getApplicationCode());
            entity.setAreaId(securityDevice.getAreaId());
            entity.setAreaName(securityDevice.getAreaName());
            entity.setDeviceStatus(securityDevice.getDeviceStatus());
            entity.setCommunicationStatus(securityDevice.getCommunicationStatus());
            entity.setBrandCode(securityDevice.getBrandCode());
            entity.setBrandName(securityDevice.getBrandName());
            entity.setThirdPartyId(securityDevice.getThirdPartyId());
            entity.setLntAndLat(securityDevice.getLntAndLat());
            entity.setDeviceProdCategoryCode(securityDevice.getDeviceProdCategoryCode());
            entity.setDeviceProdCategoryCodeConcat(securityDevice.getDeviceProdCategoryCodeConcat());
            entity.setDeviceProdCategoryName(securityDevice.getDeviceProdCategoryName());
            entity.setDeviceProdId(securityDevice.getDeviceProdId());
            entity.setDeviceProdName(securityDevice.getDeviceProdName());
            return entity;
        }).collect(Collectors.toList());
    }

    private List<InfoScreenScheduleAssocEntity> copyInfoScreenScheduleAssoc(List<SecurityDeviceInfoVO> securityDeviceInfos,
                                                                            Long informationPlanId
                                                                           ) {
        return securityDeviceInfos.stream().map(securityDevice -> {
            InfoScreenScheduleAssocEntity entity = new InfoScreenScheduleAssocEntity();
            entity.setId(IdWorker.getId());
            entity.setInformationPlanId(informationPlanId);
            entity.setSecurityDeviceId(securityDevice.getId());
            entity.setTenant(SessionContextHolder.getRequester().getTenant());
            entity.setDeleted(Boolean.FALSE);
            EntityAuditor.populateCreateAudit(entity);
            EntityAuditor.populateUpdateAudit(entity);
            return entity;
        }).collect(Collectors.toList());
    }

    @Subscribe
    @AllowConcurrentEvents
    public void initRegistryJob(SysReadEvent event) {
        log.info("information plan init job");
        SessionContextHolder.clearSessionContext();
        var list = this.lambdaQuery().list();
        list.forEach(plan -> {
            SessionContextHolder.requestTenant(plan.getTenant());
            if (ObjectUtils.isEmpty(plan.getOpenJobTaskId())) {
                plan.setOpenJobTaskId(ObjectIdUtils.next());
            }
            if (ObjectUtils.isEmpty(plan.getCloseJobTaskId())) {
                plan.setCloseJobTaskId(ObjectIdUtils.next());
            }
            createJob(plan.getOpenJobTaskId(), plan, 1,
                      plan.getPlanName() + "_开屏", plan.getOpenTime());
            createJob(plan.getCloseJobTaskId(), plan, 0,
                      plan.getPlanName() + "_关屏", plan.getCloseTime());
            this.updateById(plan);
            startOnceJob(plan);
            SessionContextHolder.clearSessionContext();
        });
    }

    public void startPlan(String taskId) {
        jobService.startJob(getJobName(taskId));
    }

    public void stopPlan(String taskId) {
        jobService.stopJob(getJobName(taskId));
    }

    private String getJobName(String id) {
        return String.format("%s_%s", JOB_NAME, id);
    }

}
