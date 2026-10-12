package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.message.handler;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.common.HeadquartersConstant;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.message.event.HeadquartersCultivateTaskPublishedEvent;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.message.event.HeadquartersCultivateTaskRevokedEvent;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.message.model.HeadquartersCultivateTaskMessage;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.service.HeadquartersCultivateTaskCoursewarLibraryService;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.vo.HeadquartersCultivateCoursewarVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.enumeration.CultivateOperationEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param.CultivateCoursewarParam;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param.CultivateTaskParam;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param.CultivateTaskSourceParam;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.service.CultivateTaskService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.ExamTaskService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskPO;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HeadquartersCultivateTaskEventHandler {

    private final HeadquartersCultivateTaskCoursewarLibraryService coursewarLibraryService;

    private final ExamTaskService examTaskService;

    private final CultivateTaskService cultivateTaskService;

    private static final String TOPIC = "HEADQUARTERS_CULTIVATE_TASK";

    @Transactional(rollbackFor = Exception.class)
    @EventListener
    public void addMarketTask(final HeadquartersCultivateTaskPublishedEvent event) {
        List<HeadquartersCultivateCoursewarVO> coursewarsByCultivateId = this.coursewarLibraryService.getCoursewarsByCultivateId(event
                                                                                                                                         .getMessage()
                                                                                                                                         .getTask()
                                                                                                                                         .getId());
        //获取关联的总部考试任务生成的市场考试任务列表
        Map<Integer, List<ExamTaskPO>> examTaskMap = new HashMap<>();
        if (Objects.nonNull(event.getMessage().getTask().getExamTaskId())) {
            examTaskMap = this.examTaskService.list(Wrappers.<ExamTaskPO>lambdaQuery()
                                                            .eq(ExamTaskPO::getSourceKey, event.getMessage().getTask().getExamTaskId())
                                                            .orderByDesc(ExamTaskPO::getCreateTime))
                    .stream()
                    .collect(Collectors.groupingBy(ExamTaskPO::getMarketTenant));
        }
        HeadquartersCultivateTaskMessage eventMessage = event.getMessage();
        Map<Integer, List<ExamTaskPO>> finalExamTaskMap = examTaskMap;
        event.getMessage().getMarkets().forEach(headquartersCultivateTaskMarket -> {
            CultivateTaskParam cultivateTaskParam = ObjectUtils.copying(eventMessage.getTask(), CultivateTaskParam.class);
            if (finalExamTaskMap.containsKey(headquartersCultivateTaskMarket.getMarketTenantCode())) {
                List<ExamTaskPO> examTaskPOS = finalExamTaskMap.get(headquartersCultivateTaskMarket.getMarketTenantCode());
                cultivateTaskParam.setExamTaskId(examTaskPOS.get(0).getId());
            } else {
                cultivateTaskParam.setExamTaskId(null);
            }
            cultivateTaskParam.setMarketTenant(headquartersCultivateTaskMarket.getMarketTenantCode());
            cultivateTaskParam.setCultivateCoursewares(ObjectUtils.copying(coursewarsByCultivateId, CultivateCoursewarParam.class));
            cultivateTaskParam.setCultivateTaskSource(CultivateTaskSourceParam.builder()
                                                              .sourceCode(HeadquartersConstant.TASK_SOURCE_CODE)
                                                              .sourceName(HeadquartersConstant.TASK_SOURCE_NAME)
                                                              .sourceKey(eventMessage.getTask().getId().toString())
                                                              .build());
            cultivateTaskService.add(cultivateTaskParam);
        });
    }

    @EventListener
    public void sendHeadquartersTaskPublishedMessage(final HeadquartersCultivateTaskPublishedEvent event) {
        List<HeadquartersCultivateCoursewarVO> coursewarsByCultivateId = this.coursewarLibraryService.getCoursewarsByCultivateId(event
                                                                                                                                         .getMessage()
                                                                                                                                         .getTask()
                                                                                                                                         .getId());
        event.getMessage().setHeadquartersCultivateCoursewars(coursewarsByCultivateId);
        event.getMessage().setOperationType(CultivateOperationEnum.PUBLISHED);
        RocketMQContextHolder.send(TOPIC, event.getMessage());
    }

    @Transactional(rollbackFor = Exception.class)
    @EventListener
    public void deleteMarketTask(final HeadquartersCultivateTaskRevokedEvent event) {
        this.cultivateTaskService.deleteBySourceKeys(Collections.singleton(event.getMessage().getTask().getId()));
    }

    @EventListener
    public void sendHeadquartersTaskRevokedMessage(final HeadquartersCultivateTaskRevokedEvent event) {
        List<HeadquartersCultivateCoursewarVO> coursewarsByCultivateId = this.coursewarLibraryService.getCoursewarsByCultivateId(event
                                                                                                                                         .getMessage()
                                                                                                                                         .getTask()
                                                                                                                                         .getId());
        event.getMessage().setHeadquartersCultivateCoursewars(coursewarsByCultivateId);
        event.getMessage().setOperationType(CultivateOperationEnum.WITHDRAW);
        event.getMessage().setId(event.getMessage().getTask().getId());
        RocketMQContextHolder.send(TOPIC, event.getMessage());
    }

}