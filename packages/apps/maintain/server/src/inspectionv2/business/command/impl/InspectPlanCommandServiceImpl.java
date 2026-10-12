package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.HandlerService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.PlanRelationService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectPlanCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectPlanTargetReferCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectTaskCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectCronQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectTaskQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectPlanMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectPlanExecStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.job.InspectTaskJob;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectPlanCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectResultCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.PlanAddParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectCronPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectPlanPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectTaskPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.PlanRelationPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.validator.InspectPlanValidator;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.BizCodeUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.job.jobtask.service.JobService;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InspectPlanCommandServiceImpl extends ServiceImpl<InspectPlanMapper, InspectPlanPO> implements InspectPlanCommandService {
    @Resource
    private InspectPlanValidator planValidator;
    @Resource
    private InspectPlanTargetReferCommandService targetReferCommandService;
    @Resource
    private InspectTaskCommandService taskCommandService;
    @Resource
    private InspectTaskQueryService taskQueryService;
    @Resource
    private InspectCronQueryService cronQueryService;
    @Resource
    private HandlerService handlerService;
    @Resource
    private PlanRelationService planRelationService;
    @Resource
    private JobService jobService;
    private static final String JOB_NAME = "InspectionTaskJobHandler";
    private static final String CALL_BACK_METHOD = "inspectionTaskJobHandler";

    @Override
    public Long save(InspectPlanCommandModel commandModel) {
        InspectPlanPO po = ObjectUtils.copying(commandModel, InspectPlanPO.class);
        planValidator.verifyIsExistByName(commandModel.getName());
        planValidator.verifyCron(commandModel.getCycle(), commandModel.getCronId());

        //查询 巡检周期
        this.extendCron(po);

        po.setCode(BizCodeUtils.generateNoRandom("JH"));
        po.setStatus(po.getCycle() ? InspectPlanExecStatusEnum.EXECUTING : InspectPlanExecStatusEnum.OVER);
        if (!po.getEnable()) {
            po.setStatus(InspectPlanExecStatusEnum.STOP);
        }
        po.setTargetNum(commandModel.getPlanTargets().size());
        po.setLimitTotalMinute(this.getLimitTotalMinute(po.getLimitTimeJson()));
        po.setCreatorId(SecurityContextHolder.getPrincipal().getId().toString());
        this.save(po);

        //创建任务与对象的关联
        targetReferCommandService.saveOrUpdate(po.getId(), commandModel.getPlanTargets());

        // 处理 单次任务和循环任务
        this.createJobOrRecord(po);
        return po.getId();
    }

    /**
     * 新增巡检计划
     *
     * @param param
     */
    @Override
    @Transactional
    public Long addPlan(PlanAddParam param) {
        Assert.isNotFound(param);
        Assert.isNotFound(param.getPlanRelationList());
        InspectPlanPO po = ObjectUtils.copying(param, InspectPlanPO.class);
        // 设置巡检周期定时任务表达式
        po.setId(IdWorker.getId());
        this.extendCron(po);

        po.setCode(BizCodeUtils.generateNoRandom("JH"));
        po.setStatus(InspectPlanExecStatusEnum.EXECUTING);

        // 去重的对象集合
        List<PlanRelationPO> distinctTargetList = Optional.ofNullable(param.getPlanRelationList()).orElse(Collections.emptyList())
                .parallelStream().collect(
                        Collectors.collectingAndThen(
                                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(PlanRelationPO::getTargetId))), ArrayList::new)
                                         );

        po.setTargetNum(distinctTargetList.size());
        po.setLimitTotalMinute(handlerService.getLimitTotalMinute(po.getLimitTimeJson()));
        po.setCreatorId(SecurityContextHolder.getPrincipal().getId().toString());
        // 保存计划
        this.save(po);

        // 设置关联关系中的计划id
        Long planId = po.getId();
        param.getPlanRelationList().forEach(t -> t.setPlanId(planId));
        // 保存计划关联关系
        planRelationService.saveBatch(param.getPlanRelationList());

        if (po.getCycle()) {
            // 循环任务需要创建定时任务，在定时任务中检查是否到时间到了就创建
            this.startPlan(po);
        } else {
            // 单次任务直接创建生效时间对应的任务
            taskCommandService.createTaskByPlan(po, param.getPlanRelationList());
        }
        return po.getId();
    }

    @Override
    public void update(InspectResultCommandModel commandModel) {

    }

    @Override
    public void remove(Long id) {
        InspectPlanPO task = this.getById(id);
        jobService.removeJob(getJobName(task.getId().toString()));
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    public void removeBatch(String ids) {
        if (ids.contains(",")) {
            for (String id : ids.split(",")) {
                this.remove(Long.valueOf(id));
            }
        } else {
            this.remove(Long.valueOf(ids));
        }
    }

    @Override
    public void removeAll(String ids) {
        if (StringUtils.isNotEmpty(ids) && ids.contains(",")) {
            for (String id : ids.split(",")) {
                //TODO this.remove(Long.valueOf(id));
            }
        }
    }

    /**
     * 开启xxl任务
     */
    @Override
    public void startPlan(Long id) {
        InspectPlanPO po = this.getById(id);
        Assert.isNotFound(po, "计划不存在");
        po.setEnable(true);
        po.setStatus(InspectPlanExecStatusEnum.EXECUTING);
        this.startPlan(po);
    }

    /**
     * 开启xxl任务
     */
    @Override
    public void startPlan(InspectPlanPO po) {
        Assert.isNotFound(po, "计划不存在");
        //如果已经创建过了，先删除之前的任务
        jobService.removeJob(getJobName(po.getId().toString()));
        if (po.getCycle()) {
            this.remoteCreateJob(po);
            if (po.getEnable()) {
                jobService.startJob(getJobName(po.getId().toString()));
            }
        } else {
            //单次计划判断该计划是否已生成任务,未生成任务时,且计划未失效时创建任务
            List<InspectTaskPO> list = taskQueryService.lambdaQuery().eq(InspectTaskPO::getPlanId, po.getId()).list();
            if (ObjectUtils.isEmpty(list) && DateUtils.compare(po.getEndTime(), LocalDateTime.now()) > 0) {
                taskCommandService.createTaskByPlan(po);
            }
        }
        this.updateById(po);
    }

    /**
     * 停止xxl任务
     */
    @Override
    public void stopPlan(Long id) {
        InspectPlanPO po = this.getById(id);
        Assert.isNotFound(po, "计划不存在");
        po.setEnable(false);
        po.setStatus(InspectPlanExecStatusEnum.STOP);
        jobService.removeJob(getJobName(po.getId().toString()));
        po.setXxlTaskId(null);
        this.updateById(po);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void overPlanBatch(List<Long> planIdList) {
        List<InspectPlanPO> inspectPlanPOS = this.lambdaQuery().in(InspectPlanPO::getId, planIdList)
                .list();
        if (CollectionUtils.isEmpty(inspectPlanPOS)) {
            return;
        }
        for (InspectPlanPO inspectPlanPO : inspectPlanPOS) {
            jobService.removeJob(getJobName(inspectPlanPO.getId().toString()));
            inspectPlanPO.setXxlTaskId(null);
            inspectPlanPO.setStatus(InspectPlanExecStatusEnum.OVER);
        }
        this.updateBatchById(inspectPlanPOS);
    }

    /**
     * 结束xxl任务
     */
    @Override
    public void overPlan(Long id) {
        InspectPlanPO po = this.getById(id);
        Assert.isNotFound(po, "计划不存在");
        jobService.removeJob(getJobName(po.getId().toString()));
        po.setStatus(InspectPlanExecStatusEnum.OVER);
        po.setXxlTaskId(null);
        this.updateById(po);
    }

    //处理任务的后续动作，单次任务 立即添加巡检记录
    //循环任务 创建定时任务
    private void createJobOrRecord(InspectPlanPO po) {
        if (po.getCycle()) {//循环任务
            this.startPlan(po);
        } else {
            // 单次任务
            taskCommandService.saveByPlanPO(po);
        }
    }

    //创建JOB
    private void remoteCreateJob(InspectPlanPO po) {
        //添加任务接口获取到任务id
        jobService.addJob(getJobName(po.getId().toString()),
                          ApplicationContextHolder.getBean(InspectTaskJob.class),
                          CALL_BACK_METHOD,
                          po.getCronExpression(),
                          po.getName(),
                          po.getId().toString(),
                          false);
    }

    private Integer getLimitTotalMinute(String taskTimeLimit) {
        if (StringUtils.isNotEmpty(taskTimeLimit)) {
            JSONObject jsonObject = JSONObject.parseObject(taskTimeLimit);
            int day = StringUtils.isNotEmpty(jsonObject.get("day").toString()) ? Integer.parseInt(jsonObject.get("day").toString()) : 0;
            int hour = StringUtils.isNotEmpty(jsonObject.get("hour").toString()) ? Integer.parseInt(jsonObject.get("hour").toString()) : 0;
            int minute =
                    StringUtils.isNotEmpty(jsonObject.get("minute").toString()) ? Integer.parseInt(jsonObject.get("minute").toString()) : 0;
            return (day * 24 * 60) + (hour * 60) + minute;
        } else {
            return null;
        }
    }

    private void extendCron(InspectPlanPO po) {
        if (po != null && po.getCronId() != null) {
            InspectCronPO cronPO = cronQueryService.getById(po.getCronId());
            if (cronPO != null) {
                po.setCronName(cronPO.getName());
                po.setCronExpression(cronPO.getCron());
            }
        }
    }

    private String getJobName(String id) {
        return String.format("%s_%s", JOB_NAME, id);
    }
}
