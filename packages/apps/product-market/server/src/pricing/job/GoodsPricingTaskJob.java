package com.guhecloud.cube.srv.gen.product.market.server.pricing.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.dao.GoodsPricingPlanDetailMapper;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.dao.GoodsPricingPlanMapper;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.dao.GoodsPricingTaskMapper;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPricingPlan;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPricingPlanDetail;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPricingTask;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPricingTaskDetail;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.enumeration.PlanStatusEnum;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.enumeration.TaskStatusEnum;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.service.GoodsPricingPlanService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.service.GoodsPricingTaskDetailService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.xxl.job.core.biz.model.ReturnT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 采价任务生成
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsPricingTaskJob {
    private final GoodsPricingPlanMapper goodsPricingPlanMapper;
    private final GoodsPricingPlanDetailMapper goodsPricingPlanDetailMapper;
    private final GoodsPricingTaskMapper goodsPricingTaskMapper;
    private final GoodsPricingTaskDetailService goodsPricingTaskDetailService;
    private final GoodsPricingPlanService goodsPricingPlanService;

    /**
     * 采价任务生成
     *
     * @param param 计划id
     *
     * @return 结果
     */
    public ReturnT<String> execute(String param) {
        long current = System.currentTimeMillis();
        Long planId = Long.valueOf(param);
        GoodsPricingPlan goodsPricingPlan = goodsPricingPlanMapper.selectById(planId);
        //设置tenant
        SessionContextHolder.requestTenant(goodsPricingPlan.getTenant());
        var nowTime = LocalDateTime.now();
        //检查定时任务是否结束 结束时间为空表明计划不会过期
        if ((goodsPricingPlan.getStartTime() == null || current >= DateUtils.toTimestamp(goodsPricingPlan.getStartTime())) &&
            (goodsPricingPlan.getEndTime() == null ||
             current <= DateUtils.toTimestamp(goodsPricingPlan.getEndTime()) && goodsPricingPlan.getEnabled())) {
            List<GoodsPricingPlanDetail> goodsPricingPlanDetails = goodsPricingPlanDetailMapper.selectList(Wrappers
                                                                                                                   .<GoodsPricingPlanDetail>lambdaQuery()
                                                                                                                   .eq(GoodsPricingPlanDetail::getPlanId, goodsPricingPlan.getId()));
            //创建任务
            GoodsPricingTask task = new GoodsPricingTask();
            task.setPlanId(goodsPricingPlan.getId());
            task.setName(goodsPricingPlan.getName());
            task.setCron(goodsPricingPlan.getCron());
            task.setUrgency(goodsPricingPlan.getUrgency());
            task.setDeadline(goodsPricingPlan.getTaskValidityTime(nowTime));
            task.setRemark(goodsPricingPlan.getRemark());
            task.setTaskDesc(goodsPricingPlan.getPlanDesc());
            task.setStatus(TaskStatusEnum.WAIT.name());//待接收
            task.setCreateUserName(goodsPricingPlan.getCreateUserName());
            EntityAuditor.populateCreateAudit(task, goodsPricingPlan.getCreateBy(), goodsPricingPlan.getCreatorId(),
                                              goodsPricingPlan.getCreatorType(), nowTime);
            EntityAuditor.populateUpdateAudit(task, goodsPricingPlan.getUpdateBy(), goodsPricingPlan.getUpdaterId(),
                                              goodsPricingPlan.getUpdaterType(), nowTime);
            goodsPricingTaskMapper.insert(task);

            //创建任务明细
            goodsPricingPlanDetails.forEach(d -> {
                GoodsPricingTaskDetail taskDetail = new GoodsPricingTaskDetail();
                taskDetail.setTaskId(task.getId());
                taskDetail.setGoodsCategoryIds(d.getGoodsCategoryIds());
                taskDetail.setUserIds(d.getUserIds());
                EntityAuditor.populateCreateAudit(taskDetail, goodsPricingPlan.getCreateBy(), goodsPricingPlan.getCreatorId(),
                                                  goodsPricingPlan.getCreatorType(), nowTime);
                EntityAuditor.populateUpdateAudit(taskDetail, goodsPricingPlan.getUpdateBy(), goodsPricingPlan.getUpdaterId(),
                                                  goodsPricingPlan.getUpdaterType(), nowTime);
                goodsPricingTaskDetailService.addAndNotice(task, taskDetail);
            });
        } else {
            //定时任务结束；更改计划状态
            goodsPricingPlan.setStatus(PlanStatusEnum.OVER.name());
            goodsPricingPlanMapper.updateById(goodsPricingPlan);
            //停止定时任务
            goodsPricingPlanService.removePlanJob(goodsPricingPlan.getXxlTaskId());
        }
        SessionContextHolder.requestTenant(null);

        return ReturnT.SUCCESS;
    }

}
