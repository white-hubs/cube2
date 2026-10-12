package com.guhecloud.cube.srv.gen.safety.education.server.transfer.service.Impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.client.TenantClient;
import com.guhecloud.cube.biz.ouaa.client.dto.TenantDTO;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.common.HeadquartersConstant;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.entity.HeadquartersCultivateTask;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.entity.HeadquartersCultivateTaskMarket;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.message.model.HeadquartersCultivateTaskMessage;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.service.HeadquartersCultivateTaskService;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.vo.HeadquartersCultivateCoursewarVO;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.domain.aggregates.HeadquartersExamTask;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.domain.valueobj.HeadquartersExamTaskMarket;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.infrastructure.mq.message.HeadquartersExamTaskPublishedMessage;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.entity.CultivateTask;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.entity.CultivateTaskCoursewareLibrary;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.enumeration.CultivateOperationEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param.CultivateCoursewarParam;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param.CultivateTaskParam;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param.CultivateTaskSourceParam;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.service.CultivateTaskCoursewarLibraryService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.service.CultivateTaskService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.ExamTaskService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.ExamTaskCommandService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command.DeleteExamTaskCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.aggregates.ExamTask;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskOperationEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskStatusEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamTaskStatus;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.repository.ExamTaskRepository;
import com.guhecloud.cube.srv.gen.safety.education.server.transfer.common.file.service.GroupPublishedFileService;
import com.guhecloud.cube.srv.gen.safety.education.server.transfer.rockermq.producer.wapper.MarketTaskFinishProducerWarp;
import com.guhecloud.cube.srv.gen.safety.education.server.transfer.service.DataTransferService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.redis.util.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.redisson.RedissonRedLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ConditionalOnBean(name = "transferRocketMQProperties")
public class DataTransferServiceImpl implements DataTransferService {

    private final TenantClient tenantClient;

    private final ExamTaskRepository examTaskRepository;

    private final ExamTaskCommandService examTaskCommandService;

    private final ExamTaskService examTaskService;

    private final CultivateTaskService cultivateTaskService;

    private final GroupPublishedFileService groupPublishedFileService;

    private final MarketTaskFinishProducerWarp marketTaskFinishProducerWarp;

    private final CultivateTaskCoursewarLibraryService coursewarLibraryService;

