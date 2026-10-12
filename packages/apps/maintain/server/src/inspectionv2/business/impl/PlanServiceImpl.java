package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.guhecloud.cube.srv.gen.loose.works.client.ProjectScheduleClient;
import com.guhecloud.cube.srv.gen.loose.works.dto.enumeration.ProjectStateEnum;
import com.guhecloud.cube.srv.gen.loose.works.param.ProjectScheduleQueryParam;
import com.guhecloud.cube.srv.gen.loose.works.vo.ProjectScheduleVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.HandlerService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.PlanRelationService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.PlanService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectCronQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectTypeQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.impl.InspectQuery;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectCronMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.PlanMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectPlanExecStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTargetCategoryEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.filter.BusinessTypeInterceptor;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.job.InspectTaskJob;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.InspectTargetInfoDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.PlanAddParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.PlanDetailParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.PlanPageParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.PlanUpdateParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectCronPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectTypePO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.PlanPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.PlanRelationPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.PlanDetailResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.StandardResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionStandardVO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.BeanUtils;
import com.guhecloud.platform.base.util.BizCodeUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.event.src.annotation.EventBusListen;
import com.guhecloud.platform.event.src.common.SysReadEvent;
import com.guhecloud.platform.job.jobtask.cron.CronExpression;
import com.guhecloud.platform.job.jobtask.service.JobService;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 巡检计划
 */
@Slf4j
@Service
@EventBusListen(order = 2)
public class PlanServiceImpl extends ServiceImpl<PlanMapper, PlanPO> implements PlanService {

    private static final String JOB_NAME = "InspectionTaskJobHandler";
    private static final String CALL_BACK_METHOD = "inspectionTaskJobHandler";
    @Resource
    private InspectQuery inspectQuery;
    @Resource
    private PlanRelationService planRelationService;
    @Resource
    private HandlerService handlerService;
    @Resource
    private InspectCronQueryService cronQueryService;
    @Resource
    private JobService jobService;
    @Resource
    @Lazy
    private InspectTaskJob inspectTaskJob;
    @Resource
    private ProjectScheduleClient projectScheduleClient;
    @Resource
    private InspectTypeQueryService inspectTypeQueryService;
    @Resource
    private InspectCronMapper inspectCronMapper;

    /**
     * 分页
     *
     * @param param
     *
     * @return
     */
    @Override
    public Pagination<PlanDetailResult> getPage(PlanPageParam param) {

        Page<PlanPO> page = this.lambdaQuery()
                .select(PlanPO::getId, PlanPO::getName, PlanPO::getTypeId, PlanPO::getTypeName, PlanPO::getReceiveType, PlanPO::getReceiveIds, PlanPO::getReceiveDeptIds, PlanPO::getReceivePostIds, PlanPO::getReceiveRoleIds, PlanPO::getTargetNum, PlanPO::getValidBeginTime, PlanPO::getValidEndTime, PlanPO::getCycle, PlanPO::getStatus, PlanPO::getCreateBy, PlanPO::getCreateTime, PlanPO::getEnable, PlanPO::getTenant)
                .like(StringUtils.isNotEmpty(param.getKeywords()), PlanPO::getName, param.getKeywords())
                .eq(param.getTypeId() != null, PlanPO::getTypeId, param.getTypeId())
                .eq(param.getStatus() != null, PlanPO::getStatus, param.getStatus())
                .orderByAsc(PlanPO::getStatus)
                .orderByDesc(PlanPO::getCreateTime)
                .page(PagingUtils.convert(param));

        Pattern pattern = Pattern.compile("[0-9]*");
        List<PlanDetailResult> vos = Optional.ofNullable(page.getRecords()).orElse(Collections.emptyList())
                .stream().map(po -> {
                    PlanDetailResult vo = ObjectUtils.copying(po, PlanDetailResult.class);
                    vo.setReceiveNames(inspectQuery.extendUserNames(vo.getTenant(), vo.getReceiveIds()));
                    vo.setReceiveDeptNames(inspectQuery.extendDeptNames(vo.getTenant(), vo.getReceiveDeptIds()));
                    vo.setReceivePostNames(inspectQuery.extendPostNames(vo.getTenant(), vo.getReceivePostIds()));
                    vo.setReceiveRoleNames(inspectQuery.extendRoleNames(vo.getTenant(), vo.getReceiveRoleIds()));
                    // 设置创建人名称
                    if (StringUtils.isNotEmpty(vo.getCreateBy()) && pattern.matcher(vo.getCreateBy()).matches()) {
                        if (StringUtils.isNotBlank(vo.getCreatorId()) && ActorTypeEnum.USER.name().equals(vo.getCreatorType())) {
                            vo.setCreateByName(handlerService.getUserNameById(Long.valueOf(vo.getCreatorId())));
                        } else {
                            vo.setCreateByName(handlerService.getUserNameByName(vo.getCreateBy()));
                        }
                    }
                    return vo;
                }).collect(Collectors.toList());
        return PagingUtils.convert(page, vos);
    }

