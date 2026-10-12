package com.guhecloud.cube.srv.gen.product.market.server.pricing.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.product.market.server.common.service.MarketGoodsCategoryService;
import com.guhecloud.cube.srv.gen.product.market.server.common.vo.MarketGoodsCategoryVO;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.dao.GoodsPricingPlanDetailMapper;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.dao.GoodsPricingPlanMapper;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPricingPlan;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPricingPlanDetail;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.enumeration.PlanStatusEnum;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.enumeration.PublishTypeEnum;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.enumeration.UrgencyEnum;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.job.GoodsPricingTaskJob;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingPlanDetailParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingPlanParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingPlanQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.service.GoodsPricingPlanDetailService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.service.GoodsPricingPlanService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.CommonStatVO;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPricingPlanVO;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.UserVO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectIdUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.event.src.annotation.EventBusListen;
import com.guhecloud.platform.event.src.common.SysReadEvent;
import com.guhecloud.platform.job.jobtask.service.JobService;
import com.guhecloud.platform.job.xxl.operator.XxlJobApiOperator;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 描述：商品采价计划服务层
 *
 * @author Appet
 * @date 2023-05-16 15:08:36
 */
@Slf4j
@Service
@EventBusListen(order = 2)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsPricingPlanServiceImpl extends ServiceImpl<GoodsPricingPlanMapper, GoodsPricingPlan> implements GoodsPricingPlanService {
    private final GoodsPricingPlanDetailService goodsPricingPlanDetailService;
    private final GoodsPricingPlanDetailMapper goodsPricingPlanDetailMapper;
    private final MarketGoodsCategoryService marketGoodsCategoryService;
    private final UserClient userClient;
    private final JobService jobService;
    private static final String JOB_NAME = "GoodsPricingTaskJobHandler";
    private static final String CALL_BACK_METHOD = "execute";
    private static final String JOB_DESC = "商品采价";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(GoodsPricingPlanParam param) {
        GoodsPricingPlan goodsPricingPlan = ObjectUtils.copying(param, GoodsPricingPlan.class);
        //自定义ID
        Long id = IdWorker.getId();
        goodsPricingPlan.setId(id);

        //保存采集信息
        updateGoodsPricingPlanDetail(goodsPricingPlan.getId(), param);
        //创建定时任务
        createJobTask(goodsPricingPlan);

        //创建人
        goodsPricingPlan.setCreateUserName(SecurityContextHolder.getPrincipal().getName());
        this.baseMapper.insert(validAndSetPlanStatus(goodsPricingPlan));
        return goodsPricingPlan.getId();
    }

    /**
     * 校验计划的状态
     *
     * @param plan
     */
    private GoodsPricingPlan validAndSetPlanStatus(GoodsPricingPlan plan) {
        if (plan.getEndTime() != null && plan.getEndTime().isBefore(LocalDateTime.now())) {
            plan.setStatus(PlanStatusEnum.OVER.name());
        } else if (plan.getEnabled()) {
            plan.setStatus(PlanStatusEnum.EXEC.name());
        } else {
            plan.setStatus(PlanStatusEnum.STOP.name());
        }
        return plan;
    }

    private void updateGoodsPricingPlanDetail(Long id, GoodsPricingPlanParam param) {
        Assert.isParameterError(CollectionUtils.isEmpty(param.getCategoryIds()) ||
                                CollectionUtils.isEmpty(param.getUserIds()), "采集信息缺失：品类和采集员不能为空");
        //删除详情信息
        goodsPricingPlanDetailService.remove(Wrappers.<GoodsPricingPlanDetail>lambdaQuery()
                                                     .eq(GoodsPricingPlanDetail::getPlanId, id));
        //保存
        String categoryIds = param.getCategoryIds().stream().map(Object::toString).collect(Collectors.joining(","));
        String userIds = param.getUserIds().stream().map(Object::toString).collect(Collectors.joining(","));
        goodsPricingPlanDetailService.add(new GoodsPricingPlanDetailParam(id, categoryIds, userIds));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, GoodsPricingPlanParam param) {
        GoodsPricingPlan goodsPricingPlan = this.baseMapper.selectById(id);
        Assert.isNotFound(goodsPricingPlan, "未查询到相关信息");
        ObjectUtils.copying(param, goodsPricingPlan);
        //保存采集信息
        updateGoodsPricingPlanDetail(goodsPricingPlan.getId(), param);
        //创建定时任务
        updateJobTask(goodsPricingPlan);
        this.baseMapper.updateById(validAndSetPlanStatus(goodsPricingPlan));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        GoodsPricingPlan goodsPricingPlan = this.baseMapper.selectById(id);
        //删除定时任务
        if (StringUtils.isNotEmpty(goodsPricingPlan.getXxlTaskId())) {
            XxlJobApiOperator.removeJob(goodsPricingPlan.getXxlTaskId());
        }
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        ids.forEach(id -> DatabaseContextHolder.softDelete(this, id));
    }

    @Override
    public GoodsPricingPlanVO getById(Long id) {
        GoodsPricingPlan goodsPricingPlan = this.baseMapper.selectById(id);
        GoodsPricingPlanVO planDetail = ObjectUtils.copying(goodsPricingPlan, GoodsPricingPlanVO.class);
        //查询品类和人员
        List<GoodsPricingPlanDetail> goodsPricingPlanDetails = goodsPricingPlanDetailMapper.selectList(Wrappers
                                                                                                               .<GoodsPricingPlanDetail>lambdaQuery()
                                                                                                               .eq(GoodsPricingPlanDetail::getPlanId, id));
        if (CollectionUtils.isNotEmpty(goodsPricingPlanDetails)) {
            List<MarketGoodsCategoryVO> allCategories = new ArrayList<>();
            List<UserVO> allUsers = new ArrayList<>();
            goodsPricingPlanDetails.forEach(d -> {
                //查询品类信息
                allCategories.addAll(marketGoodsCategoryService.getListByIds(d.getGoodsCategoryIds()));

                List<UserVO> userList = Arrays.stream(d.getUserIds().split(",")).map(u -> {
                    UserDTO user = userClient.getUser(Long.valueOf(u));
                    if (user != null) {
                        return UserVO.builder().userId(user.getId())
                                .userName(user.getName())
                                .deptName(user.getOrganization() != null ? user.getOrganization().getName() : "无部门")
                                .build();
                    }
                    return null;
                }).filter(Objects::nonNull).toList();
                allUsers.addAll(userList);
            });
            planDetail.setGoodsCategoryList(allCategories);
            planDetail.setUserList(allUsers);
        }
        return planDetail;
    }

    @Override
    public Pagination<GoodsPricingPlanVO> getPage(GoodsPricingPlanQueryParam param) {
        Period period = param.getPeriod();
        //请自行添加查询条件
        Page<GoodsPricingPlan> page = this.lambdaQuery()
                .like(StringUtils.isNotEmpty(param.getName()), GoodsPricingPlan::getName, param.getName())
                .eq(StringUtils.isNotEmpty(param.getPublishType()), GoodsPricingPlan::getPublishType, param.getPublishType())
                .eq(StringUtils.isNotEmpty(param.getUrgency()), GoodsPricingPlan::getUrgency, param.getUrgency())
                .eq(StringUtils.isNotEmpty(param.getStatus()), GoodsPricingPlan::getStatus, param.getStatus())
                .between(period !=
                         null, GoodsPricingPlan::getCreateTime, ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                .orderByDesc(GoodsPricingPlan::getCreateTime)
                .page(PagingUtils.convert(param));
        Pagination<GoodsPricingPlanVO> convert = PagingUtils.convert(page, GoodsPricingPlanVO.class);
        if (CollectionUtils.isNotEmpty(convert.getRecords())) {
            convert.getRecords().forEach(this::extendInfo);
        }
        return convert;
    }

    private void extendInfo(GoodsPricingPlanVO vo) {
        vo.setPublishTypeName(PublishTypeEnum.valueOf(vo.getPublishType()).getLabel());
        vo.setUrgencyName(UrgencyEnum.valueOf(vo.getUrgency()).getLabel());
        //状态
        vo.setStatus(PlanStatusEnum.valueOf(vo.getStatus()).getLabel());
    }

    @Override
    public synchronized void startOrStopPlan(Long id, boolean enabled) {
        GoodsPricingPlan goodsPricingPlan = this.baseMapper.selectById(id);
        Assert.isNotFound(goodsPricingPlan, "未找到计划信息");
        goodsPricingPlan.setEnabled(enabled);
        if (enabled) {
            goodsPricingPlan.setStatus(PlanStatusEnum.EXEC.name());
        } else {
            goodsPricingPlan.setStatus(PlanStatusEnum.STOP.name());
        }
        //跟新数据
        this.updateById(goodsPricingPlan);
    }

    //创建定时任务
    private void createJobTask(GoodsPricingPlan plan) {
        if (StringUtils.isBlank(plan.getXxlTaskId())) {
            plan.setXxlTaskId(ObjectIdUtils.next());
        }
        if (plan.getEnabled()) {
            jobService.addJob(getJobName(plan.getXxlTaskId()),
                              ApplicationContextHolder.getBean(GoodsPricingTaskJob.class),
                              CALL_BACK_METHOD,
                              plan.getCron(),
                              JOB_DESC,
                              plan.getId().toString(),
                              true);
        }
    }

    //删除定时任务
    private void removeJobTask(String taskId) {
        if (StringUtils.isNotBlank(taskId)) {
            jobService.removeJob(getJobName(taskId));
        }
    }

    //更新定时任务
    private void updateJobTask(GoodsPricingPlan plan) {
        //先删除定时任务
        removeJobTask(plan.getXxlTaskId());
        //创建定时任务
        createJobTask(plan);
    }

    @Override
    public List<CommonStatVO> GoodsPricingPlanStatusStat() {
        List<CommonStatVO> statList = this.baseMapper.GoodsPricingPlanStatusStat();
        Map<String, String> statMap = Optional.ofNullable(statList).orElse(Collections.emptyList()).stream()
                .collect(Collectors.toMap(CommonStatVO::getCode, CommonStatVO::getValue));

        List<CommonStatVO> collect = Arrays.stream(PlanStatusEnum.values()).map(em -> {
            CommonStatVO st = new CommonStatVO();
            st.setCode(em.name());
            st.setName(em.getLabel());
            st.setValue(StringUtils.isNotEmpty(statMap.get(em.name())) ? statMap.get(em.name()) : "0");
            return st;
        }).collect(Collectors.toList());

        //统计总计 特殊处理
        int total = 0;
        if (CollectionUtils.isNotEmpty(statList)) {
            total = statList.stream().mapToInt(s -> Integer.parseInt(s.getValue())).sum();
        }
        CommonStatVO st = new CommonStatVO();
        st.setCode("ALL");
        st.setName("全部");
        st.setValue(String.valueOf(total));
        collect.add(st);

        return collect;
    }

    @Override
    public void removePlanJob(String jobId) {
        jobService.removeJob(getJobName(jobId));
    }

    @Subscribe
    @AllowConcurrentEvents
    public void initRegistryJob(SysReadEvent event) {
        log.info("goods pricing plan init job");
        SessionContextHolder.clearSessionContext();
        var list = this.lambdaQuery().list();
        for (GoodsPricingPlan plan : list) {
            SessionContextHolder.requestTenant(plan.getTenant());
            createJobTask(plan);
            this.updateById(plan);
            SessionContextHolder.clearSessionContext();
        }
    }

    private String getJobName(String id) {
        return String.format("%s_%s", JOB_NAME, id);
    }
}