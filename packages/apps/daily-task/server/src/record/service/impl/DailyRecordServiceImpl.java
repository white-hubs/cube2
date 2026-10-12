package com.guhecloud.cube.biz.daily.task.record.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.BusinessKeysParam;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.biz.daily.task.commons.constants.DailyConstants;
import com.guhecloud.cube.biz.daily.task.commons.param.DailyRecordSubmitAfterParam;
import com.guhecloud.cube.biz.daily.task.commons.po.DailyRecordItemPO;
import com.guhecloud.cube.biz.daily.task.commons.po.DailyRecordPO;
import com.guhecloud.cube.biz.daily.task.commons.po.DailyRecordTargetItemPO;
import com.guhecloud.cube.biz.daily.task.commons.po.DailyRecordTargetPO;
import com.guhecloud.cube.biz.daily.task.commons.result.DailyRecordResult;
import com.guhecloud.cube.biz.daily.task.commons.service.record.enhance.DailyRecordSubmitAfterHandler;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyRecordWorkOrderPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskTargetPO;
import com.guhecloud.cube.biz.daily.task.enums.DailyExecutionResultEnum;
import com.guhecloud.cube.biz.daily.task.enums.DailyRecordStatusEnum;
import com.guhecloud.cube.biz.daily.task.plan.coverts.DailyModelConverterUtil;
import com.guhecloud.cube.biz.daily.task.record.convert.DailyRecordItemConvert;
import com.guhecloud.cube.biz.daily.task.record.convert.DailyRecordTargetConvert;
import com.guhecloud.cube.biz.daily.task.record.convert.DailyRecordTargetItemConvert;
import com.guhecloud.cube.biz.daily.task.record.dao.DailyRecordMapper;
import com.guhecloud.cube.biz.daily.task.record.param.DailyRecordDetailParam;
import com.guhecloud.cube.biz.daily.task.record.param.DailyRecordPageParam;
import com.guhecloud.cube.biz.daily.task.record.param.DailyRecordSubmitParam;
import com.guhecloud.cube.biz.daily.task.record.param.DailyRecordRepairSubmitParam;
import com.guhecloud.cube.biz.daily.task.record.param.DailyRecordRepairTransferParam;
import com.guhecloud.cube.biz.daily.task.record.param.DailyRecordTargetParam;
import com.guhecloud.cube.biz.daily.task.record.result.DailyRecordDetail;
import com.guhecloud.cube.biz.daily.task.record.result.DailyRecordExportResult;
import com.guhecloud.cube.biz.daily.task.record.result.DailyRecordExceptionResult;
import com.guhecloud.cube.biz.daily.task.record.result.DailyRecordItemResult;
import com.guhecloud.cube.biz.daily.task.record.result.DailyRecordPageResult;
import com.guhecloud.cube.biz.daily.task.record.result.DailyRecordSubmitResult;
import com.guhecloud.cube.biz.daily.task.record.result.DailyRecordTargetResult;
import com.guhecloud.cube.biz.daily.task.record.result.DailyRecordWorkOrderResult;
import com.guhecloud.cube.biz.daily.task.record.service.DailyRecordItemService;
import com.guhecloud.cube.biz.daily.task.record.service.DailyRecordExceptionService;
import com.guhecloud.cube.biz.daily.task.record.service.DailyRecordService;
import com.guhecloud.cube.biz.daily.task.record.service.DailyRecordTargetItemService;
import com.guhecloud.cube.biz.daily.task.record.service.DailyRecordTargetService;
import com.guhecloud.cube.biz.daily.task.record.service.DailyRecordWorkOrderService;
import com.guhecloud.cube.biz.daily.task.status.service.BusinessStateMachine;
import com.guhecloud.cube.biz.daily.task.task.dao.DailyTaskTargetMapper;
import com.guhecloud.cube.biz.daily.task.task.service.DailyTaskTargetRefService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.guhecloud.cube.biz.daily.task.status.base.BaseEvent.HANDLE_TARGET;
import static com.guhecloud.cube.biz.daily.task.status.base.BaseEvent.TASK_HANDLE;