    /**
     * 巡检计划详情
     *
     * @param param
     *
     * @return
     */
    @Override
    public PlanDetailResult getDetail(PlanDetailParam param) {
        Assert.isParameterError(param.getId() == null, "id不能为空");
        PlanDetailResult planResult = this.getDetailOnly(param.getId());
        if (!Boolean.TRUE.equals(param.getNotTarget())) {
            Pagination<List<InspectTargetInfoDTO>> pagination = planRelationService.getTargetInfoByPlanId(param);
            planResult.setTotal(pagination.getTotal());
            planResult.setTypeTargets(pagination.getRecords());
        }
        return planResult;
    }

    /**
     * 巡检计划-详情-对象的巡检标准列表
     *
     * @param plainId
     * @param targetId
     *
     * @return
     */
    @Override
    public List<StandardResult> getDeailStandardList(Long plainId, Long targetId) {
        return planRelationService.getStandardList(plainId, targetId);
    }

    /**
     * 新增/编辑巡检计划
     *
     * @param param 巡检计划
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveOrUpdatePlan(PlanAddParam param) {
        PlanPO po = saveOrUpdatePlanOnly(param);
        Long planId = po.getId();
        Integer tenant = po.getTenant();
        param.getPlanRelationList().forEach(t -> {
            t.setPlanId(planId);
            t.setId(IdWorker.getId());
            t.setTenant(tenant);
            t.setDeleted(false);
            EntityAuditor.populateCreateAudit(t);
        });
        // 保存计划关联关系
        planRelationService.remove(Wrappers.<PlanRelationPO>lambdaQuery()
                                           .eq(PlanRelationPO::getPlanId, planId)
                                  );
        planRelationService.saveBatch(param.getPlanRelationList());
        this.startPlan(po);
        return po.getId();
    }

    @Override
    public Long editPlanPrecise(@RequestBody @Validated PlanUpdateParam param) {
        // 编辑计划主体
        PlanPO planPO = saveOrUpdatePlanOnly(param);
        // 初始化删除的集合
        List<Long> deleteList = param.getDeleteList();
        if (deleteList == null) {
            deleteList = new ArrayList<>();
        }

        List<PlanRelationPO> paramRelationList = param.getPlanRelationList();
        if (CollectionUtils.isNotEmpty(paramRelationList)) {
            var nowTime = LocalDateTime.now();
            Long planId = planPO.getId();
            Integer tenant = planPO.getTenant();
            List<PlanRelationPO> saveOrUpdateList = new ArrayList<>();
            for (PlanRelationPO planRelationPO : paramRelationList) {
                deleteList.add(planRelationPO.getId());
                if (planRelationPO.getPlanId() == null) {
                    planRelationPO.setPlanId(planId);
                    planRelationPO.setId(IdWorker.getId());
                    planRelationPO.setTenant(tenant);
                    planRelationPO.setDeleted(false);
                    EntityAuditor.populateCreateAudit(planRelationPO);
                }
                saveOrUpdateList.add(planRelationPO);
            }
            // 保存计划关联关系
            planRelationService.remove(Wrappers.<PlanRelationPO>lambdaQuery()
                                               .eq(PlanRelationPO::getPlanId, planId)
                                      );
            planRelationService.saveBatch(saveOrUpdateList);
        }

        this.startPlan(planPO);
        return planPO.getId();
    }

    private PlanPO saveOrUpdatePlanOnly(PlanAddParam param) {
        Assert.isNotFound(param);
        Assert.isNotFound(param.getPlanRelationList(), "巡检对象不能为空！");
        PlanPO po = new PlanPO();
        if (param.getId() != null) {
            po = this.getById(param.getId());
            Assert.isNotFound(po == null, "计划不存在，请检查是否被删除！");
            PlanPO oldNamePo = this.getOne(Wrappers.<PlanPO>lambdaQuery()
                                                   .eq(PlanPO::getName, param.getName())
                                                   .ne(PlanPO::getId, param.getId())
                                          );
            Assert.isConflict(oldNamePo != null, "计划名称已存在！");
        } else {
            po.setId(IdWorker.getId());
        }
        //巡检对象校验
        checkInspectTarget(param.getPlanRelationList());
        BeanUtils.copyProperties(param, po);
        // 校验入参
        verifyPlanPO(po);
        // 设置巡检周期定时任务表达式
        this.setCron(param, po);
        if (StringUtils.isBlank(po.getCode())) {
            po.setCode(BizCodeUtils.generateNoRandom("JH"));
        }
        po.setStatus(Boolean.TRUE.equals(po.getEnable()) ? InspectPlanExecStatusEnum.EXECUTING : InspectPlanExecStatusEnum.STOP);

        // 去重的对象集合
        List<PlanRelationPO> distinctTargetList = Optional.ofNullable(param.getPlanRelationList()).orElse(Collections.emptyList())
                .parallelStream().collect(
                        Collectors.collectingAndThen(
                                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(PlanRelationPO::getTargetId))), ArrayList::new)
                                         );

        po.setTargetNum(distinctTargetList.size());
        po.setLimitTotalMinute(handlerService.getLimitTotalMinute(po.getLimitTimeJson()));
        po.setCreatorId(SecurityContextHolder.getPrincipal().getId().toString());
        if (Objects.nonNull(param.getTypeId())) {
            InspectTypePO inspectTypePO = inspectTypeQueryService.getById(param.getTypeId());
            po.setTypeName(Objects.nonNull(inspectTypePO) ? inspectTypePO.getName() : null);
        }
        po.setBusinessType(BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.get());
        // 保存计划
        this.saveOrUpdate(po);
        return po;
    }

    private void checkInspectTarget(List<PlanRelationPO> planRelationList) {
        //小散工程校验
        List<Long> projectScheduleIdList = planRelationList.stream()
                .filter(t -> InspectTargetCategoryEnum.SMALL_ENGINEERING.equals(t.getTargetCategory()))
                .map(PlanRelationPO::getTargetId).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(projectScheduleIdList)) {
            ProjectScheduleQueryParam param = new ProjectScheduleQueryParam();
            param.setProjectIds(projectScheduleIdList);
            List<ProjectScheduleVO> underwayList = projectScheduleClient.projectScheduleBatchPost(param)
                    .stream().filter(t -> ProjectStateEnum.UNDERWAY.name().equals(t.getStatus()))
                    .toList();
            Assert.isNotAcceptable(underwayList.size() != projectScheduleIdList.size(), "巡检对象中存在非进行中的工程");
        }
    }

    /**
     * @Author wql
     * @Description 根据巡检计划id, 对象id获取对象关联的巡检标准
     * @Date 14:03 2023/4/21
     **/
    @Override
    public List<InspectionStandardVO> getStandardList(Long planId, Long objId) {
        return planRelationService.getObjStandardList(planId, objId);
    }

