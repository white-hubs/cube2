package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.rocketmq.consumer;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.entity.HeadquartersCultivateTask;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.service.HeadquartersCultivateTaskService;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.infrastructure.mq.message.HeadquartersExamTaskPublishedMessage;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.entity.CultivateTask;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.service.CultivateTaskService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.ExamTaskService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskPO;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "HEADQUARTERS_CULTIVATE_GROUP", topic = HeadquartersExamTaskPublishedMessage.TOPIC,
                         selectorType = SelectorType.TAG
)
public class HeadquartersExamTaskPublishedConsumer implements RocketMQListener<HeadquartersExamTaskPublishedMessage> {

    private final ExamTaskService examTaskService;

    private final HeadquartersCultivateTaskService headquartersCultivateTaskService;

    private final CultivateTaskService cultivateTaskService;

    @Override
    public void onMessage(HeadquartersExamTaskPublishedMessage message) {
        List<HeadquartersCultivateTask> headquartersCultivateTasks = this.headquartersCultivateTaskService.list(Wrappers
                                                                                                                        .<HeadquartersCultivateTask>lambdaQuery()
                                                                                                                        .eq(HeadquartersCultivateTask::getExamTaskId, message.getId()));
        if (CollectionUtils.isEmpty(headquartersCultivateTasks)) {
            return;
        }
        Set<Long> headquartersCultivateIds = headquartersCultivateTasks.stream().map(HeadquartersCultivateTask::getId)
                .collect(Collectors.toSet());
        List<ExamTaskPO> examTaskPOS = this.examTaskService.list(Wrappers.<ExamTaskPO>lambdaQuery()
                                                                         .eq(ExamTaskPO::getSourceKey, message.getId())
                                                                         .orderByDesc(ExamTaskPO::getCreateTime));
        if (CollectionUtils.isEmpty(examTaskPOS)) {
            return;
        }
        List<CultivateTask> cultivateTasks = this.cultivateTaskService.list(Wrappers.<CultivateTask>lambdaQuery()
                                                                                    .in(CultivateTask::getSourceKey, headquartersCultivateIds));
        cultivateTasks.stream().forEach(cultivateTask -> {
            Optional<ExamTaskPO> taskPO = examTaskPOS.stream()
                    .filter(examTaskPO -> cultivateTask.getMarketTenant().equals(examTaskPO.getMarketTenant())).findFirst();
            taskPO.ifPresent(examTaskPO -> cultivateTask.setExamTaskId(examTaskPO.getId()));
        });
        this.cultivateTaskService.updateBatchById(cultivateTasks);
    }
}