/**
 * 日常执行记录
 */
@Slf4j
@Service
public class DailyRecordServiceImpl extends ServiceImpl<DailyRecordMapper, DailyRecordPO> implements DailyRecordService {
    private final DailyRecordItemService dailyRecordItemService;
    private final DailyRecordExceptionService dailyRecordExceptionService;
    private final DailyRecordTargetService dailyRecordTargetService;
    private final DailyRecordTargetItemService dailyRecordTargetItemService;
    private final DailyRecordWorkOrderService dailyRecordWorkOrderService;
    private final BusinessStateMachine businessStateMachine;
    private final DailyTaskTargetMapper dailyTaskTargetMapper;
    private final DailyTaskTargetRefService dailyTaskTargetRefService;

    private final FileClient fileClient;
    private final FileRefClient fileRefClient;

    private final Map<String, DailyRecordSubmitAfterHandler> submitAfterHandlerMap;

    public DailyRecordServiceImpl(
            DailyRecordItemService dailyRecordItemService,
            DailyRecordExceptionService dailyRecordExceptionService,
            DailyRecordTargetService dailyRecordTargetService,
            DailyRecordTargetItemService dailyRecordTargetItemService,
            DailyRecordWorkOrderService dailyRecordWorkOrderService,
            BusinessStateMachine businessStateMachine,
            DailyTaskTargetMapper dailyTaskTargetMapper, DailyTaskTargetRefService dailyTaskTargetRefService,
            FileClient fileClient,
            FileRefClient fileRefClient,
            List<DailyRecordSubmitAfterHandler> allHandlers
                                 ) {

        this.dailyRecordItemService = dailyRecordItemService;
        this.dailyRecordExceptionService = dailyRecordExceptionService;
        this.dailyRecordTargetService = dailyRecordTargetService;
        this.dailyRecordTargetItemService = dailyRecordTargetItemService;
        this.dailyRecordWorkOrderService = dailyRecordWorkOrderService;
        this.businessStateMachine = businessStateMachine;
        this.dailyTaskTargetMapper = dailyTaskTargetMapper;
        this.dailyTaskTargetRefService = dailyTaskTargetRefService;
        this.fileClient = fileClient;
        this.fileRefClient = fileRefClient;

        // 自动构建 handler 映射表
        this.submitAfterHandlerMap = allHandlers.stream()
                .filter(h -> h.getType() != null && !h.getType().isEmpty())
                .collect(Collectors.toMap(DailyRecordSubmitAfterHandler::getType, Function.identity(), (o, n) -> n));
    }

    /**
     * 记录分页列表
     *
     * @param param 条件参数，包含类型、执行时间范围、任务名称、执行人名称、执行结果等查询条件
     *
     * @return 分页列表，包含记录的分页查询结果
     */
    @Override
    public Pagination<DailyRecordPageResult> pageRecord(DailyRecordPageParam param) {

        // 构造分页查询条件并执行分页查询
        var page = this.page(PagingUtils.convert(param), this.getPageQueryWrapper(param));

        // 将查询结果转换为返回格式
        var result = PagingUtils.convert(page, DailyRecordPageResult.class);
        if (CollectionUtils.isEmpty(result.getRecords())) {
            return result;
        }
        var recordIds = result.getRecords().stream().map(DailyRecordPageResult::getId).toList();
        var recordTargetNameMap = dailyRecordTargetService.lambdaQuery()
                .in(DailyRecordTargetPO::getRecordId, recordIds)
                .list()
                .stream()
                .filter(target -> StringUtils.isNotBlank(target.getTargetName()))
                .collect(Collectors.groupingBy(DailyRecordTargetPO::getRecordId,
                                               Collectors.mapping(DailyRecordTargetPO::getTargetName,
                                                                  Collectors.joining(","))));
        var exceptionMap = dailyRecordExceptionService.getResultByRecordIds(recordIds);
        result.getRecords().forEach(record -> {
            record.setTargetName(recordTargetNameMap.get(record.getId()));
            var exceptions = exceptionMap.getOrDefault(record.getId(), Collections.emptyList());
            if (CollectionUtils.isNotEmpty(exceptions)) {
                var exception = exceptions.getFirst();
                record.setExceptionStatus(exception.getStatus());
                record.setRepairUserId(exception.getRepairUserId());
                record.setRepairUserName(exception.getRepairUserName());
                record.setAllowRepair(exceptions.stream().anyMatch(e -> Boolean.TRUE.equals(e.getAllowRepair())));
            } else {
                record.setAllowRepair(false);
            }
        });
        return result;
    }