    @Override
    public PlanRelationPO getObjInfo(Long planId, Long objId) {
        return planRelationService.getObjInfo(planId, objId);
    }

    /**
     * @Author wql
     * @Description 检查计划名称是否重复
     * @Date 19:51 2023/4/27
     **/
    @Override
    public Boolean checkPlanNameIsRepeat(String planName) {
        return this.lambdaQuery().eq(PlanPO::getName, planName).count() > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBatchPlanRelation(List<PlanRelationPO> taskTargetDTOList) {
        planRelationService.saveBatch(taskTargetDTOList);
    }

    /**
     * 获取巡检计划详情(仅计划,无巡检对象和内容)
     *
     * @param id
     *
     * @return
     */
    @Override
    public PlanDetailResult getDetailOnly(Long id) {
        // 查询计划信息
        PlanPO po = this.getById(id);
        Assert.isNotFound(po, "未找到指定的巡检计划");

        //通过计划ID 查询计划关联的对象 也就是巡检点
        PlanDetailResult planResult = ObjectUtils.copying(po, PlanDetailResult.class);
        planResult.setLevelName(Objects.nonNull(po.getLevel()) ? po.getLevel().name() : null);
        planResult.setReceiveNames(inspectQuery.extendUserNames(planResult.getTenant(), planResult.getReceiveIds()));
        planResult.setReceiveDeptNames(inspectQuery.extendDeptNames(planResult.getTenant(), planResult.getReceiveDeptIds()));
        planResult.setReceivePostNames(inspectQuery.extendPostNames(planResult.getTenant(), planResult.getReceivePostIds()));
        planResult.setReceiveRoleNames(inspectQuery.extendRoleNames(planResult.getTenant(), planResult.getReceiveRoleIds()));
        planResult.setCheckDeptNames(inspectQuery.extendDeptNames(planResult.getTenant(), planResult.getCheckDeptIds()));
        planResult.setCheckPostNames(inspectQuery.extendPostNames(planResult.getTenant(), planResult.getCheckPostIds()));
        planResult.setCheckRoleNames(inspectQuery.extendRoleNames(planResult.getTenant(), planResult.getCheckRoleIds()));
        if (po.getCronId() != null) {
            var cron = inspectCronMapper.selectById(po.getCronId());
            planResult.setCronName(Optional.ofNullable(cron).orElse(new InspectCronPO()).getName());
        }
        // 设置检查人名称
        if ("2".equals(po.getCheckUserType())) {
            planResult.setCheckUserNames(handlerService.handlerDeptIdsAndPostIdsToNames(po.getCheckDeptIds(), po.getCheckPostIds()));
        } else {
            planResult.setCheckUserNames(handlerService.handlerUserIdsToNames(planResult.getCheckUserIds()));
        }

        return planResult;
    }

    /**
     * 获取巡检对象和巡检内容
     *
     * @param param planId
     *
     * @return
     */
    @Override
    public Pagination<InspectTargetInfoDTO> getPlanTargetPage(PlanPageParam param) {
        return planRelationService.selectPlanTargetPage(param);
    }

    @Override
    public void startPlanJob(String jobId) {
        jobService.startJob(getJobName(jobId));
    }

    @Override
    public void stopPlanJob(String jobId) {
        jobService.stopJob(getJobName(jobId));
    }

    @Override
    public void removePlanJob(String jobId) {
        jobService.removeJob(getJobName(jobId));
    }

    /**
     * 验证新增计划入参
     *
     * @param po
     */
    private void verifyPlanPO(PlanPO po) {
        //接收类型为部门时,需要检查该部门下是否有人员
        if ("2".equals(po.getReceiveType())) {
            String userIds = handlerService.HandlerDeptIdsAndPostIdsToUserIds(po.getReceiveDeptIds(), po.getReceivePostIds(), po.getReceiveRoleIds());
            Assert.isNotAcceptable(StringUtils.isEmpty(userIds), "巡检计划选择的无接收人员");
        }
        if (po.isCheck() && "2".equals(po.getCheckUserType())) {
            String userIds = handlerService.HandlerDeptIdsAndPostIdsToUserIds(po.getCheckDeptIds(), po.getCheckPostIds(), po.getCheckRoleIds());
            Assert.isNotAcceptable(StringUtils.isEmpty(userIds), "巡检计划选择的无稽查人员");
        }
    }

    /**
     * 设置定时任务表达式
     *
     * @param param
     * @param po
     */
    private void setCron(PlanAddParam param, PlanPO po) {
        if (po == null || !po.getCycle()) {
            return;
        }
        if (po.getCronId() != null) {
            InspectCronPO cronPO = cronQueryService.getById(po.getCronId());
            if (cronPO != null) {
                po.setCronExpression(cronPO.getCron());
            }
        } else {
            //根据据前端选择生成cron表达式
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("cycleType", param.getCycleType());
            jsonObject.put("selection", param.getSelection());
            jsonObject.put("hour", param.getHour());
            jsonObject.put("type", "custom");
            po.setCronName(jsonObject.toJSONString());
            po.setCronExpression(CronExpression.explainCronExpression(param.getCycleType(), param.getSelection(), param.getHour()));
        }
    }

    private void startPlan(PlanPO po) {
        Assert.isNotFound(po, "计划不存在");
        //如果已经创建过了，先删除之前的任务
        jobService.removeJob(getJobName(po.getId().toString()));
        var date = po.getValidBeginTime();
        Assert.isParameterError(ObjectUtils.isEmpty(date), "计划生效时间不能为空");
        if (!po.getCycle() && DateUtils.compare(date, DateUtils.offset(LocalDateTime.now(), ChronoUnit.SECONDS, 10)) < 0) {
            //如果是单次任务且生效时间小于当前10秒后的时间立即生成任务
            inspectTaskJob.inspectionTaskJobHandler(String.valueOf(po.getId()));
        } else {
            this.remoteCreateJob(po);
            // 如果是启用就开始任务
            if (po.getEnable()) {
                jobService.startJob(getJobName(po.getId().toString()));
            }
        }
        this.updateById(po);
    }

    private void remoteCreateJob(PlanPO po) {
        jobService.addJob(getJobName(po.getId().toString()),
                          ApplicationContextHolder.getBean(InspectTaskJob.class),
                          CALL_BACK_METHOD,
                          getCron(po),
                          po.getName(),
                          po.getId().toString(),
                          false);
    }

    private String getCron(PlanPO po) {
        var cron = "";
        if (po.getCycle()) {
            cron = po.getCronExpression();
        } else {
            cron = getCron(po.getValidBeginTime());
        }
        return cron;
    }

    /**
     * 通过输入指定日期时间生成cron表达式
     *
     * @param date 指定日期
     *
     * @return cron表达式
     */
    public String getCron(LocalDateTime date) {
        return DateUtils.format(date, "ss mm HH dd MM ? yyyy");
    }

    @Subscribe
    @AllowConcurrentEvents
    public void initRegistryJob(SysReadEvent event) {
        log.info("collect configure plan init job");
        SessionContextHolder.clearSessionContext();
        var list = this.lambdaQuery()
                .eq(PlanPO::getStatus, InspectPlanExecStatusEnum.EXECUTING)
                .eq(PlanPO::getEnable, true)
                .list();
        for (PlanPO item : list) {
            SessionContextHolder.requestTenant(item.getTenant());
            remoteCreateJob(item);
            jobService.startJob(getJobName(item.getId().toString()));
            SessionContextHolder.clearSessionContext();
        }
    }

    private String getJobName(String id) {
        return String.format("%s_%s", JOB_NAME, id);
    }
}
