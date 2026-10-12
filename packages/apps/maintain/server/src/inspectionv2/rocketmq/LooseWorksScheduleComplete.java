package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.rocketmq;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.PlanRelationService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectPlanCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.PlanRelationPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.rocketmq.message.ProjectScheduleMessage;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/11/11 15:47
 * @Description: 小散工程进度监听
 */

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "PROJECT_WORKS_COMPLETE", topic = ProjectScheduleMessage.TOPIC)
public class LooseWorksScheduleComplete implements RocketMQListener<ProjectScheduleMessage> {
    private final InspectPlanCommandService inspectPlanCommandService;
    private final PlanRelationService planRelationService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void onMessage(ProjectScheduleMessage message) {
        SessionContextHolder.requestTenant(message.getTenant());
        List<PlanRelationPO> relationPOList = planRelationService
                .lambdaQuery().select(PlanRelationPO::getPlanId)
                .eq(PlanRelationPO::getTargetId, message.getProjectId())
                .list();
        if (CollectionUtils.isEmpty(relationPOList)) {
            return;
        }
        List<Long> planIdList = relationPOList.stream().map(PlanRelationPO::getPlanId)
                .collect(Collectors.toList());
        try {
            inspectPlanCommandService.overPlanBatch(planIdList);
        } catch (Exception e) {
            log.info("小散工程完成结束计划失败:{}", e);
        }

    }
}