    /**
     * 根据记录ID(或任务+对象id)查询记录详情信息。
     *
     * @param param 记录的主键ID 或 任务id+对象id
     *
     * @return 返回封装好的记录详细信息对象 {@link DailyRecordDetail}
     */
    @Override
    public DailyRecordDetail detailRecord(DailyRecordDetailParam param) {
        DailyRecordPO record = null;
        if (param.getId() != null) {
            record = this.getById(param.getId());
        } else if (param.getTaskId() != null && param.getTargetId() != null) {
            var dailyRecordTargetPO = dailyRecordTargetService.lambdaQuery().eq(DailyRecordTargetPO::getTaskId, param.getTaskId())
                    .eq(DailyRecordTargetPO::getTargetId, param.getTargetId()).last("limit 1").one();
            record = this.getById(Optional.ofNullable(dailyRecordTargetPO).orElse(new DailyRecordTargetPO()).getRecordId());
        }
        Assert.isNotFound(record);
        // 实际上这里并不会抛出异常，因为上一行已经进行了检测，所以这里不会抛出异常
        var id = Objects.requireNonNull(record).getId();
        var detail = DailyModelConverterUtil.dailyRecordPoToResult(record);

        /* 查询该记录下的所有执行标准项 */
        var recordItem = dailyRecordItemService.lambdaQuery().eq(DailyRecordItemPO::getRecordId, id).list();
        // 处理执行标准项
        var recordItemResultList = dailyRecordItemService.getDailyRecordItemResults(recordItem);
        detail.setRecordItemList(recordItemResultList);

        /* 查询该记录关联的所有目标对象及其执行内容 */
        var recordTargetPOList = dailyRecordTargetService.lambdaQuery().eq(DailyRecordTargetPO::getRecordId, id).list();
        if (CollectionUtils.isNotEmpty(recordTargetPOList)) {
            // 查询各目标对象对应的执行项数据
            var recordTargetItemPOList = dailyRecordTargetItemService.lambdaQuery().eq(DailyRecordTargetItemPO::getRecordId, id).list();
            /* 将执行项与对应文件绑定，并按照目标对象ID进行分组 */
            var targetIdItemMap = dailyRecordTargetItemService.getRecordTargetStandardMap(recordTargetItemPOList);

            /* 组装最终的目标对象及各自的标准执行情况列表 */
            var targetResultList = recordTargetPOList.stream()
                    .map(target -> DailyRecordTargetResult.builder().targetId(target.getTargetId())
                            .targetName(target.getTargetName())
                            .targetCategory(target.getTargetCategory()).masterId(id)
                            .standardResultList(targetIdItemMap.get(target.getTargetId()))
                            .build()).collect(Collectors.toList());
            detail.setTargetList(targetResultList);
        }

        /* 处理工单 */
        var workOrderList = dailyRecordWorkOrderService.lambdaQuery().eq(DailyRecordWorkOrderPO::getRecordId, id).list();
        var workOrderResultList = Optional.ofNullable(workOrderList).orElse(Collections.emptyList()).stream()
                .map(DailyRecordWorkOrderResult::of).collect(Collectors.toList());
        detail.setWorkOrderList(workOrderResultList);
        var exceptionList = dailyRecordExceptionService.getResultByRecordIds(Collections.singletonList(id))
                .getOrDefault(id, Collections.emptyList());
        detail.setExceptionList(exceptionList);
        if (CollectionUtils.isNotEmpty(detail.getTargetList()) && CollectionUtils.isNotEmpty(exceptionList)) {
            var targetExceptionMap = exceptionList.stream().filter(e -> e.getTargetId() != null)
                    .collect(Collectors.toMap(e -> e.getTargetId(), Function.identity(), (o, n) -> o));
            detail.getTargetList().forEach(target -> target.setException(targetExceptionMap.get(target.getTargetId())));
        }
        return detail;
    }

