package com.guhecloud.cube.srv.gen.disinfection.server.member.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.srv.gen.disinfection.server.member.constants.MemberJobConstants;
import com.guhecloud.cube.srv.gen.disinfection.server.member.dao.DisinfectionDeviceAutomaticMapper;
import com.guhecloud.cube.srv.gen.disinfection.server.member.entity.MemberDeviceAutomatic;
import com.guhecloud.cube.srv.gen.disinfection.server.member.job.DisinfectionDeviceAutomaticJob;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.DeviceAutomaticCommandParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.DisinfectionDeviceAutomaticQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionDeviceAutomaticService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.MemberDeviceAutomaticPageResult;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.MemberDevicePropDTO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectIdUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.event.src.annotation.EventBusListen;
import com.guhecloud.platform.event.src.common.SysReadEvent;
import com.guhecloud.platform.job.jobtask.cron.CronExpression;
import com.guhecloud.platform.job.jobtask.service.JobService;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@EventBusListen(order = 2)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DisinfectionDeviceAutomaticServiceImpl extends ServiceImpl<DisinfectionDeviceAutomaticMapper, MemberDeviceAutomatic> implements DisinfectionDeviceAutomaticService {

    private final JobService jobService;

    private final DeviceCenterStdClient deviceCenterStdClient;

    /**
     * 自动指令分页列表
     *
     * @param param 条件
     *
     * @return 分页列表
     */
    @Override
    public Pagination<MemberDeviceAutomaticPageResult> deviceAutomaticPage(DisinfectionDeviceAutomaticQueryParam param) {
        // 查询分页信息
        Page<MemberDeviceAutomatic> page = this.page(PagingUtils.convert(param), Wrappers.<MemberDeviceAutomatic>lambdaQuery()
                .eq(param.getGroupId() != null, MemberDeviceAutomatic::getGroupId, param.getGroupId())
                .like(StringUtils.isNotBlank(param.getName()), MemberDeviceAutomatic::getName, param.getName())
                .orderByAsc(MemberDeviceAutomatic::getCreateTime));
        var deviceMap = new HashMap<Long, DeviceBaseArchivesInfoDTO>();
        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            var deviceList = deviceCenterStdClient.getListByDeviceId(page.getRecords().stream().map(MemberDeviceAutomatic::getDeviceId)
                                                                             .collect(Collectors.toSet()));
            Optional.ofNullable(deviceList).orElse(CollectionUtils.emptyList()).forEach(dto -> {
                deviceMap.put(dto.getId(), dto);
            });
        }

        /* 处理属性列表*/
        List<MemberDeviceAutomaticPageResult> resultList = new ArrayList<>();
        page.getRecords().forEach(automatic -> {
            MemberDeviceAutomaticPageResult result = new MemberDeviceAutomaticPageResult();
            result.setId(automatic.getId());
            result.setGroupId(automatic.getGroupId());
            result.setName(automatic.getName());
            result.setDisplay(automatic.getDisplay());
            result.setDeviceId(automatic.getDeviceId());
            result.setLoopType(automatic.getLoopType());
            result.setLoopDay(automatic.getLoopDay());
            result.setLoopHour(automatic.getLoopHour());
            result.setPropWriteValueList(JsonUtils.deserialize(automatic.getPropWriteValueList(), new TypeReference<List<MemberDevicePropDTO>>() {}));
            result.setDeviceName(deviceMap.containsKey(automatic.getDeviceId()) ? deviceMap.get(automatic.getDeviceId()).getName() : "");
            resultList.add(result);
        });
        // 返回
        return PagingUtils.convert(page, resultList);
    }

    /**
     * 添加自动化指令
     *
     * @param param 入参
     *
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addDeviceAutomatic(DeviceAutomaticCommandParam param) {
        MemberDeviceAutomatic po = param.toMemberDeviceAutomatic();
        po.setTaskId(ObjectIdUtils.next());
        createJobInfoDTO(po);
        this.save(po);
        return true;
    }

    /**
     * 修改自动化指令
     *
     * @param param 入参
     *
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean editDeviceAutomatic(DeviceAutomaticCommandParam param) {
        var old = this.getById(param.getId());
        Assert.isNotFound(old);
        var newPo = param.toMemberDeviceAutomatic();
        // 修改原来的job
        var corn = CronExpression.explainCronExpression(newPo.getLoopType(), newPo.getLoopDay(), newPo.getLoopHour());
        jobService.updateJob(getJobName(old.getTaskId()), corn, param.getName(), String.valueOf(old.getId()));
        old.setDeviceId(newPo.getDeviceId());
        old.setDisplay(newPo.getDisplay());
        old.setDeviceId(newPo.getDeviceId());
        old.setName(newPo.getName());
        old.setLoopType(newPo.getLoopType());
        old.setLoopDay(newPo.getLoopDay());
        old.setLoopHour(newPo.getLoopHour());
        old.setPropWriteValueList(newPo.getPropWriteValueList());
        old.setUpdateTime(LocalDateTime.now());
        old.setUpdateBy(SecurityContextHolder.getPrincipal().getAccount());
        this.updateById(old);
        return true;
    }

    private String getJobName(String taskId) {
        return String.format("%s_%s", MemberJobConstants.AUTOMATIC_JOB_HANDLER, taskId);
    }

    /**
     * 移除指令
     *
     * @param id 主键
     *
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeDeviceAutomatic(Long id) {
        MemberDeviceAutomatic po = this.getById(id);
        jobService.removeJob(getJobName(po.getTaskId()));
        this.removeById(id);
        return true;
    }

    @Subscribe
    @AllowConcurrentEvents
    public void initRegistryJob(SysReadEvent event) {
        SessionContextHolder.clearSessionContext();
        var deviceAutomatics = this.list();
        deviceAutomatics.forEach(automatic -> {
            SessionContextHolder.requestTenant(automatic.getTenant());
            if (StringUtils.isBlank(automatic.getTaskId())) {
                automatic.setTaskId(ObjectIdUtils.next());
            }
            createJobInfoDTO(automatic);
            this.updateById(automatic);
            SessionContextHolder.clearSessionContext();
        });
    }

    private void createJobInfoDTO(MemberDeviceAutomatic po) {
        jobService.addJob(getJobName(po.getTaskId()),
                          ApplicationContextHolder.getBean(DisinfectionDeviceAutomaticJob.class),
                          "memberDeviceAutomatic",
                          CronExpression.explainCronExpression(po.getLoopType(), po.getLoopDay(), po.getLoopHour()),
                          po.getName(),
                          po.getId().toString(),
                          true
                         );
    }
}