    private final HeadquartersCultivateTaskService headquartersCultivateTaskService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ConsumeOrderlyStatus trainingTaskDataTransfer(String groupTrainTaskMessage) {

        HeadquartersCultivateTaskMessage headquartersCultivateTaskMessage = JsonUtils
                .deserialize(groupTrainTaskMessage, HeadquartersCultivateTaskMessage.class);
        //获取市场信息
        List<HeadquartersCultivateTaskMarket> markets = headquartersCultivateTaskMessage.getMarkets();
        Assert.parameterIsNull(markets, "获取集团下市场信息为空");
        //消息租户过滤
        List<TenantDTO> tenantDTOS = tenantClient.listTenants("");
        List<Integer> currentTenantCodeList = new ArrayList<>(tenantDTOS.size());
        if (ObjectUtils.isNotEmpty(tenantDTOS)) {
            currentTenantCodeList = tenantDTOS
                    .stream().map(TenantDTO::getCode).collect(Collectors.toList());
        }
        if (ObjectUtils.isEmpty(currentTenantCodeList)) {
            return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
        }
        Optional<Integer> first = markets.stream()
                .map(HeadquartersCultivateTaskMarket::getMarketTenantCode)
                .filter(currentTenantCodeList::contains)
                .findFirst();
        //不存在
        if (!first.isPresent()) {
            return ConsumeOrderlyStatus.SUCCESS;
        }
        SessionContextHolder.requestTenant(first.get());
        //获取关联的总部考试任务生成的市场考试任务列表
        List<ExamTaskPO> examTasks = new ArrayList<>();
        if (Objects.nonNull(headquartersCultivateTaskMessage.getTask().getExamTaskId())) {
            examTasks = this.examTaskService.list(Wrappers.<ExamTaskPO>lambdaQuery()
                                                          .eq(ExamTaskPO::getSourceKey, headquartersCultivateTaskMessage.getTask()
                                                                  .getExamTaskId())
                                                          .eq(ExamTaskPO::getMarketTenant, first.get())
                                                          .orderByDesc(ExamTaskPO::getCreateTime));
        }
        List<ExamTaskPO> finalExamTasks = examTasks;
        Long sourceKey = headquartersCultivateTaskMessage.getTask().getId();
        try {
            if (CultivateOperationEnum.PUBLISHED.equals(headquartersCultivateTaskMessage.getOperationType())) {
                //是否存在
                CultivateTask groupTask = cultivateTaskService.getOne(Wrappers.<CultivateTask>lambdaQuery()
                                                                              .eq(CultivateTask::getSourceKey, sourceKey.toString())
                                                                              .eq(CultivateTask::getMarketTenant, first.get()));
                boolean existFlag = false;
                if (Objects.nonNull(groupTask)) {
                    existFlag = true;
                }
                CultivateTaskParam cultivateTaskParam = ObjectUtils.copying(headquartersCultivateTaskMessage.getTask(), CultivateTaskParam.class);
                if (!CollectionUtils.isEmpty(finalExamTasks)) {
                    cultivateTaskParam.setExamTaskId(finalExamTasks.get(0).getId());
                } else {
                    cultivateTaskParam.setExamTaskId(null);
                }
                //市场编码
                cultivateTaskParam.setMarketTenant(first.get());
                //培训课件
                List<HeadquartersCultivateCoursewarVO> headquartersCultivateCoursewars = headquartersCultivateTaskMessage.getHeadquartersCultivateCoursewars();
                if (!CollectionUtils.isEmpty(headquartersCultivateCoursewars)) {
                    // 培训课件处理
                    List<CultivateCoursewarParam> cultivateCoursewares = new ArrayList<>(headquartersCultivateCoursewars.size());
                    for (HeadquartersCultivateCoursewarVO headquartersCultivateCoursewar : headquartersCultivateCoursewars) {
                        CultivateCoursewarParam coursewarParam = ObjectUtils.copying(headquartersCultivateCoursewar, CultivateCoursewarParam.class);
                        StringBuilder fileIds = new StringBuilder();
                        for (FileVO file : headquartersCultivateCoursewar.getFiles()) {
                            if (StringUtils.isNotBlank(fileIds.toString())) {
                                fileIds.append(",");
                            }
                            fileIds.append(groupPublishedFileService.uploadFileByPath(first.get(), file.getPath()));
                        }
                        coursewarParam.setFileIds(fileIds.toString());
                        cultivateCoursewares.add(coursewarParam);
                    }
                    cultivateTaskParam.setCultivateCoursewares(cultivateCoursewares);
                }
                //任务来源
                cultivateTaskParam.setCultivateTaskSource(CultivateTaskSourceParam.builder()
                                                                  .sourceCode(HeadquartersConstant.TASK_SOURCE_CODE)
                                                                  .sourceName(HeadquartersConstant.TASK_SOURCE_NAME)
                                                                  .sourceKey(sourceKey.toString())
                                                                  .build());
                if (existFlag) {
                    cultivateTaskService.update(groupTask.getId(), cultivateTaskParam);
                } else {
                    cultivateTaskService.add(cultivateTaskParam);
                }
            } else if (CultivateOperationEnum.WITHDRAW
                    .equals(headquartersCultivateTaskMessage.getOperationType())) {
                //撤回逻辑
                cultivateTaskService.deleteBySourceKeys(Collections.singleton(headquartersCultivateTaskMessage.getTask().getId()));

            }
        } finally {
            SessionContextHolder.clearSessionContext();
        }

        return ConsumeOrderlyStatus.SUCCESS;
    }