    /**
     * 获取导出记录数据
     *
     * @param param 查询条件参数，用于过滤数据
     *
     * @return 导出结果列表，包含每条记录的基本信息以及相关的错误项名称集合
     */
    @Override
    public List<DailyRecordExportResult> getExportRecordDataList(DailyRecordPageParam param) {
        // 查询符合条件的记录列表
        var list = this.list(this.getPageQueryWrapper(param));
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }

        // 构建导出结果列表，并收集记录ID集合
        var recordIdSet = new HashSet<>();
        var exportResultList = list.stream().map(p -> {
            recordIdSet.add(p.getId());
            return DailyRecordExportResult.builder()
                    .id(p.getId())
                    .taskId(p.getTaskId())
                    .taskName(p.getTaskName())
                    .executionTime(p.getExecutionTime())
                    .executorId(p.getExecutorId())
                    .executorName(p.getExecutorName())
                    .executionResult(p.getExecutionResult())
                    .build();
        }).collect(Collectors.toList());

        // 查询所有记录中标识为错误的子项数据
        var recordItemPOList = dailyRecordItemService.lambdaQuery().in(DailyRecordItemPO::getRecordId, recordIdSet)
                .eq(DailyRecordItemPO::getErrorFlag, true).list();

        // 收集文件引用键并构建标准结果列表
        var fileRefIdSet = new HashSet<Long>();
        var standardresultList = Optional.ofNullable(recordItemPOList).orElse(new ArrayList<>()).stream().map(rip -> {
            var fileRefKey = rip.getFileRefKey();
            if (CollectionUtils.isNotEmpty(fileRefIdSet)) {
                fileRefIdSet.add(rip.getFileRefKey());
            }
            return DailyRecordItemResult.builder().taskName(rip.getTaskName()).itemId(rip.getItemId()).itemName(rip.getItemName())
                    .fileRefKey(fileRefKey)
                    .build();
        }).toList();

        // 若存在文件引用键，则获取对应文件信息并设置到结果对象中
        var resultStream = standardresultList.stream();
        if (CollectionUtils.isNotEmpty(fileRefIdSet)) {
            var fileParam = new BusinessKeysParam();
            fileParam.setBusinessKeys(new ArrayList<>(fileRefIdSet));
            var fileMap = fileClient.getByBusinessKeys(fileParam);
            resultStream = resultStream.peek(r -> r.setFileList(fileMap.get(r.getFileRefKey())));
        }

        // 按照标准ID分组错误项结果
        var standIdItemMap = resultStream.collect(Collectors.groupingBy(DailyRecordItemResult::getStandardId));

