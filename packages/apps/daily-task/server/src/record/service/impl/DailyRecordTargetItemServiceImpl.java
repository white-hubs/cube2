package com.guhecloud.cube.biz.daily.task.record.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.param.BusinessKeysParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.daily.task.commons.dto.DailyRecordStandardResultDTO;
import com.guhecloud.cube.biz.daily.task.commons.po.DailyRecordTargetItemPO;
import com.guhecloud.cube.biz.daily.task.record.dao.DailyRecordTargetItemMapper;
import com.guhecloud.cube.biz.daily.task.record.result.DailyRecordItemResult;
import com.guhecloud.cube.biz.daily.task.record.result.DailyRecordTargetStandardResult;
import com.guhecloud.cube.biz.daily.task.record.service.DailyRecordTargetItemService;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DailyRecordTargetItemServiceImpl extends ServiceImpl<DailyRecordTargetItemMapper, DailyRecordTargetItemPO> implements DailyRecordTargetItemService {

    private final FileClient fileClient;

    /**
     * 根据指定条件查询记录目标项，并按任务ID、记录ID和目标ID的组合作为键进行分组返回
     *
     * @param ids       记录ID列表，用于筛选对应的记录目标项
     * @param taskIds   任务ID列表，用于筛选对应的任务目标项
     * @param targetIds 目标ID列表，用于筛选对应的目标项
     * @param errorFlag 错误标识，用于筛选是否包含错误的记录目标项，可为空表示不限制
     *
     * @return 返回按"任务ID+记录ID+目标ID"组合作为键，对应的目标项列表作为值的映射关系
     */
    @Override
    public Map<String, List<DailyRecordTargetItemPO>> getRecordTargetItemMap(HashSet<Long> ids, HashSet<Long> taskIds,
                                                                             HashSet<Long> targetIds,
                                                                             Boolean errorFlag
                                                                            ) {
        // 根据传入的条件动态构建查询语句，只对非空列表进行IN查询
        var targetItemList = this.lambdaQuery()
                .in(CollectionUtils.isNotEmpty(ids), DailyRecordTargetItemPO::getRecordId, ids)
                .in(CollectionUtils.isNotEmpty(taskIds), DailyRecordTargetItemPO::getTaskId, taskIds)
                .in(CollectionUtils.isNotEmpty(targetIds), DailyRecordTargetItemPO::getTargetId, targetIds)
                .eq(errorFlag != null, DailyRecordTargetItemPO::getErrorFlag, errorFlag)
                .list();
        if (CollectionUtils.isEmpty(targetItemList)) {
            return new HashMap<>();
        }
        // 将查询结果按照任务ID、记录ID和目标ID的组合作为键进行分组
        return targetItemList.stream()
                .collect(Collectors.groupingBy(targetItem -> String.valueOf(targetItem.getTaskId()) + targetItem.getRecordId() +
                                                             targetItem.getTargetId()));
    }

    /**
     * 对日常记录目标项列表进行处理并按主ID分组
     *
     * @param recordTargetItemPOList 日常记录目标项PO列表
     *
     * @return 按主ID分组的日常记录标准结果映射表，键为主ID，值为对应的结果列表
     */
    @Override
    public Map<Long, List<DailyRecordTargetStandardResult>> getRecordTargetStandardMap(
            List<DailyRecordTargetItemPO> recordTargetItemPOList
                                                                                      ) {
        // 调用方法处理记录目标项列表
        var list = this.getRecordTargetItemList(recordTargetItemPOList);
        // 按主ID对处理结果进行分组
        return list.stream()
                .collect(Collectors.groupingBy(DailyRecordTargetStandardResult::getMasterId));
    }

    /**
     * 根据目标项列表构建标准化的执行记录结果，并关联对应的附件信息。
     *
     * @param recordTargetItemPOList 目标项数据列表，用于生成标准结果对象
     *
     * @return 标准化的每日记录结果列表，按标准ID分组并包含关联的文件信息
     */
    @Override
    public List<DailyRecordTargetStandardResult> getRecordTargetItemList(List<DailyRecordTargetItemPO> recordTargetItemPOList) {
        Map<Long, List<FileVO>> targetFileMap = new HashMap<>();
        var fileSet = new HashSet<Long>();

        // 构建目标对象执行结果列表，并收集文件引用ID
        var targetItemResultList = recordTargetItemPOList.stream().map(targetItem -> {
            fileSet.add(targetItem.getFileRefKey());
            return DailyRecordStandardResultDTO.builder().masterId(targetItem.getTargetId()).standardId(targetItem.getStandardId())
                    .standardName(targetItem.getStandardName()).itemId(targetItem.getItemId()).itemName(targetItem.getItemName())
                    .remark(targetItem.getRemark()).resultId(targetItem.getResultId())
                    .fileRefKey(targetItem.getFileRefKey()).resultId(targetItem.getResultId()).resultName(targetItem.getResultName())
                    .errorFlag(targetItem.getErrorFlag())
                    .build();
        }).toList();

        /* 批量获取目标对象相关联的附件信息 */
        if (CollectionUtils.isNotEmpty(fileSet)) {
            var fileParam = new BusinessKeysParam();
            fileParam.setBusinessKeys(new ArrayList<>(fileSet));
            targetFileMap = fileClient.getByBusinessKeys(fileParam);
        }

        Map<Long, List<FileVO>> finalTargetFileMap = targetFileMap;

        /* 将执行项与对应文件绑定，并按照标准ID进行分组 */
        var resultList = new ArrayList<DailyRecordTargetStandardResult>();
        targetItemResultList.stream().peek(item -> item.setFileList(finalTargetFileMap.get(item.getFileRefKey())))
                .collect(Collectors.groupingBy(dto -> String.valueOf(dto.getMasterId()) + dto.getStandardId()))
                .forEach((standardId, ls) -> {

                    var itemList = ls.stream().map(item -> DailyRecordItemResult
                            .builder().record(item.getMasterId()).standardId(item.getStandardId())
                            .itemId(item.getItemId())
                            .itemName(item.getItemName()).remark(item.getRemark()).resultId(item.getResultId())
                            .resultName(item.getResultName())
                            .errorFlag(item.getErrorFlag()).fileList(item.getFileList())
                            .build()).collect(Collectors.toList());
                    resultList.add(DailyRecordTargetStandardResult
                                           .builder().masterId(ls.getFirst().getMasterId()).standardId(ls.getFirst().getStandardId())
                                           .standardName(ls.getFirst().getStandardName()).itemList(itemList).build());
                });
        return resultList;
    }
}