    private void deleteFileList(boolean existFlag, CultivateTask groupTask) {
        List<Long> existFileList = new ArrayList<>();
        //删除已存在的文件
        if (existFlag) {
            List<CultivateTaskCoursewareLibrary> existCoursewareLibrary = coursewarLibraryService
                    .list(Wrappers.<CultivateTaskCoursewareLibrary>lambdaQuery()
                                  .eq(CultivateTaskCoursewareLibrary::getCultivateTaskId, groupTask.getId())
                                  .eq(CultivateTaskCoursewareLibrary::getDeleted, false));
            if (!CollectionUtils.isEmpty(existCoursewareLibrary)) {
                existCoursewareLibrary.forEach(taskCoursewareLibrary -> {
                    if (StringUtils.contains(taskCoursewareLibrary.getFileIds(), ",")) {
                        existFileList.addAll(Arrays.stream(taskCoursewareLibrary.getFileIds().split(","))
                                                     .map(Long::parseLong)
                                                     .toList());
                    } else if (StringUtils.isNotBlank(taskCoursewareLibrary.getFileIds())) {
                        existFileList.add(Long.parseLong(taskCoursewareLibrary.getFileIds()));
                    }
                });
            }
        }
        if (!CollectionUtils.isEmpty(existFileList)) {
            groupPublishedFileService.fileBatchDelete(existFileList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ConsumeOrderlyStatus examTaskDataTransfer(String groupExamTaskMessage) {
        HeadquartersExamTaskPublishedMessage examTaskReceiveMessage = JsonUtils
                .deserialize(groupExamTaskMessage, HeadquartersExamTaskPublishedMessage.class);
        HeadquartersExamTask headquartersExamTask = JsonUtils
                .deserialize(examTaskReceiveMessage.getExamTaskContent(), HeadquartersExamTask.class);

        //获取市场信息
        List<HeadquartersExamTaskMarket> markets = headquartersExamTask.getExamTaskMarkets();

        Assert.parameterIsNull(markets, "获取集团下市场信息为空");
        //消息租户过滤
        List<TenantDTO> tenantDTOS = tenantClient.listTenants("");
        List<Integer> currentTenantCodeList = new ArrayList<>(tenantDTOS.size());
        if (ObjectUtils.isNotEmpty(tenantDTOS)) {
            currentTenantCodeList = tenantDTOS
                    .stream().map(TenantDTO::getCode).collect(Collectors.toList());
        }
        if (ObjectUtils.isEmpty(currentTenantCodeList)) {
            return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
        }
        Optional<Integer> first = markets.stream()
                .map(HeadquartersExamTaskMarket::getMarketTenantCode)
                .filter(currentTenantCodeList::contains)
                .findFirst();
        //不存在 ack
        if (!first.isPresent()) {
            return ConsumeOrderlyStatus.SUCCESS;
        }
        List<HeadquartersCultivateTask> headquartersCultivateTasks = this.headquartersCultivateTaskService.list(Wrappers
                                                                                                                        .<HeadquartersCultivateTask>lambdaQuery()
                                                                                                                        .eq(HeadquartersCultivateTask::getExamTaskId, headquartersExamTask
                                                                                                                                .getId()
                                                                                                                                .toString()));
        SessionContextHolder.requestTenant(first.get());
        try {
            if (ExamTaskOperationEnum.PUBLISHED.equals(examTaskReceiveMessage.getOperationType())) {
                //数据转换和下发
                ExamTask groupExamTask = new ExamTask(IdWorker.getId(),
                                                      headquartersExamTask.getExamTaskInfo(),
                                                      headquartersExamTask.getThemeInfo(),
                                                      headquartersExamTask.getExamTaskChargeUserInfo(),
                                                      headquartersExamTask.getExamTaskType(),
                                                      new ExamTaskStatus(ExamTaskStatusEnum.UNPUBLISHED, ExamTaskStatusEnum.UNPUBLISHED.name()));
                //考试时间信息
                groupExamTask.settingExamTaskDateInfo(headquartersExamTask.getExamTaskDateInfo().getStartTime(), headquartersExamTask
                        .getExamTaskDateInfo().getEndTime());
                //任务来源
                groupExamTask.settingExamTaskSource(HeadquartersConstant.TASK_SOURCE_CODE, HeadquartersConstant.TASK_SOURCE_NAME, headquartersExamTask
                        .getId().toString());
                //试卷信息
                groupExamTask.settingExamPaper(headquartersExamTask.getExamPapers());
                //复考信息
                groupExamTask.settingExamTaskRetakeInfo(headquartersExamTask.getExamTaskRetakeInfo().getRetake(), headquartersExamTask
                        .getExamTaskRetakeInfo().getRetakeNumber());
                //时段信息
                groupExamTask.settingExamTaskTimedInfo(headquartersExamTask.getExamTaskTimedInfo().getTimed(), headquartersExamTask
                        .getExamTaskTimedInfo().getExamDuration(), headquartersExamTask.getExamTaskTimedInfo().getExamDurationUnit());
                groupExamTask.settingExamTaskUserNum(headquartersExamTask.getExamUserNumber());
                groupExamTask.setMarketTenant(first.get());
                examTaskRepository.save(groupExamTask);
                if (!CollectionUtils.isEmpty(headquartersCultivateTasks)) {
                    Set<Long> headquartersCultivateIds = headquartersCultivateTasks.stream().map(HeadquartersCultivateTask::getId)
                            .collect(Collectors.toSet());
                    List<CultivateTask> cultivateTasks = this.cultivateTaskService.list(Wrappers.<CultivateTask>lambdaQuery()
                                                                                                .in(CultivateTask::getSourceKey, headquartersCultivateIds));
                    cultivateTasks.forEach(cultivateTask ->
                                                   cultivateTask.setExamTaskId(groupExamTask.getId())
                                          );
                    this.cultivateTaskService.updateBatchById(cultivateTasks);
                }
            } else if (ExamTaskOperationEnum.WITHDRAW
                    .equals(examTaskReceiveMessage.getOperationType())) {
                //任务删除
                examTaskCommandService.deleteExamTaskBySourceId(new DeleteExamTaskCommand(Collections.singleton(headquartersExamTask.getId())));
            }
        } finally {
            SessionContextHolder.clearSessionContext();
        }
        return ConsumeOrderlyStatus.SUCCESS;
    }

    @Override
    public void marketMessagePushGroup(String id, String sourceCode, Message message) {
        RedissonRedLock redissonRedLock = RedisUtils.getDistributedLock()
                .tryLock(id,
                         Expiration.from(5000, TimeUnit.MILLISECONDS));
        //数据转换发送给集团
        try {
            //如果市场发布的消息为来源集团
            if (HeadquartersConstant.TASK_SOURCE_CODE.equals(sourceCode)) {

                SendResult sendResult = marketTaskFinishProducerWarp.send(message, 300000);
                if (Objects.nonNull(sendResult)) {
                    log.info("市场转发集团数据MQ发送状态为:{},MsgId为:{},Topic为:{},brokerName为:{}",
                             sendResult.getSendStatus().name(),
                             sendResult.getMsgId(),
                             Objects.nonNull(sendResult.getMessageQueue()) ? sendResult.getMessageQueue().getTopic() : "",
                             Objects.nonNull(sendResult.getMessageQueue()) ? sendResult.getMessageQueue().getBrokerName() : "");
                }
            }
        } catch (Exception e) {
            log.error("市场转发集团数据数据推送失败:{}", e.getMessage());
        } finally {
            if (Objects.nonNull(redissonRedLock)) {
                redissonRedLock.unlock();
            }
        }
    }
}