        /* 记录对象 */
        var recordTargetMap = new HashMap<Long, NameAndErrorDTO>();
        var recordTargetPOList = dailyRecordTargetService.lambdaQuery().in(DailyRecordTargetPO::getRecordId, recordIdSet).list();
        Optional.ofNullable(recordTargetPOList).orElse(Collections.emptyList()).stream()
                .collect(Collectors.groupingBy(DailyRecordTargetPO::getRecordId)).forEach((recordId, ls) -> {
                    StringBuilder BuilderTargetNames = new StringBuilder();
                    StringBuilder BuilderRecordErrorNames = new StringBuilder();
                    ls.forEach(target -> {
                        BuilderTargetNames.append(target.getTargetName()).append(",");
                        if (DailyExecutionResultEnum.ABNORMAL.name().equals(target.getExecutionResult())) {
                            BuilderRecordErrorNames.append(target.getTargetName()).append(",");
                        }
                    });
                    String targetNames = "";
                    String recordErrorNames = "";
                    if (!BuilderTargetNames.isEmpty()) {
                        targetNames = BuilderTargetNames.substring(0, BuilderTargetNames.length() - 1);
                    }
                    if (!BuilderRecordErrorNames.isEmpty()) {
                        recordErrorNames = BuilderRecordErrorNames.substring(0, BuilderRecordErrorNames.length() - 1);
                    }
                    recordTargetMap.put(recordId, new NameAndErrorDTO(targetNames, recordErrorNames));
                });

        // 将每个记录对应的错误项名称拼接后设置进导出结果中
        exportResultList.forEach(e -> {
            var errorNames = Optional.ofNullable(standIdItemMap.get(e.getId())).orElse(new ArrayList<>()).stream()
                    .map(DailyRecordItemResult::getItemName)
                    .collect(Collectors.joining());
            var targetNameDTO = recordTargetMap.getOrDefault(e.getId(), new NameAndErrorDTO());
            e.setTargetNames(targetNameDTO.getNames());
            e.setTargetErrorNames(targetNameDTO.getErrorNames());
            e.setRecordErrorNames(errorNames);
        });

