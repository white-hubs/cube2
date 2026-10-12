package com.guhecloud.cube.biz.daily.task.record.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.daily.task.commons.po.DailyRecordTargetItemPO;
import com.guhecloud.cube.biz.daily.task.commons.po.DailyRecordTargetPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyRecordWorkOrderPO;
import com.guhecloud.cube.biz.daily.task.record.dao.DailyRecordTargetMapper;
import com.guhecloud.cube.biz.daily.task.record.dto.DailyRecordTargetErrorDTO;
import com.guhecloud.cube.biz.daily.task.record.param.DailyRecordDetailParam;
import com.guhecloud.cube.biz.daily.task.record.param.DailyRecordTargetBatchParam;
import com.guhecloud.cube.biz.daily.task.record.result.DailyRecordTargetDetailResult;
import com.guhecloud.cube.biz.daily.task.record.result.DailyRecordWorkOrderResult;
import com.guhecloud.cube.biz.daily.task.record.service.DailyRecordTargetItemService;
import com.guhecloud.cube.biz.daily.task.record.service.DailyRecordTargetService;
import com.guhecloud.cube.biz.daily.task.record.service.DailyRecordWorkOrderService;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserMapQuery;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DailyRecordTargetServiceImpl extends ServiceImpl<DailyRecordTargetMapper, DailyRecordTargetPO> implements DailyRecordTargetService {

    private final DailyRecordTargetItemService dailyRecordTargetItemService;
    private final DailyRecordWorkOrderService dailyRecordWorkOrderService;

    private final UserClient userClient;

    /**
     * 获取记录对象详情
     *
     * @param param 查询参数，包含任务ID、目标ID和记录ID等信息
     *
     * @return DailyRecordTargetStandardResult 记录目标的详细信息，包括标准ID、标准名称、文件列表等
     */
    @Override
    public DailyRecordTargetDetailResult getRecordTargetDetail(DailyRecordDetailParam param) {
        // 参数校验，确保任务ID和目标ID不为空
        Assert.isParameterError(param.getTaskId() == null || param.getTargetId() == null, "请填写完整参数！");

        // 查询目标项信息，根据记录ID、目标ID和任务ID进行精确查询
        var targetItemList = dailyRecordTargetItemService.lambdaQuery()
                .eq(param.getId() != null, DailyRecordTargetItemPO::getRecordId, param.getId())
                .eq(DailyRecordTargetItemPO::getTargetId, param.getTargetId()).eq(DailyRecordTargetItemPO::getTaskId, param.getTaskId())
                .list();
        /* 处理工单 */
        var workOrderList = dailyRecordWorkOrderService.lambdaQuery().eq(DailyRecordWorkOrderPO::getTaskId, param.getTaskId())
                .eq(DailyRecordWorkOrderPO::getTargetId, param.getTargetId()).list();
        var workOrderResultList = Optional.ofNullable(workOrderList).orElse(Collections.emptyList()).stream()
                .map(DailyRecordWorkOrderResult::of).collect(Collectors.toList());
        /* 组装标准 */
        var standardResultList = dailyRecordTargetItemService.getRecordTargetItemList(targetItemList);
        return DailyRecordTargetDetailResult.builder().standardList(standardResultList).workOrderList(workOrderResultList).build();
    }

    /**
     * 批量获取记录对象列表
     *
     * @param param 批量查询参数，包含任务ID、目标ID和记录ID等信息
     *
     * @return List<DailyRecordTargetErrorDTO> 批量获取记录对象列表
     */
    @Override
    public List<DailyRecordTargetErrorDTO> batchRecordTargetErrorList(DailyRecordTargetBatchParam param) {
        var targetList = this.lambdaQuery()
                .in(CollectionUtils.isNotEmpty(param.getIds()), DailyRecordTargetPO::getRecordId, param.getIds())
                .in(CollectionUtils.isNotEmpty(param.getTaskIds()), DailyRecordTargetPO::getTaskId, param.getTaskIds())
                .in(CollectionUtils.isNotEmpty(param.getTargetIds()), DailyRecordTargetPO::getTargetId, param.getTargetIds())
                .eq(StringUtils.isNotBlank(param.getExecutorResult()), DailyRecordTargetPO::getExecutionResult, param.getExecutorResult())
                .eq(StringUtils.isNotBlank(param.getExecutor()), DailyRecordTargetPO::getCreateBy, param.getExecutor())
                .list();
        if (CollectionUtils.isEmpty(targetList)) {
            return Collections.emptyList();
        }
        var taskIdSet = new HashSet<Long>();
        var recordIdSet = new HashSet<Long>();
        var targetIdSet = new HashSet<Long>();
        var createBySet = new HashSet<String>();
        targetList.forEach(targetItem -> {
            createBySet.add(targetItem.getCreateBy());
            taskIdSet.add(targetItem.getTaskId());
            recordIdSet.add(targetItem.getRecordId());
            targetIdSet.add(targetItem.getTargetId());
        });
        /* 获取人员name */
        var userParam = new UserMapQuery();
        userParam.setUserNames(createBySet);
        var userMap = userClient.getTenantUserMap(userParam);
        /* 获取对象的执行内容 */
        var targetItemMap = dailyRecordTargetItemService.getRecordTargetItemMap(recordIdSet, taskIdSet, targetIdSet, true);
        return targetList.stream().map(target -> {
            var user = userMap.getOrDefault(target.getCreateBy(), new UserDTO());
            var itemList = targetItemMap.getOrDefault(
                    String.valueOf(target.getTaskId()) + target.getRecordId() + target.getTargetId(), Collections.emptyList());
            var errNames = itemList.stream()
                    .filter(item -> Boolean.TRUE.equals(item.getErrorFlag()))
                    .map(DailyRecordTargetItemPO::getItemName)
                    .collect(Collectors.joining(","));
            return DailyRecordTargetErrorDTO.builder().taskId(target.getTaskId())
                    .taskName(target.getTaskName()).recordId(target.getRecordId())
                    .executorId(user.getId()).executorName(user.getName())
                    .executionTime(target.getCreateTime())
                    .executionResult(target.getExecutionResult())
                    .targetId(target.getTargetId()).targetName(target.getTargetName())
                    .targetCode(target.getTargetCode())
                    .targetCategory(target.getTargetCategory())

                    .targetErrorNames(errNames).build();
        }).collect(Collectors.toList());
    }

}