        return exportResultList;
    }

    /**
     * 提交一条日常记录（Daily Record）及相关子项数据。
     * <p>
     * 该方法会根据传入的参数构建主记录、记录项、目标及目标项等实体对象，并保存至数据库，
     * 同时处理与之关联的文件信息并调用文件服务完成文件绑定操作。
     *
     * @param param 日常记录提交参数，包含任务ID、名称、类型以及各项明细数据
     *
     * @return 返回生成的记录ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized DailyRecordSubmitResult submitRecord(DailyRecordSubmitParam param) {
        var principal = SecurityContextHolder.getPrincipal();
        Assert.isConflict(principal == null, "用户未登录，请先登录！");
        // 初始化关联对象的参数
        this.initParamOtherTarget(param);
        Long recordId = IdWorker.getId();
        var fileRefMap = new HashMap<Long, List<Long>>();

        var dailyRecordPO = DailyRecordPO.builder().id(recordId).dailyType(param.getDailyType()).taskId(param.getTaskId())
                .taskName(param.getTaskName()).executorId(principal.getId()).executorName(principal.getName())
                .remark(param.getRemark())
                .executionTime(LocalDateTime.now()).build();
        //记录绑定图片
        if (CollectionUtils.isNotEmpty(param.getFileIds())) {
            fileRefMap.put(recordId, param.getFileIds());
        }
        AtomicBoolean executorResultBool = new AtomicBoolean(true);
        /* 获取对象数据 */
        var targetMap = this.getLongDailyTaskTargetPOHashMap(param);

        var recordItemList = new ArrayList<DailyRecordItemPO>();
        var recordTargetList = new ArrayList<DailyRecordTargetPO>();
        var recordTargetItemList = new ArrayList<DailyRecordTargetItemPO>();

        // 构建并填充日常记录项列表
        Optional.ofNullable(param.getRecordItemList()).orElse(new ArrayList<>())
                .forEach(item -> {
                    if (Boolean.TRUE.equals(item.getErrorFlag())) {
                        executorResultBool.set(false);
                    }
                    recordItemList.add(DailyRecordItemConvert.buildRecordItem(param, item, recordId, fileRefMap));
                });

        // 构建并填充记录目标及其对应的子项列表
        List<Long> taskTargetIds = new ArrayList<>();
        Map<Long, String> abnormalTargets = new HashMap<>();
        Set<Long> targetIds = new HashSet<>();
        Optional.ofNullable(param.getRecordTargetList()).orElse(new ArrayList<>()).forEach(target -> {
            targetIds.add(target.getTargetId());
            var taskTarget = targetMap.getOrDefault(target.getTargetId(), new DailyTaskTargetPO());
            taskTargetIds.add(taskTarget.getId());
            var recordTarget = DailyRecordTargetConvert.builderRecordTarget(param.getTaskId(), param.getTaskName(), recordId, target.getTargetId(), targetMap);

            AtomicBoolean targetExecutorResultBool = new AtomicBoolean(true);
            target.getTargetItemList().forEach(targetItem -> {
                // 判断是否有错误项
                if (Boolean.TRUE.equals(targetItem.getErrorFlag())) {
                    executorResultBool.set(false);
                    targetExecutorResultBool.set(false);
                }
                var fileRefId = IdWorker.getId();
                fileRefMap.put(fileRefId, targetItem.getFileIdList());
                recordTargetItemList.add(DailyRecordTargetItemConvert.builderRecordTargetItem(param.getTaskId(), param.getTaskName(), recordId, recordTarget, targetItem, fileRefId));
            });
            var executionResult = CollectionUtils.isNotEmpty(target.getTargetItemList()) && targetExecutorResultBool.get()
                                  ? DailyExecutionResultEnum.NORMAL.name() : DailyExecutionResultEnum.ABNORMAL.name();
            recordTarget.setExecutionResult(executionResult);
            if (DailyExecutionResultEnum.ABNORMAL.name().equals(executionResult)) {
                executorResultBool.set(false);
                abnormalTargets.put(target.getTargetId(), taskTarget.getTargetName());
            }
            recordTargetList.add(recordTarget);
        });
        dailyRecordPO.setExecutionResult(
                executorResultBool.get() ? DailyExecutionResultEnum.NORMAL.name() : DailyExecutionResultEnum.ABNORMAL.name());
        dailyRecordPO.setRecordStatus(executorResultBool.get() ? DailyRecordStatusEnum.NORMAL.name()
                                      : DailyRecordStatusEnum.ABNORMAL_WAIT_REPAIR.name());
        // 构建并填充文件关联参数
        var fileRefCreateParams = new ArrayList<FileRefCreateParam>();
        if (!fileRefMap.isEmpty()) {
            fileRefMap.forEach((fileRefId, fileIdList) -> {
                var fileRefCreateParam = new FileRefCreateParam();
                fileRefCreateParam.setSceneCode(DailyConstants.DAILY_TASK_CREATE_FILE);
                fileRefCreateParam.setBusinessKey(fileRefId);
                fileRefCreateParam.setFileIdList(fileIdList);
                fileRefCreateParam.setTag(DailyConstants.DAILY_TASK);
                fileRefCreateParams.add(fileRefCreateParam);
            });
        }
        taskTargetIds.removeIf(Objects::isNull);
        this.submitDatabaseOperation(dailyRecordPO, recordItemList, recordTargetList, recordTargetItemList, fileRefCreateParams);
        //更新对象最终任务ID
        targetMap.values().forEach(t -> t.setTaskId(param.getTaskId()));
        dailyTaskTargetMapper.updateById(new ArrayList<>(targetMap.values()));
        businessStateMachine.pushEvent(taskTargetIds, param.getDailyType(), HANDLE_TARGET);
        businessStateMachine.pushEvent(param.getTaskId(), param.getDailyType(), TASK_HANDLE);
        if (DailyExecutionResultEnum.ABNORMAL.name().equals(dailyRecordPO.getExecutionResult())) {
            try {
                dailyRecordExceptionService.createExceptions(recordId, param.getTaskId(), param.getDailyType(), abnormalTargets);
            } catch (Exception e) {
                log.error("创建日常任务异常修复记录失败，recordId={}：", recordId, e);
            }
        }

        try {
            var handler = submitAfterHandlerMap.get(param.getDailyType());
            if (handler != null) {
                handler.handle(DailyRecordSubmitAfterParam.builder().dailyRecordPO(dailyRecordPO).recordItemList(recordItemList)
                                       .recordTargetList(recordTargetList).recordTargetItemList(recordTargetItemList)
                                       .fileRefCreateParams(fileRefCreateParams).build());
            }
        } catch (Exception e) {
            log.error("提交执行结果后置处理异常{}：", param.getDailyType(), e);
        }
        DailyRecordSubmitResult result = new DailyRecordSubmitResult();
        result.setRecordId(recordId);
        if (CollectionUtils.isNotEmpty(targetIds)) {
            result.setTaskTargetRefResults(dailyTaskTargetMapper.selectCurrentUserWaitHandlerTask(SecurityContextHolder.getPrincipal()
                                                                                                          .getId(), targetIds));
        }
        return result;
    }

    @Override
    public Boolean submitRepair(DailyRecordRepairSubmitParam param) {
        return dailyRecordExceptionService.submitRepair(param);
    }

    @Override
    public Boolean transferRepair(DailyRecordRepairTransferParam param) {
        return dailyRecordExceptionService.transferRepair(param);
    }

    @Override
    public DailyRecordExceptionResult getRepairDetail(Long exceptionId) {
        return dailyRecordExceptionService.getExceptionDetail(exceptionId);
    }

    public void initParamOtherTarget(DailyRecordSubmitParam param) {
        var resultList = new ArrayList<DailyRecordTargetParam>();
        if (param.getRecordTargetList() == null) {
            return;
        }
        param.getRecordTargetList().forEach(target -> {
            resultList.add(target);
            if (target.getOtherTargetIdList() != null) {
                target.getOtherTargetIdList().forEach(otherTargetId -> {
                    DailyRecordTargetParam otherTarget = new DailyRecordTargetParam();
                    otherTarget.setTargetId(otherTargetId);
                    otherTarget.setTargetCategory(target.getTargetCategory());
                    otherTarget.setTargetItemList(target.getTargetItemList());
                    resultList.add(otherTarget);
                });
            }
        });
        if (param.getRecordTargetList().size() != resultList.size()) {
            param.setRecordTargetList(resultList);
        }
    }

    @Override
    public DailyRecordResult getNormalTaskRecord(Long id) {
        DailyRecordPO dailyRecordPO = this.lambdaQuery().eq(DailyRecordPO::getTaskId, id)
                .last("limit 1")
                .one();
        if (Objects.isNull(dailyRecordPO)) {
            return new DailyRecordResult();
        }
        DailyRecordResult detail = DailyModelConverterUtil.dailyRecordPoToNormalResult(dailyRecordPO);
        detail.setFileList(fileClient.getByBusinessKey(dailyRecordPO.getId()));
        return detail;
    }

    /**
     * 根据提交参数获取目标ID与任务目标PO对象的映射关系
     *
     * @param param 日常记录提交参数，包含任务ID和记录目标列表
     *
     * @return 以目标ID为key，DailyTaskTargetPO对象为value的HashMap
     */
    private HashMap<Long, DailyTaskTargetPO> getLongDailyTaskTargetPOHashMap(DailyRecordSubmitParam param) {
        var targetMap = new HashMap<Long, DailyTaskTargetPO>();
        if (CollectionUtils.isNotEmpty(param.getRecordTargetList())) {
            // 提取目标ID
            var targetIdSet = new HashSet<Long>();
            param.getRecordTargetList().forEach(target -> targetIdSet.add(target.getTargetId()));
            Set<Long> targetRefIds = dailyTaskTargetRefService.getTargetIds(Collections.singleton(param.getTaskId()));
            var taskTargetPOList = Optional
                    .ofNullable(dailyTaskTargetMapper.selectList(Wrappers.<DailyTaskTargetPO>lambdaQuery()
                                                                         .in(DailyTaskTargetPO::getId, targetRefIds)
                                                                         .in(DailyTaskTargetPO::getTargetId, targetIdSet)))
                    .orElse(new ArrayList<>());
            String completeState = businessStateMachine.getCompleteState(param.getDailyType(), HANDLE_TARGET);
            Optional<DailyTaskTargetPO> existCompletedTarget = taskTargetPOList.stream()
                    .filter(t -> StringUtils.equals(completeState, t.getStatus()))
                    .findFirst();
            if (existCompletedTarget.isPresent()) {
                throw new ServiceException("存在已处理对象，请刷新重新选择");
            }
            taskTargetPOList.forEach(tp -> targetMap.put(tp.getTargetId(), tp));
        }
        return targetMap;
    }

    /**
     * 提交数据库操作事务，保存相关数据及关联文件
     *
     * @param dailyRecordPO        主记录实体对象
     * @param recordItemList       记录项列表
     * @param recordTargetList     目标记录列表
     * @param recordTargetItemList 目标记录项列表
     * @param fileRefCreateParams  文件关联列表
     */
    @Transactional(rollbackFor = Exception.class)
    private void submitDatabaseOperation(DailyRecordPO dailyRecordPO, ArrayList<DailyRecordItemPO> recordItemList,
                                         ArrayList<DailyRecordTargetPO> recordTargetList,
                                         ArrayList<DailyRecordTargetItemPO> recordTargetItemList,
                                         ArrayList<FileRefCreateParam> fileRefCreateParams
                                        ) {

        // 持久化主记录及相关子表数据
        this.save(dailyRecordPO);
        if (CollectionUtils.isNotEmpty(recordItemList)) {
            dailyRecordItemService.saveBatch(recordItemList);
        }
        if (CollectionUtils.isNotEmpty(recordTargetList)) {
            dailyRecordTargetService.saveBatch(recordTargetList);
        }
        if (CollectionUtils.isNotEmpty(recordTargetItemList)) {
            dailyRecordTargetItemService.saveBatch(recordTargetItemList);
        }
        if (CollectionUtils.isNotEmpty(fileRefCreateParams)) {
            fileRefClient.refInduceBatch(fileRefCreateParams);
        }
    }

    /**
     * 构建记录分页查询的包装器
     *
     * @param param 记录分页查询参数对象，包含查询条件
     *
     * @return 返回构建好的查询包装器，用于数据库查询操作
     */
    private Wrapper<DailyRecordPO> getPageQueryWrapper(DailyRecordPageParam param) {
        // 判断执行时间范围条件是否完整（开始时间和结束时间都不为空）
        var executionTime = param.getExecutionTime();
        var executionTimeBool = param.getExecutionTime() != null && param.getExecutionTime().getBegin() != null &&
                                param.getExecutionTime().getEnd() != null;
        return Wrappers.<DailyRecordPO>lambdaQuery()
                .eq(DailyRecordPO::getDailyType, param.getDailyType())
                .between(executionTimeBool, DailyRecordPO::getExecutionTime, ObjectUtils.ifNull(executionTime, Period::getBegin), ObjectUtils.ifNull(executionTime, Period::getEnd))
                .like(StringUtils.isNotBlank(param.getTaskName()), DailyRecordPO::getTaskName, param.getTaskName())
                .like(StringUtils.isNotBlank(param.getExecutorName()), DailyRecordPO::getExecutorName, param.getExecutorName())
                .eq(StringUtils.isNotBlank(param.getExecutionResult()), DailyRecordPO::getExecutionResult, param.getExecutionResult())
                .eq(StringUtils.isNotBlank(param.getRecordStatus()), DailyRecordPO::getRecordStatus, param.getRecordStatus())
                .in(CollectionUtils.isNotEmpty(param.getRecordStatuses()), DailyRecordPO::getRecordStatus, param.getRecordStatuses())
                .in(CollectionUtils.isNotEmpty(param.getTaskIds()), DailyRecordPO::getTaskId, param.getTaskIds())
                .orderByDesc(DailyRecordPO::getExecutionTime);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class NameAndErrorDTO {
        private String names;
        private String errorNames;
    }

}
